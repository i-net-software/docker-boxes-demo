package com.inet.dsl

/**
 * Enum for the AllJobsCollector
 * Each job type is a different phase in which the job will run
 */
enum JobType {
    
    LOWERBASE(1),
    UPPERBASE(2),
    
    PLUGIN(3),
    PRODUCT(4),
        
    SETUP(5),
    INSTALLER(6),
    SDK(7)
  
    JobType(int value) {this.value = value}
    private final int value
    public int value() {return value}
}