pipeline {
    agent any

    environment {
        AWS_CREDENTIALS = credentials('aws-credentials-file')
    }

    stages {
        stage('Load Secrets') {
            steps {
                script {
                    def awsCredentials = readJSON text: AWS_CREDENTIALS
                    env.AWS_ACCESS_KEY_ID = awsCredentials.AWS_ACCESS_KEY_ID
                    env.AWS_SECRET_ACCESS_KEY = awsCredentials.AWS_SECRET_ACCESS_KEY
                    env.AWS_ACCOUNT_ID = awsCredentials.AWS_ACCOUNT_ID
                    env.ECR_REGION = awsCredentials.ECR_REGION
                    env.ECR_REPOSITORY = awsCredentials.ECR_REPOSITORY
                    env.STAGING_BUCKET = awsCredentials.STAGING_BUCKET
                    env.PRODUCTION_BUCKET = awsCredentials.PRODUCTION_BUCKET
                    env.CLUSTER_NAME = awsCredentials.CLUSTER_NAME
                    env.BACKEND_SERVICE_NAME = awsCredentials.BACKEND_SERVICE_NAME
                    env.DOCKER_REGISTRY = "${awsCredentials.AWS_ACCOUNT_ID}.dkr.ecr.${awsCredentials.ECR_REGION}.amazonaws.com"
                }
            }
        }
        stage('Checkout') {
            steps {
                git 'https://github.com/ninganzaremy/goalglo.git'
            }
        }
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh './mvnw clean package'
                }
            }
        }
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'yarn install'
                    sh 'yarn run build'
                }
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry("https://${env.DOCKER_REGISTRY}", 'ecr:${env.ECR_REGION}:aws-credentials') {
                        def backendImage = docker.build("${env.ECR_REPOSITORY}:${env.BUILD_NUMBER}", './backend')
                        def frontendImage = docker.build("goalglo-frontend:${env.BUILD_NUMBER}", './frontend')

                        backendImage.push()
                        frontendImage.push()
                    }
                }
            }
        }
        stage('Update Task Definition') {
            steps {
                script {
                    def taskDefinition = readFile file: 'backend/ecs-task-definition.json'
                    def updatedTaskDefinition = taskDefinition.replace('${AWS_ACCOUNT_ID}.dkr.ecr.${ECR_REGION}.amazonaws.com/${ECR_REPOSITORY}', "${env.DOCKER_REGISTRY}/${env.ECR_REPOSITORY}:${env.BUILD_NUMBER}")
                    writeFile file: 'backend/ecs-task-definition-updated.json', text: updatedTaskDefinition
                }
            }
        }
        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    withAWS(credentials: 'aws-credentials', region: "${env.ECR_REGION}") {
                        sh "aws ecs update-service --cluster ${env.CLUSTER_NAME} --service ${env.BACKEND_SERVICE_NAME} --force-new-deployment"
                        sh "aws s3 sync ./frontend/dist s3://${env.STAGING_BUCKET}"
                    }
                }
            }
        }
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                script {
                    withAWS(credentials: 'aws-credentials', region: "${env.ECR_REGION}") {
                        sh "aws ecs update-service --cluster ${env.CLUSTER_NAME} --service ${env.BACKEND_SERVICE_NAME} --force-new-deployment"
                        sh "aws s3 sync ./frontend/dist s3://${env.PRODUCTION_BUCKET}"
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}