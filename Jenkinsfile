pipeline {
    agent any
    environment {
        AWS_CREDENTIALS_FILE = credentials('aws-credentials-file')
        PATH = "${env.PATH}:/usr/local/bin"
    }

    parameters {
        choice(name: 'BRANCH_NAME', choices: [''], description: 'Branch to build')
        choice(name: 'REPOSITORY_URL', choices: [''], description: 'Repository URL')
    }

    stages {
        stage('Load Secrets') {
            steps {
                script {
                    def envPrefix = params.BRANCH_NAME == 'develop' ? 'DEV' : 'PROD'
                    echo "Deployment to ****** ${envPrefix} ****** - Start"

                    echo "Stage: Load Secrets - Start"
                    def awsCredentialsContent = readFile(AWS_CREDENTIALS_FILE)
                    def awsCredentials = readJSON text: awsCredentialsContent

                    env.AWS_ACCOUNT_ID = awsCredentials.AWS_ACCOUNT_ID
                    env.ECR_REGION = awsCredentials.ECR_REGION
                    env.DOCKER_REGISTRY = "${awsCredentials.AWS_ACCOUNT_ID}.dkr.ecr.${awsCredentials.ECR_REGION}.amazonaws.com"

                    env.ECR_REPOSITORY = awsCredentials["${envPrefix}_ECR_REPOSITORY"]
                    env.BUCKET_NAME = awsCredentials["${envPrefix}_BUCKET_NAME"]
                    env.CLUSTER_NAME = awsCredentials["${envPrefix}_CLUSTER_NAME"]
                    env.BACKEND_SERVICE_NAME = awsCredentials["${envPrefix}_BACKEND_SERVICE_NAME"]
                    env.DB_URL = awsCredentials["${envPrefix}_DB_URL"]
                    env.DB_USER = awsCredentials["${envPrefix}_DB_USER"]
                    env.DB_PASSWORD = awsCredentials["${envPrefix}_DB_PASSWORD"]

                    echo "Stage: Load Secrets - Completed"
                }
            }
        }
        stage('Checkout') {
            steps {
                echo "Stage: Checkout - Start"
                checkout([$class: 'GitSCM', branches: [[name: "*/${params.BRANCH_NAME}"]],
                          userRemoteConfigs: [[url: "${params.REPOSITORY_URL}"]]])
                echo "Stage: Checkout - Completed"
            }
        }
        stage('Build and Test') {
            parallel {
                stage('Build Backend') {
                    steps {
                        echo "Stage: Build Backend - Start"
                        dir('backend') {
                            withEnv([
                                "SPRING_DATASOURCE_URL=${env.DB_URL}",
                                "SPRING_DATASOURCE_USERNAME=${env.DB_USER}",
                                "SPRING_DATASOURCE_PASSWORD=${env.DB_PASSWORD}",
                                "SPRING_JPA_HIBERNATE_DDL_AUTO=update",
                                "SPRING_JPA_SHOW_SQL=true"
                            ]) {
                                sh './mvnw clean package'
                            }
                        }
                        echo "Stage: Build Backend - Completed"
                    }
                }
                stage('Build Frontend') {
                    steps {
                        echo "Stage: Build Frontend - Start"
                        dir('frontend') {
                            nodejs('NodeJS') {
                                sh '''
                                yarn install --network-timeout 100000 --frozen-lockfile
                                yarn build
                                '''
                            }
                        }
                        echo "Stage: Build Frontend - Completed"
                    }
                }
            }
        }
        stage('Docker Build & Push Backend') {
            steps {
                echo "Stage: Docker Build & Push Backend - Start"
                script {
                    writeFile file: 'Dockerfile.backend', text: '''
                    # Use the official OpenJDK image as a base
                    FROM openjdk:11-jre-slim

                    # Set the working directory
                    WORKDIR /app

                    # Copy the jar file
                    COPY target/*.jar app.jar

                    # Expose the port the application runs on
                    EXPOSE 8080

                    # Run the application
                    ENTRYPOINT ["java", "-jar", "app.jar"]
                    '''

                    withCredentials([usernamePassword(credentialsId: 'aws-ecr-credentials', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        sh '''
                        set +x
                        aws ecr get-login-password --region ${ECR_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
                        docker build -t ${DOCKER_REGISTRY}/${ECR_REPOSITORY}:${BUILD_NUMBER} -f Dockerfile.backend ./backend
                        docker push ${DOCKER_REGISTRY}/${ECR_REPOSITORY}:${BUILD_NUMBER}
                        set -x
                        '''
                    }
                }
                echo "Stage: Docker Build & Push Backend - Completed"
            }
        }
        stage('Deploy Frontend to S3') {
            steps {
                echo "Stage: Deploy Frontend to S3 - Start"
                script {
                    sh '''
                    set +x
                    aws s3 sync ./frontend/dist s3://${BUCKET_NAME} --delete
                    aws s3 website s3://${BUCKET_NAME} --index-document index.html --error-document index.html
                    set -x
                    '''
                }
                echo "Stage: Deploy Frontend to S3 - Completed"
            }
        }
        stage('Deploy Backend to ECR') {
            steps {
                echo "Stage: Deploy to Environment - Start"
                script {
                    withAWS(credentials: 'aws-ecr-credentials', region: "${env.ECR_REGION}") {
                        sh '''
                        set +x
                        aws ecs update-service --cluster ${CLUSTER_NAME} --service ${BACKEND_SERVICE_NAME} --force-new-deployment
                        set -x
                        '''
                    }
                }
                echo "Stage: Deploy to Environment - Completed"
            }
        }
    }
    post {
        always {
            echo "Stage: Clean Workspace - Start"
            cleanWs()
            echo "Stage: Clean Workspace - Completed"
        }
    }
}