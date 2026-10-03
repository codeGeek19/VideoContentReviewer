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
        stage('Package') {
            steps {
                bat "mvn package -DskipTests -Dspring.profile=${params.ENV}"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                bat "copy /Y \"target\\ROOT.war\" \"${params.TOMCAT_HOME}\\webapps\\ROOT.war\""
            }
        }
    }

    post {
        success { echo "Deployed to ${params.ENV}: http://localhost:8080/" }
        failure { echo 'Pipeline failed, check the stage logs.' }
    }
}