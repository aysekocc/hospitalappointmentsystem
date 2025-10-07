pipeline {
    agent any

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

        stage('Install JDK & Maven') {
            steps {
                sh '''
                # JDK ve Maven yükle (Alpine/Debian tabanlı container için)
                apt-get update && apt-get install -y openjdk-21-jdk maven
                export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH
                java -version
                mvn -version
                '''
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
                    sh '''
                    echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin
                    docker build -t $DOCKERHUB_USER/$BACKEND_IMAGE:latest -f Dockerfile .
                    docker push $DOCKERHUB_USER/$BACKEND_IMAGE:latest
                    '''
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
