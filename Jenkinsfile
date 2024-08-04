pipeline {
    agent any
    environment {
        AWS_CREDENTIALS_FILE = credentials('AWS_CREDENTIALS_FILE')
    }

    parameters {
        choice(name: 'BRANCH_NAME', choices: ['test', 'main'], description: 'Branch to build')
        choice(name: 'REPOSITORY_URL', choices: ['https://github.com/ninganzaremy/goalglo.git'], description: 'Repository URL')
    }

    stages {
        stage('Load Secrets') {
            steps {
                script {
                    def envPrefix = params.BRANCH_NAME == 'test' ? 'DEV' : 'PROD'

                    echo "Deployment to ****** ${envPrefix} ****** - Start"

                    echo "Stage: Load Secrets - Start"

                    def awsCredentialsContent = readFile(AWS_CREDENTIALS_FILE)
                    def jsonSlurper = new groovy.json.JsonSlurper()
                    def awsCredentials = jsonSlurper.parseText(awsCredentialsContent)

                    env.AWS_ACCOUNT_ID = awsCredentials.AWS_ACCOUNT_ID
                    env.ECR_REGION = awsCredentials.ECR_REGION
                    env.DOCKER_REGISTRY = "${awsCredentials.AWS_ACCOUNT_ID}.dkr.ecr.${awsCredentials.ECR_REGION}.amazonaws.com"
                    env.ECR_FRONTEND_REPOSITORY = awsCredentials["${envPrefix}_ECR_FRONTEND_REPOSITORY"]
                    env.ECR_BACKEND_REPOSITORY = awsCredentials["${envPrefix}_ECR_BACKEND_REPOSITORY"]
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
        stage('Build, Push, and Deploy') {
            steps {
                script {
                    echo "Stage: Build, Push, and Deploy - Start"
                    withCredentials([usernamePassword(credentialsId: 'AWS_ECR_CREDENTIALS', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {

                        echo "AWS ECR Login - Start"

                        sh '''
                        aws ecr get-login-password --region ${ECR_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
                        '''

                        echo "AWS ECR Login - Completed"

                        parallel(
                            buildBackend: {
                                stage('Build and Push Backend') {
                                    echo "Stage: Build and Push Backend - Start"
                                    dir('backend') {
                                        withEnv([
                                            "SPRING_DATASOURCE_URL=${env.DB_URL}",
                                            "SPRING_DATASOURCE_USERNAME=${env.DB_USER}",
                                            "SPRING_DATASOURCE_PASSWORD=${env.DB_PASSWORD}",
                                            "SPRING_JPA_HIBERNATE_DDL_AUTO=update",
                                            "SPRING_JPA_SHOW_SQL=true",
                                            "SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver",
                                            "SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect"
                                        ]) {
                                            echo "Building Backend Application - Start"

                                            sh '''
                                            ./mvnw clean package
                                            cat <<EOF > Dockerfile
                                            FROM openjdk:11-jre-slim
                                            WORKDIR /app
                                            COPY target/*.jar app.jar
                                            EXPOSE 8080
                                            ENTRYPOINT ["java", "-jar", "app.jar"]
                                            EOF
                                            docker build -t ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER} .
                                            docker push ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER}
                                            '''

                                            echo "Building and Push Backend Docker Image - Completed"
                                        }
                                    }
                                    echo "Stage: Build and Push Backend - Completed"
                                }
                            },
                            buildFrontend: {
                                stage('Build and Push Frontend') {

                                    echo "Stage: Build and Push Frontend - Start"

                                    dir('frontend') {

                                      echo "Building Frontend Application - Start"

                                        sh '''
                                        yarn install --network-timeout 100000 --frozen-lockfile
                                        yarn build

                                        cat <<EOF > Dockerfile
                                        FROM nginx:alpine
                                        COPY dist /usr/share/nginx/html
                                        EXPOSE 80
                                        CMD ["nginx", "-g", "daemon off;"]
                                        EOF
                                        docker build -t ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER} .
                                        docker push ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER}
                                        '''
                                    }
                                    echo "Stage: Build and Push Frontend - Completed"
                                }
                            }
                        )

                        stage('Deploy Frontend to S3') {
                            script {

                                echo "Stage: Deploy Frontend to S3 - Start"

                                sh '''
                                aws s3 sync ./frontend/dist s3://${BUCKET_NAME} --delete
                                aws s3 website s3://${BUCKET_NAME} --index-document index.html --error-document index.html
                                '''

                                echo "Stage: Deploy Frontend to S3 - Completed"
                            }
                        }

                        stage('Deploy Backend to ECS') {
                           script {

                                echo "Stage: Deploy Backend to ECS - Start"

                                sh '''
                                aws ecs update-service --cluster ${CLUSTER_NAME} --service ${BACKEND_SERVICE_NAME} --region ${ECR_REGION} --force-new-deployment
                                '''

                                echo "Stage: Deploy Backend to ECS - Completed"
                            }
                        }
                    }
                    echo "Stage: Build, Push, and Deploy - Completed"
                }
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