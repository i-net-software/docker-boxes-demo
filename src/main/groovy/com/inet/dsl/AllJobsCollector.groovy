package com.inet.dsl

/**
 * Collects all Jobs in a list
 * Can later be used to generate a build job with all configred jobs in it
 */ 
class AllJobsCollector {
    
    private static Map<JobType,List<String>> jobList = new HashMap()
    
    public static addJob( String name, JobType priority ) {
        
        List<String> jobs = jobList.get( priority )
        if ( !jobs ) {
            jobs = new ArrayList()
        }
        
        jobs.add( name )
        jobList.put( priority, jobs )
    }
    
    public static Map<JobType,List<String>> getJobList() {
        return jobList
    }
}