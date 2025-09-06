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
