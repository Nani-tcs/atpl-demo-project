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
        stage('Find Java') {
            steps {
                sh '''#!/bin/bash
                    echo "=== Finding Java ==="
                    find / -name "java" -type f 2>/dev/null
                    echo "=== Current PATH ==="
                    echo $PATH
                    echo "=== Current JAVA_HOME ==="
                    echo $JAVA_HOME
                    echo "=== Which Java ==="
                    which java || echo "java not in PATH"
                '''
            }
        }
        stage('Build') {
            steps {
                sh '''#!/bin/bash
                    java -version
                    mvn clean package -DskipTests
                '''
            }
        }
    }
    post {
        success { echo 'Success!' }
        failure { echo 'Failed!' }
    }
}