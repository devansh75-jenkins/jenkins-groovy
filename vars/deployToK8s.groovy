// vars/deployToK8s.groovy
def call(Map args = [:]) {
    if (!args.image) {
        error "❌ Image tag required for deployment!"
    }
    if (!args.kubeconfig) {
        error "❌ Kubeconfig path required!"
    }
    if (!args.namespace) {
        args.namespace = "default"
    }
    if (!args.deployment) {
        error "❌ Deployment name required!"
    }

    echo "🚀 Deploying ${args.image} to namespace ${args.namespace}"

    sh """
    export KUBECONFIG=${args.kubeconfig}
    kubectl set image deployment/${args.deployment} ${args.deployment}=${args.image} -n ${args.namespace}
    kubectl rollout status deployment/${args.deployment} -n ${args.namespace}
    """
}
