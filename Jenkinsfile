pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh '''#!/bin/bash
                    export JAVA_HOME=/opt/java/openjdk
                    export PATH=$JAVA_HOME/bin:$PATH
                    echo "JAVA_HOME=$JAVA_HOME"
                    ls $JAVA_HOME/bin/java
                    $JAVA_HOME/bin/java -version
                    JAVA_HOME=$JAVA_HOME mvn clean package -DskipTests
                '''
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t nani682/atpl-demo:1.${BUILD_NUMBER} ."
            }
        }
        stage('Docker Push') {
            steps {
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
            steps {
                sh "kubectl apply -f src/K8S/deployment.yaml"
                sh "kubectl apply -f src/K8S/service.yaml"
            }
        }
        stage('Verify') {
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