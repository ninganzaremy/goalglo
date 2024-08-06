pipeline {
    agent any
    environment {
        AWS_CREDENTIALS_FILE = credentials('AWS_CREDENTIALS_FILE')
    }

    parameters {
        choice(name: 'BRANCH_NAME', choices: [''], description: 'Branch to build')
        choice(name: 'REPOSITORY_URL', choices: [''], description: 'Repository URL')
    }

    stages {
        stage('Load Secrets') {
            steps {
                loadSecrets()
            }
        }
        stage('Checkout') {
            steps {
                checkoutCode()
            }
        }
        stage('Build Docker Images') {
            steps {
                buildDockerImages()
            }
        }
        stage('Push and Deploy') {
            steps {
                pushAndDeploy()
            }
        }
    }
    post {
        always {
            cleanWorkspace()
        }
    }
}

def loadSecrets() {
    script {
        def envPrefix = params.BRANCH_NAME == 'develop' ? 'DEV' : 'PROD'
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

def checkoutCode() {
    echo "Stage: Checkout - Start"
    checkout([$class: 'GitSCM', branches: [[name: "*/${params.BRANCH_NAME}"]],
              userRemoteConfigs: [[url: "${params.REPOSITORY_URL}"]]])
    echo "Stage: Checkout - Completed"
}

def buildDockerImages() {
    parallel(
        buildBackend: {
            buildBackendImage()
        },
        buildFrontend: {
            buildFrontendImage()
        }
    )
}

def buildBackendImage() {
    stage('Build Backend') {
        echo "Stage: Build Backend - Start"
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
                sh '''
                set +x
                docker build -t ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER} -f Dockerfile.backend .
                set -x
                '''
            }
            echo "Building Backend Docker Image - Completed"
        }
        echo "Stage: Build Backend - Completed"
    }
}

def buildFrontendImage() {
    stage('Build Frontend') {
        echo "Stage: Build Frontend - Start"
        dir('frontend') {
            sh '''
            set +x
            docker build -t ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER} -f Dockerfile.frontend .
            set -x
            '''
        }
        echo "Stage: Build Frontend - Completed"
    }
}

def pushAndDeploy() {
    script {
        echo "Stage: Push and Deploy - Start"
        withCredentials([usernamePassword(credentialsId: 'AWS_ECR_CREDENTIALS', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
            dockerLogin()
            parallel(
                pushBackend: {
                    pushBackendImage()
                },
                pushFrontend: {
                    /* pushFrontendImage() */
                }
            )
            parallel(
                deployFrontend: {
                    deployFrontendToS3()
                },
                deployBackend: {
                    deployBackendToECS()
                }
            )
        }
        echo "Stage: Push and Deploy - Completed"
    }
}

def dockerLogin() {
    echo "AWS ECR Login - Start"
    sh '''
    set +x
    aws ecr get-login-password --region ${ECR_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
    set -x
    '''
    echo "AWS ECR Login - Completed"
}

def pushBackendImage() {
    stage('Push Backend') {
        echo "Stage: Push Backend - Start"
        sh '''
        set +x
        docker push ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER}
        docker tag ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER} ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:latest
        docker push ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:latest
        set -x
        '''
        echo "Push Backend Docker Image - Completed"
    }
}

def pushFrontendImage() {
    stage('Push Frontend') {
        echo "Stage: Push Frontend - Start"
        sh '''
        set +x
        docker push ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER}
        docker tag ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER} ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:latest
        docker push ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:latest
        set -x
        '''
        echo "Push Frontend Docker Image - Completed"
    }
}

def deployFrontendToS3() {
    stage('Deploy Frontend To S3') {
        echo "Stage: Deploy Frontend to S3 - Start"
        sh '''
        set +x
        docker save ${DOCKER_REGISTRY}/${ECR_FRONTEND_REPOSITORY}:${BUILD_NUMBER} | tar -xOf - --wildcards '*/layer.tar' | tar -x -C ./dist && aws s3 sync ./dist/usr/share/nginx/html s3://${BUCKET_NAME} --delete
        aws s3 website s3://${BUCKET_NAME} --index-document index.html --error-document index.html
        set -x
        '''
        echo "Stage: Deploy Frontend to S3 - Completed"
    }
}

def deployBackendToECS() {
    stage('Deploy Backend to ECS') {
        echo "Stage: Deploy Backend to ECS - Start"
        sh '''
        set +x
        aws ecs update-service --cluster ${CLUSTER_NAME} --service ${BACKEND_SERVICE_NAME} --region ${ECR_REGION} --force-new-deployment
        set -x
        '''
        echo "Stage: Deploy Backend to ECS - Completed"
    }
}

def cleanWorkspace() {
    echo "Stage: Clean Workspace - Start"
    cleanWs()
    echo "Stage: Clean Workspace - Completed"
}