import jenkins.model.*

// Only one executors on master - this is for the seed job
Jenkins.instance.setNumExecutors(1)
