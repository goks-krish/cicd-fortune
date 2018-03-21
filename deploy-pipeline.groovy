#!groovy
node() {
    
    stage('Download CICD Sources') { 
        sh "rm -rf *"
        echo "1. Download Sources for Server"
        sh "git clone https://github.com/goks-krish/cicd-fortune.git"
    }

    stage('Create Cluster on GKE') { 
        echo "2. Create Cluster on GKE"
        sh 'gcloud beta container --project "rock-loop-418" clusters create "cluster-fortune" --zone "asia-south1-c" --username "admin" --cluster-version "1.8.8-gke.0" --machine-type "f1-micro" --image-type "COS" --disk-size "100" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" --num-nodes "3" --network "default" --enable-cloud-logging --enable-cloud-monitoring --subnetwork "default"'
        echo " Get credentials"
        sh 'gcloud container clusters get-credentials cluster-fortune --zone asia-south1-c --project rock-loop-418'
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