def call(String branch = 'main', String url, String credId = null) {
        echo "Cloning fresh repo..."
        if (credId) {
            git branch: branch,
                url: url,
                credentialsId: credId
        } else {
            git branch: branch,
                url: url
        }
    }



 // Private repo
// gitClone('main', 'https://github.com/your-org/private-repo.git', 'github-cred-id')
// Public repo
// gitClone('main', 'https://github.com/your-org/public-repo.git')
 
