#!groovy
node() {
    
    stage('Download CICD Sources') { 
        sh "rm -rf *"
        echo "1. Download Sources for Server"
        sh "git clone https://github.com/goks-krish/cicd-fortune.git"
    }

    stage('Create Cluster on GKE') { 
        echo "2. Create Cluster on GKE"
    }

    stage('Deploy on Kubernetes') { 
        echo "3. Deploy on Kubernetes"
         dir("cicd-fortune") {
            sh "kubectl create -f fortune.yaml"
        }
        sh "kubectl get services"
        sh "kubectl get rc"
        sh "kubectl get pods"
    }
    
}