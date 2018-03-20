#!groovy
node() {
    
    env.JAVA_HOME = "/opt/java/jdk1.8.0_162"

    stage('Download Fortune Sources') { 
        sh "rm -rf *"
        echo "1. Download Sources for Server"
        sh "git clone https://github.com/goks-krish/fortune.git"
    }

    stage('Run Code Analysis') { 
        echo "2. Run Code Analysis"
        dir("fortune") {
            sh "mvn clean"
            sh "mvn checkstyle:checkstyle"
        }
        step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: '**/target/checkstyle-result.xml', healthy:'20', unHealthy:'100'])
    }
    
    stage('Build Jar') { 
        echo "3. Build Jar"
        dir("fortune") {
            sh "mvn install"
        }
    }
    
    stage('Build Docker') { 
        echo "4. Build Docker"
        dir("fortune") {
            sh "docker build -t gokskrish/fortune:latest ."
        }
    }
    
    stage('Upload to Docker Hub') { 
        echo "5. Upload to Docker Hub"
        dir("fortune") {
            sh "docker push gokskrish/fortune"
        }
    }
    
    stage('Download Fortune-UI Sources') { 
        echo "6. Download Sources for UI"
        sh "git clone https://github.com/goks-krish/fortune-ui.git"
    }
    
    stage('Build Docker for UI') { 
        echo "7. Build Docker for UI"
        dir("fortune-ui") {
            sh "docker build -t gokskrish/fortune-ui:latest ."
        }
    }

    stage('Upload to UI Docker Hub') { 
        echo "8. Upload to Docker Hub"
        dir("fortune-ui") {
            sh "docker push gokskrish/fortune-ui"
        }
    }

}
	