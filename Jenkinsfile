pipeline {
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: '', description: 'Branch to build')
        string(name: 'REPOSITORY_URL', defaultValue: '', description: 'Repository URL')
        choice(name: 'DEPLOY_CHOICE', choices: ['both', 'frontend', 'backend'], description: 'Choose what to deploy')
        booleanParam(name: 'UPDATE_API_GATEWAY', defaultValue: false, description: 'Update API Gateway with new endpoints')
    }
    stages {
        stage('Prepare Environment') {
            steps {
                prepareEnvironment()
            }
        }
        stage('Load Secrets') {
            steps {
                loadSecrets()
            }
        }
        stage('Checkout Code') {
            steps {
                checkoutCode()
            }
        }
        stage('Build') {
            parallel {
                stage('Build Backend') {
                    when {
                        expression { params.DEPLOY_CHOICE in ['both', 'backend'] }
                    }
                    steps {
                        script {
                            withAWS(credentials: 'AWS_JENKINS_ACCESS', region: "${ECR_REGION}") {
                                buildBackendImage()
                            }
                        }
                    }
                }
                stage('Build Frontend') {
                    when {
                        expression { params.DEPLOY_CHOICE in ['both', 'frontend'] }
                    }
                    steps {
                        script {
                            withAWS(credentials: 'AWS_JENKINS_ACCESS', region: "${ECR_REGION}") {
                                buildFrontend()
                            }
                        }
                    }
                }
            }
        }
        stage('Push and Deploy') {
            steps {
                script {
                    withAWS(credentials: 'AWS_JENKINS_ACCESS', region: "${ECR_REGION}") {
                        dockerLogin()
                        parallel(
                           deployBackend: {
                               if (params.DEPLOY_CHOICE in ['both', 'backend']) {
                                   pushBackendImage()
                                   script {
                                       deployBackendToECS()
                                       if (params.UPDATE_API_GATEWAY) {
                                           updateAPIGateway()
                                       }
                                   }
                               }
                           },
                           deployFrontend: {
                               if (params.DEPLOY_CHOICE in ['both', 'frontend']) {
                                   deployFrontendToS3()
                                   invalidateCloudFrontCache()
                               }
                           }
                        )
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWorkspace()
        }
    }
}

/* ==================== Function Definitions ====================*/
/**
 * Prepares the environment for the deployment process.
 * This method sets up necessary tools, configurations, and credentials required for the deployment pipeline.
 * It includes steps such as installing required software, configuring AWS CLI, and setting up any other
 * prerequisites for the build and deployment process.
 */
def prepareEnvironment() {
    script {
        def envPrefix = params.BRANCH_NAME == 'testing' ? 'PROD' : 'DEV'
        env.envPrefix = envPrefix

        def secretCredentialId = envPrefix == 'DEV' ? 'DEV_BACKEND_SECRET_FILE_NAME' : 'PROD_BACKEND_SECRET_FILE_NAME'
        def frontendSecretCredentialId = envPrefix == 'DEV' ? 'DEV_FRONTEND_SECRET_FILE_NAME' : 'PROD_FRONTEND_SECRET_FILE_NAME'
        def ecrRegionCredentialId = 'ECR_REGION'

        withCredentials([string(credentialsId: secretCredentialId, variable: 'BACKEND_SECRET_FILE_NAME')]) {
            env.BACKEND_SECRET_FILE_NAME = "${BACKEND_SECRET_FILE_NAME}"
        }

        withCredentials([string(credentialsId: frontendSecretCredentialId, variable: 'FRONTEND_SECRET_FILE_NAME')]) {
            env.FRONTEND_SECRET_FILE_NAME = "${FRONTEND_SECRET_FILE_NAME}"
        }

        withCredentials([string(credentialsId: ecrRegionCredentialId, variable: 'ECR_REGION')]) {
            env.ECR_REGION = "${ECR_REGION}"
        }
    }
}
/**
 * Loads secrets from AWS Secrets Manager and sets them as environment variables.
 * This method retrieves sensitive configuration data required for the deployment process.
 */
def loadSecrets() {
    script {
        def envPrefix = env.envPrefix
        echo "Deployment to ****** ${envPrefix} ****** - Start"
        echo "Stage: Load Secrets - Start"
        withAWS(credentials: 'AWS_JENKINS_ACCESS', region: "${ECR_REGION}") {
            def awsCredentialsContent = sh(
               script: """
                    set +x
                    aws secretsmanager get-secret-value --secret-id ${BACKEND_SECRET_FILE_NAME} --query SecretString --output text --region ${ECR_REGION}
                    set -x
                """,
               returnStdout: true
            ).trim()
            def jsonSlurper = new groovy.json.JsonSlurper()
            def awsCredentials = jsonSlurper.parseText(awsCredentialsContent)
            env.AWS_ACCOUNT_ID = awsCredentials.AWS_ACCOUNT_ID
            env.ECR_REGION = awsCredentials.ECR_REGION
            env.PRIMARY_REGION = awsCredentials.PRIMARY_REGION
            env.DOCKER_REGISTRY = "${awsCredentials.AWS_ACCOUNT_ID}.dkr.ecr.${awsCredentials.ECR_REGION}.amazonaws.com"
            env.ECR_BACKEND_REPOSITORY = awsCredentials["${envPrefix}_ECR_BACKEND_REPOSITORY"]
            env.BUCKET_NAME = awsCredentials["${envPrefix}_BUCKET_NAME"]
            env.CLUSTER_NAME = awsCredentials["${envPrefix}_CLUSTER_NAME"]
            env.BACKEND_SERVICE_NAME = awsCredentials["${envPrefix}_BACKEND_SERVICE_NAME"]
            env.CLOUDFRONT_DISTRIBUTION_ID = awsCredentials["${envPrefix}_CLOUDFRONT_DISTRIBUTION_ID"]
            env.VPC_ID = awsCredentials["${envPrefix}_VPC_ID"]
            env.TASK_FAMILY = awsCredentials["${envPrefix}_TASK_FAMILY"]
            env.ECS_SECURITY_GROUP = awsCredentials["${envPrefix}_ECS_SECURITY_GROUP"]
            env.LOG_GROUP_NAME = awsCredentials["${envPrefix}_LOG_GROUP_NAME"]
            env.PRIVATE_SUBNET_1 = awsCredentials["${envPrefix}_PRIVATE_SUBNET_1"]
            env.AWS_ACCESS_KEY_ID = awsCredentials["${envPrefix}_AWS_ACCESS_KEY_ID"]
            env.AWS_SECRET_ACCESS_KEY = awsCredentials["${envPrefix}_AWS_SECRET_ACCESS_KEY"]
            env.API_GATEWAY_NAME = awsCredentials["${envPrefix}_API_GATEWAY_NAME"]
            env.API_GATEWAY_ID = awsCredentials["${envPrefix}_API_GATEWAY_ID"]
        }
        echo "Stage: Load Secrets - Completed"
    }
}
/**
 * Checks out the specified branch from the Git repository.
 * This method is responsible for fetching the latest code from the version control system.
 */
def checkoutCode() {
    echo "Stage: Checkout - Start"
    checkout([
       $class: 'GitSCM', branches: [[name: "*/${params.BRANCH_NAME}"]],
       userRemoteConfigs: [[url: "${params.REPOSITORY_URL}"]]
    ])
    echo "Stage: Checkout - Completed"
}
/**
 * Builds the Docker image for the backend application.
 * This method compiles the backend code and packages it into a Docker container.
 */
def buildBackendImage() {
    echo "Stage: Build Backend - Start"
    dir('backend') {
        sh '''
        docker build --cache-from ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:latest \
            -t ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER} -f Dockerfile.backend .
        '''
    }
    echo "Stage: Build Backend - Completed"
}

/**
 * Builds the frontend application.
 * This method installs dependencies, compiles the frontend code, and prepares it for deployment.
 */
def buildFrontend() {
    echo "Stage: Build Frontend - Start"
    dir('frontend') {
        sh '''
        set +x
        # Fetch frontend secrets from AWS Secrets Manager
        FRONTEND_SECRETS=$(aws secretsmanager get-secret-value --secret-id ${FRONTEND_SECRET_FILE_NAME} --query SecretString --output text --region ${ECR_REGION})
        # Parse the JSON and create .env file
        echo $FRONTEND_SECRETS | jq -r 'to_entries[] | "\\(.key)=\\(.value)"' > .env
        set -x
        '''

        sh '''
        yarn install --frozen-lockfile
        '''
        sh '''
        yarn build
        '''
    }
    echo "Stage: Build Frontend - Completed"
}
/**
 * Performs Docker login to Amazon Elastic Container Registry (ECR).
 * This method authenticates the pipeline to allow pushing and pulling Docker images from ECR.
 */
def dockerLogin() {
    echo "AWS ECR Login - Start"
    sh '''
    set +x
    aws ecr get-login-password --region ${ECR_REGION} | docker login --username AWS --password-stdin ${DOCKER_REGISTRY}
    set -x
    '''
    echo "AWS ECR Login - Completed"
}
/**
 * Pushes the built backend Docker image to Amazon ECR.
 * This method uploads the backend container image to the specified ECR repository.
 */
def pushBackendImage() {
    echo "Stage: Push Backend - Start"
    sh '''
    docker push ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER}
    docker tag ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:${BUILD_NUMBER} ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:latest
    docker push ${DOCKER_REGISTRY}/${ECR_BACKEND_REPOSITORY}:latest
    '''
    echo "Push Backend Docker Image - Completed"
}
/**
 * Deploys the backend application to Amazon ECS (Elastic Container Service).
 * This method updates the ECS task definition and service to use the new Docker image.
 */
def deployBackendToECS() {
    echo "Stage: Deploy Backend to ECS - Start"
    sh """
        set -e
        set +x
        
        # Read the task definition template
        TASK_DEF_TEMPLATE=\$(cat config/task-definition.json)
        
        # Replace variables in the template using sed
        NEW_TASK_DEF_JSON=\$(echo "\$TASK_DEF_TEMPLATE" | sed -e 's|\${TASK_FAMILY}|'"${TASK_FAMILY}"'|g' \
            -e 's|\${envPrefix}|'"${envPrefix}"'|g' \
            -e 's|\${DOCKER_REGISTRY}|'"${DOCKER_REGISTRY}"'|g' \
            -e 's|\${ECR_BACKEND_REPOSITORY}|'"${ECR_BACKEND_REPOSITORY}"'|g' \
            -e 's|\${ECR_REGION}|'"${ECR_REGION}"'|g' \
            -e 's|\${AWS_ACCESS_KEY_ID}|'"${AWS_ACCESS_KEY_ID}"'|g' \
            -e 's|\${AWS_SECRET_ACCESS_KEY}|'"${AWS_SECRET_ACCESS_KEY}"'|g' \
            -e 's|\${BACKEND_SECRET_FILE_NAME}|'"${BACKEND_SECRET_FILE_NAME}"'|g' \
            -e 's|\${LOG_GROUP_NAME}|'"${LOG_GROUP_NAME}"'|g' \
            -e 's|\${AWS_ACCOUNT_ID}|'"${AWS_ACCOUNT_ID}"'|g')
        
        echo "Registering new task definition..."
        NEW_TASK_DEFINITION=\$(aws ecs register-task-definition \
            --cli-input-json "\$NEW_TASK_DEF_JSON" \
            --region ${ECR_REGION} \
            --query "taskDefinition.taskDefinitionArn" \
            --output text)
        
        echo "Updating ECS service..."
        aws ecs update-service \
            --cluster ${CLUSTER_NAME} \
            --service ${BACKEND_SERVICE_NAME} \
            --task-definition \$NEW_TASK_DEFINITION \
            --region ${ECR_REGION}
        
        set -x
    """
    echo "Stage: Deploy Backend to ECS - Completed"
}
/**
 * Deploys the frontend application to Amazon S3.
 * This method uploads the built frontend files to the specified S3 bucket with appropriate caching settings.
 */
def deployFrontendToS3() {
    echo "Stage: Deploy Frontend to S3 - Start"
    dir('frontend') {
        sh '''
        # Sync all assets except index.html with long cache
        aws s3 sync ./dist s3://${BUCKET_NAME}/ \
            --exclude "index.html" \
            --cache-control "max-age=31536000, public"

        # Sync index.html with no-cache
        aws s3 sync ./dist s3://${BUCKET_NAME}/ \
            --exclude "*" --include "index.html" \
            --cache-control "no-cache, no-store, must-revalidate"
        '''
    }
    echo "Deploy Frontend to S3 - Completed"
}
/**
 * Invalidates the CloudFront cache for the index.html file.
 * This ensures that users receive the latest version of the frontend application.
 */
def invalidateCloudFrontCache() {
    echo "Invalidating CloudFront cache for index.html"
    sh '''
    aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DISTRIBUTION_ID} --paths "/index.html"
    '''
    echo "CloudFront cache invalidation completed"
}
/**
 * Updates the API Gateway configuration.
 * This method sets up or updates the API Gateway to route requests to the backend service.
 */
def updateAPIGateway() {
    echo "Stage: Update API Gateway - Start"
    sh """
        set +x
    
        API_ID=${API_GATEWAY_ID}
    
        # Get the root resource ID
        ROOT_RESOURCE_ID=\$(aws apigateway get-resources --rest-api-id \$API_ID --region ${ECR_REGION} --query 'items[?path==`/`].id' --output text)
    
        # Create or get the proxy resource
        PROXY_RESOURCE_ID=\$(aws apigateway get-resources --rest-api-id \$API_ID --region ${ECR_REGION} --query 'items[?pathPart==`{proxy+}`].id' --output text)
        if [ -z "\$PROXY_RESOURCE_ID" ]; then
            echo "Creating proxy resource..."
            PROXY_RESOURCE_ID=\$(aws apigateway create-resource --rest-api-id \$API_ID --parent-id \$ROOT_RESOURCE_ID --path-part "{proxy+}" --region ${ECR_REGION} --query 'id' --output text)
        else
            echo "Proxy resource already exists."
        fi
    
        # Create or update ANY method for the proxy resource
        METHOD_EXISTS=\$(aws apigateway get-method --rest-api-id \$API_ID --resource-id \$PROXY_RESOURCE_ID --http-method ANY --region ${ECR_REGION} --query 'httpMethod' --output text || true)
        if [ "\$METHOD_EXISTS" != "ANY" ]; then
            echo "Creating ANY method for proxy resource..."
            aws apigateway put-method --rest-api-id \$API_ID --resource-id \$PROXY_RESOURCE_ID --http-method ANY --authorization-type NONE --region ${ECR_REGION}
        else
            echo "ANY method already exists for proxy resource."
        fi
    
        # Update integration with the new ECS service
        SERVICE_ENDPOINT="http://${env.BACKEND_SERVICE_NAME}.${env.CLUSTER_NAME}.${env.ECR_REGION}.amazonaws.com/{proxy}"
        aws apigateway put-integration --rest-api-id \$API_ID --resource-id \$PROXY_RESOURCE_ID --http-method ANY --type HTTP_PROXY --integration-http-method ANY --uri "\$SERVICE_ENDPOINT" --region ${ECR_REGION} --passthrough-behavior WHEN_NO_MATCH --timeout-in-millis 29000 --cache-key-parameters "method.request.path.proxy"
    
        # Redeploy the API
        aws apigateway create-deployment --rest-api-id \$API_ID --stage-name ${envPrefix} --region ${ECR_REGION}
    
        set -x
    """
    echo "Stage: Update API Gateway - Completed"
}
/**
 * Cleans up the Jenkins workspace after the deployment process.
 * This method removes temporary files and artifacts to prepare for the next build.
 */
def cleanWorkspace() {
    echo "Stage: Clean Workspace - Start"
    cleanWs()
    echo "Stage: Clean Workspace - Completed"
}