package com.inet.dsl

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import com.inet.dsl.*

/**
 * Creating a Gradle Build as we do for most of our projects.
 * Usage:
 *          new GradleJobBuilder(
 *              name: 'NAME'
 *              helpdesk: [ 'PROJECT' ]
 *          ).build(this)
 */
class GradleJobBuilder {

    String name                                                 // Name of the Build Job in Jenkins
    String description                                          // Description of the Build Job in Jenkins
    String artifacts                                            // ANT-pattern for artefacts to publish
    List<String> emails = []                                    // Emails to notify

    // Gradle BuildSteps
    List<HashMap<String,Object>> gradleSteps
    String rootBuildScriptDir                                   // Root-Build Dir for the gradle step. Will be generated if needed
    String buildFile                                            // Build file for the gradle task. Will override the rootBuildScriptDir
    String tasks = ':uploadArchives :check'                     // gradle task to run
    Boolean useWrapper = false                                  // whether to use the gradlew in the project or not
    
    // Pre-defined values
    String label = 'ubuntu-build'                               // Label of the Node to run the job
    String pollScmSchedule = 'H/10 * * * 1-5'                   // cron-schedule when to check the SCM for changes
    String junitResults = '**/build/test-results/**/*.xml'      // junit results to fetch
    
    // SVN Lists for specific projects
    // You only have to define the projects name in the list
    List<String> demo1 = []                                     // Demo1 projects from SVN
    List<String> demo2 = []                                     // Demo2 projects from SVN

    List<Map<String, String>> github = []                       // Shared projects from GIT
    List<Map<String, String>> subversion = []                   // Shared projects from SVN
    
    JobType priority = JobType.PLUGIN

    /**
     * Builder for the job. Usage see above.
     * @param dslFactory - context we are running in
     */
    Job build(DslFactory dslFactory) {
        
        def displayName = name
        name = name.replaceAll(' ', '-')
        AllJobsCollector.addJob( name, priority )
        
        dslFactory.job(name) {
            it.description this.description
            it.label this.label
            it.displayName displayName

            // How much to keep
            logRotator {
                numToKeep 5
            }
            
            // Check all SVN definitions and add them for checkout
            multiscm {
                demo1.each { SVN.demo( delegate, it ) }
                demo2.each { SVN.demo( delegate, it ) }

                this.github.each { gh ->
                    git {
                        remote {
                            github( gh.get('project') )
                        }
                        branch( gh.get('branch')?:"master" )
                        relativeTargetDir( gh.get('directory')?:"." )
                    }
                }
                
                this.subversion.each { svn ->
                    SVN.checkout(delegate, svn.get('url'), svn.get('directory')?:"." )
                }

                // we always checkout the docker-boxes.
                // Just for showcase reasons here ... can be removed
                // SVN.demo( delegate, 'docker-boxes' )
            }
            
            // Add xnvc if the label of the executor is '.*gui'
            wrappers {
                if ( label.contains('gui') ) {
                    xvnc {}
                }
            }

            // When to run
/*            triggers {
                scm pollScmSchedule
            }
*/            
            // What to do
            // If no gradle Steps are defined, just make the now
            if ( !this.gradleSteps ) {
                this.gradleSteps = [
                    new GradleStep(
                        rootBuildScriptDir: this.rootBuildScriptDir,
                        buildFile: this.buildFile,
                        tasks: this.tasks,
                        useWrapper: this.useWrapper,
                    ).step(),
                ]
            }
            
            steps {
                // This is just for gradle steps.
                // Iterate over all the steps and create the builder
                this.gradleSteps.each { step ->
                    gradle {
                        tasks( "clean " + step.tasks )
                        makeExecutable( step.useWrapper )
                        useWrapper( step.useWrapper )

                        if ( !step.rootBuildScriptDir ) {
                            // If no Root given, try to guess it
                            // Use the first defined checkout location
                            step.rootBuildScriptDir =
                                demo1?demo1[0]:
                                    (demo2?demo2[0]:".")
                        } else {
                            // Prefix with the workspace
                            rootBuildScriptDir( step.rootBuildScriptDir )
                        }

                        // If buildFile is set, use it ...
                        if ( step.buildFile ) {
                            buildFile( step.buildFile )
                        } else
                        {
                            // Prefix with the workspace
                            rootBuildScriptDir( "\${workspace}/" + step.rootBuildScriptDir )
                        }
                    }
                }
            }
            
            // What to keep
            publishers {
                if (junitResults && junitResults.length() > 0 ) { archiveJunit junitResults }
                if (artifacts && artifacts.length() > 0 ) { archiveArtifacts artifacts }
                if (emails) { mailer emails.join(' ') }
                
                // Clean only on windows machines
                if ( !(label.startsWith('ubuntu') || label.startsWith('fedora'))  ) {
                    wsCleanup {
                        cleanWhenFailure( false )
                        cleanWhenUnstable( false )
                    }
                }
            }
        }
    }
}