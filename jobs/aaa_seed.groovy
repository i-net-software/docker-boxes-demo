import com.inet.dsl.*

/**
 * Seed Job for the Jenkins setup we are using.
 * It will look in all job/*.groovy files for defined jobs and views
 * Jobs that are missing will be removed
 */
job('Seed Job') {
    label( "ubuntu-build" )                                // Only run on the master, since this is the instance to update
    logRotator {                                           // Keep logs for the last 5 runs
        numToKeep 5
    }
    scm {
        SVN.shared(delegate, 'docker-boxes-demo')           // Checkout via SVN, uses simplified syntax - see SVN.groovy
    }
    triggers {
        scm 'H 1 * * 1-5'                                   // Build regularly
    }
    steps {
        gradle {                                            // Run a clean gradle build to check for errors in the job definition
            tasks( 'clean test' )                           
            useWrapper( false )
            rootBuildScriptDir( "\${workspace}/docker-boxes-demo" )
        }
        dsl {                                               // Run the DSL.
            external(

                // Patterns of jobs to execute. Could also use e.g. System.getenv('BRANCH') for dynamics
                '**/jobs/*.groovy',
                '**/jobs/*shared/*.groovy',
                '**/jobs/*/*.groovy',
                '**/jobs/*views/*.groovy',
            )
            additionalClasspath 'docker-boxes-demo/src/main/groovy'
            removeAction('DELETE')
            removeViewAction('DELETE')
        }
    }
    publishers {
        wsCleanup()                                         // Clean up
    }
}