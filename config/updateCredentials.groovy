import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

def changePassword = { username, new_password, id, description ->
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
        jenkins.model.Jenkins.instance
    )

    def result = null
    def c = creds.findResult { it.username == username ? it : null }
    def credentials_store = jenkins.model.Jenkins.instance.getExtensionList(
        'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
        )[0].getStore()


    if ( c ) {
        println "found credential ${c.id} for username ${c.username}"
        result = credentials_store.updateCredentials(
            com.cloudbees.plugins.credentials.domains.Domain.global(), 
            c, 
            new UsernamePasswordCredentialsImpl(c.scope, c.id, c.description, c.username, new_password)
            )

    } else {
        println "creating credential for ${username}"
        result = credentials_store.addCredentials(
            com.cloudbees.plugins.credentials.domains.Domain.global(), 
            new UsernamePasswordCredentialsImpl( com.cloudbees.plugins.credentials.CredentialsScope.GLOBAL, id, description, username, new_password)
        )
    }

    if (result) {
        println "password changed for ${username}" 
    } else {
        println "failed to change password for ${username}"
    }
}

// Use multiple times for other user:password credentials.
// Will create and update users
changePassword("jenkins", "jenkins", "docker-jenkins-user", "Jenkins password for Ubuntu Slaves")
