pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        DOCKERHUB_USER = 'aysekoc481'
        DOCKERHUB_PASS = credentials('dockerhub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/aysekocc/hospitalappointmentsystem.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('hospitalappointmentsystem') {  // pom.xml burada
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('hospitalappointmentsystem\\hospital-frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    bat """
                    echo %DOCKERHUB_PASS% | docker login -u %DOCKERHUB_USER% --password-stdin

                    docker build -t %DOCKERHUB_USER%/hospitalappointmentsystem-backend:latest -f hospitalappointmentsystem\\Dockerfile hospitalappointmentsystem
                    docker push %DOCKERHUB_USER%/hospitalappointmentsystem-backend:latest

                    docker build -t %DOCKERHUB_USER%/hospitalappointmentsystem-frontend:latest -f hospitalappointmentsystem\\hospital-frontend\\Dockerfile hospitalappointmentsystem\\hospital-frontend
                    docker push %DOCKERHUB_USER%/hospitalappointmentsystem-frontend:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose down || exit /b 0'
                bat 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo 'Pipeline başarıyla tamamlandı!'
        }
        failure {
            echo 'Pipeline hata aldı!'
        }
    }
}
