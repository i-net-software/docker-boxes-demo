import com.inet.dsl.*

/**
 * Build All Job
 * Uses that collected Jobs from AllJobsCollector
 */
job('___ Build All ___') {
    label( "master" )                                       // Only run on the master, since this is the instance to update
    logRotator {                                            // Keep logs for the last 5 runs
        numToKeep 5
    }
    triggers {
        scm 'H 2 * * 1-5'                                   // Build regularly
    }
    steps {
                                                            // Create List of Jobs
        downstreamParameterized {
        
            println "Jobs collected: " + AllJobsCollector.getJobList()
            AllJobsCollector.jobList.sort { a, b ->         // Get and sort the Jobs from the Collector
                a.key <=> b.key }.each { prioritoy, jobs ->

                    trigger( jobs.join(', ') ) {            // Create a trigger of all the jobs for this priority
                        block {
                            buildStepFailure('FAILURE')
                            failure('FAILURE')
                            unstable('UNSTABLE')
                        }
                    }
            }
        }        
    }
}