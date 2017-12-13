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

    }
}