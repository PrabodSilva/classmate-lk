pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'main', url: 'https://github.com/PrabodSilva/classmate-lk.git'
            }
        }
        stage('Build Eureka Server') {
            steps {
                echo 'Building eureka-server...'
                dir('eureka-server') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        stage('Done') {
            steps {
                echo 'CI pipeline finished successfully!'
            }
        }
    }
}