pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'nani682'
        IMAGE_NAME = 'atpl-demo'
        IMAGE_TAG = "1.${BUILD_NUMBER}"
        DOCKER_IMAGE = "${DOCKER_HUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"
        K8S_DEPLOYMENT = 'src/K8S/deployment.yaml'
        K8S_SERVICE = 'src/K8S/service.yaml'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building application with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing image to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'
                sh "kubectl apply -f ${K8S_DEPLOYMENT}"
                sh "kubectl apply -f ${K8S_SERVICE}"
                sh "kubectl rollout status deployment/atpl-demo-deployment"
            }
        }

        stage('Verify') {
            steps {
                echo 'Verifying deployment...'
                sh 'kubectl get pods'
                sh 'kubectl get services'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
            echo 'App is live at: http://localhost/demo/'
        }
        failure {
            echo 'Pipeline failed! Check logs above.'
        }
    }
}