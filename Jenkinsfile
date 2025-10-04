pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        DOCKERHUB_USER = 'aysekoc481'
        DOCKERHUB_PASS = credentials('dockerhub-credentials')
        BACKEND_IMAGE = "hospitalappointmentsystem-backend"
        FRONTEND_IMAGE = "hospitalappointmentsystem-frontend"
        PATH = "${tool 'Maven3'}\\bin;${tool 'JDK21'}\\bin;%PATH%"
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
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('hospital-frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // Windows için Docker login
                    bat """
                    echo %DOCKERHUB_PASS% | docker login -u %DOCKERHUB_USER% --password-stdin
                    docker build -t %DOCKERHUB_USER%/%BACKEND_IMAGE%:latest -f backend/Dockerfile backend
                    docker push %DOCKERHUB_USER%/%BACKEND_IMAGE%:latest
                    docker build -t %DOCKERHUB_USER%/%FRONTEND_IMAGE%:latest -f hospital-frontend/Dockerfile hospital-frontend
                    docker push %DOCKERHUB_USER%/%FRONTEND_IMAGE%:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    bat 'docker-compose down || exit 0'
                    bat 'docker-compose up -d'
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
