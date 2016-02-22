import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import javaposse.jobdsl.plugin.*
import hudson.scm.SubversionSCM

/**
 * As soon as the jobdsl plugin is available,
 * Create thios seed job and trigger it to be build later on
 */
if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "job-dsl" } != null ) {

    def jobName = 'Seed Job'
    def instance = Jenkins.getInstance()

    //*
    instance.getJobNames().each {
        if ( it == jobName) {
            println "'" + jobName + "' already exists."
            instance = null
            return
        }
    }
    /*/
    //*/

    if ( !instance ) { return }
    def project = new FreeStyleProject(instance, jobName)
    project.setAssignedLabel( new hudson.model.labels.LabelAtom('master') )
    
    // We are using predefined user id dev. You change it in the global config
    def scm = new SubversionSCM("https://github.com/i-net-software/docker-boxes-demo", "", "docker-boxes-demo")
    project.setScm(scm)

    // Get script execute from checked out git repository:
    def jobDslBuildStep = new ExecuteDslScripts(
        scriptLocation = new ExecuteDslScripts.ScriptLocation(value = "false", targets="**/jobs/**/*seed.groovy", scriptText=""),
        ignoreExisting = false,
        removedJobAction = RemovedJobAction.DELETE,
        removedViewAction = RemovedViewAction.DELETE,
        lookupStrategy = LookupStrategy.JENKINS_ROOT,
        additionalClasspath = 'docker-boxes-demo/src/main/groovy'
    );

    project.getBuildersList().add(jobDslBuildStep)
    
    // Attention: Recursion. The updated seed job has to remove this trigger.
    project.getPublishersList().add(new hudson.tasks.BuildTrigger(jobName, false))
    
    project.save()

    instance.reload()
    println "'" + jobName + "' created."

    (instance.getAllItems(FreeStyleProject.class).find { it.name == jobName }).scheduleBuild2( 20 )
}