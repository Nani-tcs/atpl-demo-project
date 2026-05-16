pipeline {
    agent none

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'java -version'
                sh 'mvn clean package -DskipTests'
                stash includes: 'target/*.jar', name: 'jar'
            }
        }

        stage('Docker Build & Push') {
            agent any
            steps {
                unstash 'jar'
                sh "docker build -t nani682/atpl-demo:1.${BUILD_NUMBER} ."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    sh "docker push nani682/atpl-demo:1.${BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy') {
            agent any
            steps {
                sh "kubectl apply -f src/K8S/deployment.yaml"
                sh "kubectl apply -f src/K8S/service.yaml"
            }
        }

        stage('Verify') {
            agent any
            steps {
                sh 'kubectl get pods'
            }
        }
    }

    post {
        success { echo 'Pipeline completed successfully!' }
        failure { echo 'Pipeline failed!' }
    }
}