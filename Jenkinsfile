pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['dev', 'test'], description: 'Target environment')
        string(name: 'HOST_PORT', defaultValue: '8081', description: 'Host port for the container')
    }

    environment {
        IMAGE_NAME = 'vcrp'
        CONTAINER  = 'vcrp'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build') {
            steps { bat "mvn clean compile -Dspring.profile=${params.ENV}" }
        }
        stage('Test') {
            steps { bat 'mvn test -Dheadless=true' }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
                }
            }
        }
        stage('Package') {
            steps {
                bat "mvn package -DskipTests -Dspring.profile=${params.ENV}"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }
        stage('Docker Build') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    bat "docker build -t %DH_USER%/%IMAGE_NAME%:${env.BUILD_NUMBER} -t %DH_USER%/%IMAGE_NAME%:latest ."
                }
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    bat 'echo %DH_PASS%| docker login -u %DH_USER% --password-stdin'
                    bat "docker push %DH_USER%/%IMAGE_NAME%:${env.BUILD_NUMBER}"
                    bat 'docker push %DH_USER%/%IMAGE_NAME%:latest'
                }
            }
        }
        stage('Deploy Container') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    bat 'docker rm -f %CONTAINER% 2>nul & exit /b 0'
                    bat "docker run -d --name %CONTAINER% -p ${params.HOST_PORT}:8080 %DH_USER%/%IMAGE_NAME%:${env.BUILD_NUMBER}"
                    bat 'docker ps --filter name=%CONTAINER%'
                }
            }
        }
    }

    post {
        success { echo "Deployed container on http://localhost:${params.HOST_PORT}/" }
        failure { echo 'Pipeline failed. No image is pushed or deployed when tests fail.' }
    }
}