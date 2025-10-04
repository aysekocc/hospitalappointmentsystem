pipeline {
    agent any   // Pipeline herhangi bir node üzerinde çalışabilir

    tools {
        maven 'Maven3' // Jenkins'te Maven tanımlı olmalı
        jdk 'JDK17'    // Jenkins'te JDK tanımlı olmalı
    }

    environment {
        DOCKERHUB_USER = 'senin_dockerhub_username'
        DOCKERHUB_PASS = credentials('dockerhub-credentials') // Jenkins Credentials kısmına ekleyeceksin
        IMAGE_NAME = "randevu-sistemi"
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
                    sh "docker build -t $DOCKERHUB_USER/$IMAGE_NAME:latest ."
                    sh "docker push $DOCKERHUB_USER/$IMAGE_NAME:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Eğer sunucuda docker-compose varsa
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Pipeline başarıyla tamamlandı!'
        }
        failure {
            echo '❌ Pipeline hata aldı!'
        }
    }
}
