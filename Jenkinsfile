pipeline {
    agent any

    tools {
        maven 'Maven3.9.9'
        jdk 'JDK17'
        dockerTool 'Docker20'
    }

    environment {
        DOCKERHUB_USER = 'aysekoc'
        BACKEND_IMAGE = "hospitalappointmentsystem-backend"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/aysekocc/hospitalappointmentsystem.git'
            }
        }

        stage('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests'
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

        stage('Docker Build & Push Backend') {
            steps {
                withCredentials([string(credentialsId: 'dockerhub-credentials', variable: 'DOCKERHUB_PASS')]) {
                    // Windows CMD için --password-stdin yerine direkt değişkeni kullanıyoruz
                    bat """
                    docker login -u %DOCKERHUB_USER% -p %DOCKERHUB_PASS%
                    docker build -t %DOCKERHUB_USER%/%BACKEND_IMAGE%:latest -f Dockerfile .
                    docker push %DOCKERHUB_USER%/%BACKEND_IMAGE%:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose down || exit 0'
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
