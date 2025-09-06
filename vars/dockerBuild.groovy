// vars/dockerBuild.groovy
def call(Map args = [:]) {
  if (!args.image) error "image name required"
  if (!args.dockerfile) args.dockerfile = 'Dockerfile'
  // immutable tag: commit sha + build number
  def sha = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
  def tag = "${args.image}:${sha}-${env.BUILD_NUMBER}"
  sh "docker build -f ${args.dockerfile} -t ${tag} ."
  env.IMAGE_TAG = tag          // make global & visible in Jenkins UI
  return tag
}
