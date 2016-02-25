package com.inet.dsl

/**
 * Collects all Jobs so that they can be put into the Build All job
 */
class AllJobsCollector {
    
    /**
     * Set up an empty jobs.list file on startup
     */
    private static jobFile = new File('/tmp/jobs.list')
    static {
        try {
            
            // This will only compile in the jenkins groovy environment as we do not provide the jenkins libs!
            hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
            if ( !workspace.isRemote() ) {
                jobFile = new File( workspace.getRemote(), "jobs.list" )
            }
        } catch( Exception e ){}
    }
    
    /**
     * Add a job to the list
     * @param name - name of the job
     * @param priority - Type/priority
     */
    public static addJob( String name, JobType priority ) {
        
        HashMap jobList = getJobList();
        List<String> jobs = jobList.get( priority.toString() )
        if ( !jobs ) {
            jobs = new ArrayList()
        }
        
        jobs.add( name )
        jobList.put( priority.toString(), jobs )

        writeJobList(jobList);
    }
    
    /**
     * Read the job list or create a new one
     * Will crash if file errors appear. We do not catch them
     * because we do not have a PrintStream.
     * @retuns List of Job in Map
     */
    public static HashMap getJobList() {
        
        HashMap map = new HashMap();
        try {
            FileInputStream fis = new FileInputStream(jobFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch ( IOException e ) {}
        return map
    }
    
    /**
     * Write the job list or create a new one
     * Will crash if file errors appear. We do not catch them
     * because we do not have a PrintStream.
     * @param jobList - List of Job in Map
     */
    private static writeJobList(HashMap jobList) {
        FileOutputStream fos = new FileOutputStream(jobFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(jobList);
        oos.close();
        fos.close();
    }
}