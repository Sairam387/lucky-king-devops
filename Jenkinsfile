groovy
pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn package -DskipTests'
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
            echo 'Lucky King CI pipeline completed successfully!'
        }

        failure {
            echo 'Lucky King CI pipeline failed!'
        }
    }
}
