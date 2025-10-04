pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        DOCKERHUB_USER = 'aysekoc481'
        DOCKERHUB_PASS = credentials('dockerhub-credentials')
        BACKEND_IMAGE = "hospitalappointmentsystem-backend"
        FRONTEND_IMAGE = "hospitalappointmentsystem-frontend"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/aysekocc/hospitalappointmentsystem.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    sh "echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin"

                    sh "docker build -t $DOCKERHUB_USER/$BACKEND_IMAGE:latest -f backend/Dockerfile backend"
                    sh "docker push $DOCKERHUB_USER/$BACKEND_IMAGE:latest"

                    sh "docker build -t $DOCKERHUB_USER/$FRONTEND_IMAGE:latest -f frontend/Dockerfile frontend"
                    sh "docker push $DOCKERHUB_USER/$FRONTEND_IMAGE:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
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
