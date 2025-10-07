pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'JDK21'
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
        sh 'docker-compose down || exit 0'
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
