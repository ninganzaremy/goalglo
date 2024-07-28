pipeline {
    agent any

    environment {
        AWS_CREDENTIALS = credentials('aws-credentials-file')
    }

    stages {
        stage('Load Secrets') {
            steps {
                script {
                    echo "Stage: Load Secrets - Start"
                    try {
                        def awsCredentials = readJSON text: AWS_CREDENTIALS
                        echo "AWS Credentials Loaded: ${awsCredentials}"

                        if (!awsCredentials.AWS_ACCESS_KEY_ID || !awsCredentials.AWS_SECRET_ACCESS_KEY) {
                            error "AWS credentials are not properly configured"
                        }

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
                        echo "Stage: Load Secrets - Completed"
                    } catch (Exception e) {
                        echo "Error loading AWS credentials: ${e.message}"
                        error "Failed to load AWS credentials"
                    }
                }
            }
        }
        stage('Checkout') {
            steps {
                echo "Stage: Checkout - Start"
                git 'https://github.com/ninganzaremy/goalglo.git'
                echo "Stage: Checkout - Completed"
            }
        }
        stage('Build Backend') {
            steps {
                echo "Stage: Build Backend - Start"
                dir('backend') {
                    sh './mvnw clean package'
                }
                echo "Stage: Build Backend - Completed"
            }
        }
        stage('Build Frontend') {
            steps {
                echo "Stage: Build Frontend - Start"
                dir('frontend') {
                    sh 'yarn install'
                    sh 'yarn run build'
                }
                echo "Stage: Build Frontend - Completed"
            }
        }
        stage('Docker Build & Push') {
            steps {
                echo "Stage: Docker Build & Push - Start"
                script {
                    docker.withRegistry("https://${env.DOCKER_REGISTRY}", "ecr:${env.ECR_REGION}:aws-credentials") {
                        def backendImage = docker.build("${env.ECR_REPOSITORY}:${env.BUILD_NUMBER}", './backend')
                        def frontendImage = docker.build("goalglo-frontend:${env.BUILD_NUMBER}", './frontend')

                        backendImage.push()
                        frontendImage.push()
                    }
                }
                echo "Stage: Docker Build & Push - Completed"
            }
        }
        stage('Update Task Definition') {
            steps {
                echo "Stage: Update Task Definition - Start"
                script {
                    def taskDefinition = readFile file: 'backend/ecs-task-definition.json'
                    def updatedTaskDefinition = taskDefinition.replace("${env.DOCKER_REGISTRY}/${env.ECR_REPOSITORY}", "${env.DOCKER_REGISTRY}/${env.ECR_REPOSITORY}:${env.BUILD_NUMBER}")
                    writeFile file: 'backend/ecs-task-definition-updated.json', text: updatedTaskDefinition
                }
                echo "Stage: Update Task Definition - Completed"
            }
        }
        stage('Deploy to Environment') {
            steps {
                echo "Stage: Deploy to Environment - Start"
                script {
                    if (params.BRANCH_NAME == 'develop') {
                        echo "Deploying to Staging Environment"
                        withAWS(credentials: 'aws-credentials', region: "${env.ECR_REGION}") {
                            sh "aws ecs update-service --cluster ${env.CLUSTER_NAME} --service ${env.BACKEND_SERVICE_NAME} --force-new-deployment"
                            sh "aws s3 sync ./frontend/dist s3://${env.STAGING_BUCKET}"
                        }
                    } else if (params.BRANCH_NAME == 'main') {
                        echo "Deploying to Production Environment"
                        withAWS(credentials: 'aws-credentials', region: "${env.ECR_REGION}") {
                            sh "aws ecs update-service --cluster ${env.CLUSTER_NAME} --service ${env.BACKEND_SERVICE_NAME} --force-new-deployment"
                            sh "aws s3 sync ./frontend/dist s3://${env.PRODUCTION_BUCKET}"
                        }
                    } else {
                        echo "Deployment for ${params.BRANCH_NAME} branch not configured"
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