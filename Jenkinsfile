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

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/lucky-king-1.0.0-SNAPSHOT.jar',
                             fingerprint: true
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
