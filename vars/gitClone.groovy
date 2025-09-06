def call(String branch = 'main', String url, String credId = null) {
    // if (fileExists('.git')) {
    //     echo "Repo already cloned. Pulling latest changes..."
    //     sh """
    //         git checkout ${branch}
    //         git pull origin ${branch}
    //     """
    // } else {
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
// }

// def buildProject(String buildCmd = 'mvn clean install') {
//     echo "Building project..."
//     sh "${buildCmd}"
// }

// def pushChanges(String branch = 'main', String commitMsg = "Automated commit", String credId = null, String repoUrl) {
//     if (credId) {
//         // Private repo push
//         withCredentials([usernamePassword(credentialsId: credId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
//             sh """
//                 git config user.email "jenkins@company.com"
//                 git config user.name "Jenkins CI"

//                 git add .
//                 git commit -m "${commitMsg}" || echo "No changes to commit"
//                 git push https://${GIT_USER}:${GIT_PASS}@${repoUrl} ${branch}
//             """
//         }
//     } else {
//         // Public repo push (only works if repo allows anonymous push, usually disabled on GitHub)
//         sh """
//             git config user.email "jenkins@company.com"
//             git config user.name "Jenkins CI"

//             git add .
//             git commit -m "${commitMsg}" || echo "No changes to commit"
//             git push ${repoUrl} ${branch}
//         """
//     }
// }
