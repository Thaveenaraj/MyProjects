pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID = "383053847833"
        AWS_REGION = "ap-south-1"
        ECR_REPO_NAME = "rdsproject-ap"
        ECR_URI = "383053847833.dkr.ecr.ap-south-1.amazonaws.com/${ECR_REPO_NAME}"
        DOCKER_IMAGE_TAG = "1.0"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📦 Checking out code from GitHub..."
                git branch: 'feature2', url: 'https://github.com/Thaveenaraj/MyProjects.git'
            }
        }

        stage('Maven Build') {
            steps {
                echo "🧱 Building project with Maven..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo "🐳 Building Docker image..."
                sh """
                docker build -t ${ECR_REPO_NAME}:${DOCKER_IMAGE_TAG} .
                """
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]){
                    sh """
                    aws ecr get-login-password --region ${AWS_REGION} \
                    | docker login --username AWS --password-stdin ${ECR_URI}
                    """
                }
            }
        }

        stage('Tag & Push Image to ECR') {
            steps {
                sh """
                docker tag ${ECR_REPO_NAME}:${DOCKER_IMAGE_TAG} ${ECR_URI}:${DOCKER_IMAGE_TAG}
                docker push ${ECR_URI}:${DOCKER_IMAGE_TAG}
                """
            }
        }

        stage('Deploy to EKS') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                    sh """
                    aws eks update-kubeconfig --region ${AWS_REGION} --name rds-cluster
                    kubectl set image deployment/rds-springboot-app rds-springboot-app=${ECR_URI}:${DOCKER_IMAGE_TAG} -n default
                    kubectl rollout status deployment/rds-springboot-app -n default
                    """
                }
            }
        }
    }

    post {
        success { echo "✅ Deployment Successful!" }
        failure { echo "❌ Deployment Failed!" }
    }
}
