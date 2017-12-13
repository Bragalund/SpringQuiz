pipeline {
    agent any
    stages {
        stage('Install all components') {
             tools {
                 maven "maven"
                 jdk "jdk"
             }
             steps {
               {
                 sh('mvn clean install verify')
               }
             }
        }

    }
}