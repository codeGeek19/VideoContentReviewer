pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['dev', 'test'], description: 'Target environment')
        string(name: 'TOMCAT_HOME', defaultValue: 'C:\\tomcat', description: 'Tomcat install folder')
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
        stage('Deploy') {
            steps {
                bat "if exist \"${params.TOMCAT_HOME}\\webapps\\ROOT\" rmdir /S /Q \"${params.TOMCAT_HOME}\\webapps\\ROOT\" & exit /b 0"
                bat "copy /Y \"target\\ROOT.war\" \"${params.TOMCAT_HOME}\\webapps\\ROOT.war\""
            }
        }
    }

    post {
        success { echo "Tests passed and deployed to ${params.ENV}: http://localhost:8080/" }
        failure { echo 'Pipeline failed. Deployment is skipped when tests fail.' }
    }
}