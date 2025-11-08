pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID = "383053847833"
        AWS_REGION = "ap-south-1"
        ECR_REPO_NAME = "rdsproject-ap"
        ECR_URI = "383053847833.dkr.ecr.ap-south-1.amazonaws.com/rdsproject-ap/${ECR_REPO_NAME}"
        DOCKER_IMAGE_TAG = "1.0"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📦 Checking out code from GitHub..."
                git branch: 'feature1', url: 'https://github.com/Thaveenaraj/MyProjects.git'
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

        stage('Login to AWS ECR') {
            steps {
                echo "🔐 Logging in to Amazon ECR..."
                sh """
                aws ecr get-login-password --region ${AWS_REGION} \
                | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                """
            }
        }

        stage('Tag & Push Image to ECR') {
            steps {
                echo "📤 Tagging and pushing Docker image to ECR..."
                sh """
                docker tag ${ECR_REPO_NAME}:${DOCKER_IMAGE_TAG} ${ECR_URI}:${DOCKER_IMAGE_TAG}
                docker push ${ECR_URI}:${DOCKER_IMAGE_TAG}
                """
            }
        }

        stage('Deploy to EKS') {
            steps {
                echo "🚀 Deploying latest Docker image to EKS..."
                sh """
                aws eks update-kubeconfig --region ${AWS_REGION} --name rds-cluster

                kubectl set image deployment/rds-springboot-deployment \
                rds-springboot-app=${ECR_URI}:${DOCKER_IMAGE_TAG} -n default

                kubectl rollout status deployment/rds-springboot-deployment -n default
                """
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful!"
        }
        failure {
            echo "❌ Deployment Failed!"
        }
    }
}
