pipeline {

    agent any

    stages {
        stage('Run user tests') {
          tools {
            maven "maven"
            jdk "jdk"
          }
          steps {
            dir('user'){
              sh('mvn clean test')
            }
          }
        }

        stage('Run quiz tests') {
          tools {
            maven "maven"
            jdk "jdk"
          }
          steps {
            dir('quiz'){
              sh('mvn clean test')
            }
          }
        }

        stage('Run highscore tests') {
         tools {
           maven "maven"
           jdk "jdk"
         }
         steps {
           dir('highscore'){
             sh('mvn clean test')
           }
         }
        }

        stage('Clean and run all'){
          tools {
            maven "maven"
            jdk "jdk"
          }
          steps {
            sh('mvn clean install verify')
          }
        }

        stage('Deploy microservice to google cloud') {
          steps {
            sh('gcloud auth activate-service-account --key-file /home/ubuntu/${SECRET_FILE_NAME}')
            sh('gcloud config set project ${ENTERPRISE2_PROJECT_ID}')
            sh('gcloud config set compute/zone ${ENTERPRISE2_ZONE}')
            sh('gcloud docker -- push eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_eureka:${BUILD_NUMBER}')
            sh('gcloud docker -- push eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_zuul:${BUILD_NUMBER}')
            sh('gcloud docker -- push eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_quiz1:${BUILD_NUMBER}')
            sh('gcloud docker -- push eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_highscore:${BUILD_NUMBER}')
            sh('gcloud docker -- push eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_user:${BUILD_NUMBER}')
            sh('gcloud container clusters get-credentials ${ENTERPRISE2_CLUSTER} --zone ${ENTERPRISE2_ZONE} --project ${ENTERPRISE2_PROJECT_ID}')
            sh('kubectl set image deployment/springquiz_eureka springquiz_eureka=eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_eureka:${BUILD_NUMBER}')
            sh('kubectl set image deployment/springquiz_zuul springquiz_zuul=eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_zuul:${BUILD_NUMBER}')
            sh('kubectl set image deployment/springquiz_quiz1 springquiz_quiz1=eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_quiz1:${BUILD_NUMBER}')
            sh('kubectl set image deployment/springquiz_highscore springquiz_highscore=eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_highscore:${BUILD_NUMBER}')
            sh('kubectl set image deployment/springquiz_user springquiz_user=eu.gcr.io/${ENTERPRISE2_PROJECT_ID}/springquiz_user:${BUILD_NUMBER}')
          }
        }

         stage('Clean up after deploy') {
           tools {
             maven "maven"
             jdk "jdk"
           }
           steps {
             sh('mvn clean')
           }
         }

    }
}