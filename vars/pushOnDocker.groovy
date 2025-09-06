// vars/pushImage.groovy
// -------------------------------------------------------
// 📌 Function: pushImage
// 🔹 Use: DockerHub/Registry pe login karke securely image push karta hai.
// 🔹 Safe: Credentials Jenkins se aate hai, local machine pe save nahi hote.
// 🔹 Cleanup: Temp Docker config delete karta hai login ke baad.
// -------------------------------------------------------

// vars/pushImage.groovy
def call(Map args = [:]) {
    if (!args.imageTag) error "pushImage: 'imageTag' argument required"
    if (!args.dockerCredsId) error "pushImage: 'dockerCredsId' argument required"

    def imageTag = args.imageTag
    def credsId = args.dockerCredsId
    def removeLocal = args.get('removeLocal', true)

    withCredentials([usernamePassword(credentialsId: credsId,
                                      usernameVariable: 'DOCKER_USER',
                                      passwordVariable: 'DOCKER_PASS')]) {
        sh """

          DOCKER_TMP=\$(mktemp -d /tmp/docker-cred-XXXXXX)
          export DOCKER_CONFIG="\$DOCKER_TMP"

          echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
          echo "✅ Docker login successful (temp config: \$DOCKER_CONFIG)"

          docker push ${imageTag}

          rm -rf "\$DOCKER_CONFIG"

          ${removeLocal ? "docker rmi -f ${imageTag} || true" : "echo 'Keeping local image'"}
        """
    }
}

