// vars/buildImage.groovy
// -------------------------------------------------------
// 📌 Function: buildImage
// 🔹 Use: Docker image build karta hai aur commit hash + build number ke sath tag banata hai.
// 🔹 Safe: Error aane par pipeline fail kar deta hai.
// 🔹 Return: Docker image tag (e.g. myorg/myapp:abcd1234-15)
// -------------------------------------------------------

def call(Map args = [:]) {
    if (!args.image) error "buildImage: 'image' argument required"
    def image = args.image
    def dockerfile = args.get('dockerfile','Dockerfile')
    def buildArgs = args.get('buildArgs', [])

    // Git commit short hash (first 8 chars)
    def commit = env.GIT_COMMIT?.take(8)
    // Agar commit available nahi to fallback build number
    def tag = commit ? "${image}:${commit}-${env.BUILD_NUMBER}" : "${image}:${env.BUILD_NUMBER}"

    echo "🔧 Building image ${tag} using ${dockerfile}"

    sh """
  
      docker build -f ${dockerfile} -t ${tag} ${buildArgs.join(' ')} .
    """

    // IMAGE_TAG ko environment variable bana do taaki agli stages use kar saken
    env.IMAGE_TAG = tag
    return tag
}




// @Library('shared-lib') _   // 📌 shared library use kar rahe hain (Manage Jenkins me add karna hota hai)

// pipeline {
//   agent { label 'docker-builder' } // 📌 Jenkins agent jisme docker installed hai

//   environment {
//     GIT_CRED_ID    = 'pass'       // 📌 GitHub repo ke liye Jenkins credentials
//     DOCKER_CRED_ID = 'docker1'    // 📌 Docker registry ke liye credentials
//   }

//   stages {
//     stage('Checkout Code') {
//       steps {
//         // 📌 GitHub se code checkout karna
//         checkout([$class: 'GitSCM',
//           branches: [[name: '*/main']],
//           userRemoteConfigs: [[url: 'https://github.com/devanshlodh666/ecom.git', credentialsId: env.GIT_CRED_ID]]
//         ])
//       }
//     }

//     stage('Build Docker Image') {
//       steps {
//         script {
//           // 📌 Image build karna (tag: imageName:commit-buildNo)
//           def tag = buildImage(image: 'devanshlodhi/djangoapp', dockerfile: 'Dockerfile')
//           echo "✅ Built image: ${tag}"
//         }
//       }
//     }

//     stage('Push Docker Image') {
//       steps {
//         script {
//           // 📌 Build ke baad image push karna
//           def toPush = env.IMAGE_TAG ?: error("IMAGE_TAG not set")
//           pushImage(imageTag: toPush, dockerCredsId: env.DOCKER_CRED_ID, removeLocal: true)
//           echo "✅ Image pushed successfully"
//         }
//       }
//     }

//     stage('Deploy to K8s') {
//       steps {
//         script {
//           // 📌 Deployment example (use IMAGE_TAG for update)
//           sh "kubectl set image deployment/ecom ecom=${env.IMAGE_TAG} -n production --record"
//           echo "🚀 Deployed ${env.IMAGE_TAG} to production"
//         }
//       }
//     }
//   }

//   post {
//     success { echo "🎉 Pipeline Success!" }
//     failure { echo "❌ Pipeline Failed!" }
//   }
// }
