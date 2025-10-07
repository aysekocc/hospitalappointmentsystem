pipeline {
    agent any

    tools {
        maven 'Maven3'   // Global Tool Configuration’daki Maven ismi
        jdk 'JDK21'      // Global Tool Configuration’daki JDK ismi
    }

    environment {
        DOCKERHUB_USER = 'aysekoc'
        BACKEND_IMAGE = "hospitalappointmentsystem-backend"
        JAVA_HOME = tool name: 'JDK21', type: 'jdk'  // JAVA_HOME ayarı
        PATH = "${JAVA_HOME}/bin:${env.PATH}"         // PATH’e ekleme
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/aysekocc/hospitalappointmentsystem.git'
            }
        }

        stage('Build Backend') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('hospital-frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Docker Build & Push Backend') {
            steps {
                withCredentials([string(credentialsId: 'dockerhub-credentials', variable: 'DOCKERHUB_PASS')]) {
                    sh """
                    echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin
                    docker build -t $DOCKERHUB_USER/$BACKEND_IMAGE:latest -f Dockerfile .
                    docker push $DOCKERHUB_USER/$BACKEND_IMAGE:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
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
