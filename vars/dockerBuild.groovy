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
      set -euo pipefail
      docker build -f ${dockerfile} -t ${tag} ${buildArgs.join(' ')} .
    """

    // IMAGE_TAG ko environment variable bana do taaki agli stages use kar saken
    env.IMAGE_TAG = tag
    return tag
}
