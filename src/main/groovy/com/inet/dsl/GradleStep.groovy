package com.inet.dsl

/**
 * Singular gradle build step.
 * We need to have multiple gradle steps in a job.
 */
class GradleStep {
    String rootBuildScriptDir                                   // Root-Build Dir for the gradle step. Will be generated if needed
    String buildFile                                            // Build file for the gradle task. Will override the rootBuildScriptDir
    String tasks = ':uploadArchives :check'                     // gradle task to run
    Boolean useWrapper = false                                  // whether to use the gradlew in the project or not
    
    HashMap<String, Object> step () {
        return  [
                    rootBuildScriptDir: this.rootBuildScriptDir,
                    buildFile: this.buildFile,
                    tasks: this.tasks,
                    useWrapper: this.useWrapper,
                ]
    }
}