pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
    }

    post {
        success {
            echo 'Lucky King build successful!'
        }

        failure {
            echo 'Lucky King build failed!'
        }
    }
}