import com.inet.dsl.*

    // Create multiple GradleJobBuilder for more jobs.
    
    new GradleJobBuilder(
        name: 'Build Demo 1',
        demo: [ 'docker-boxes-demo' ],
        demo1: [ 'docker-boxes-demo' ],
        // rootBuildScriptDir: '${workspace}/docker-boxes-demo',    // This folder has the gradle wrapper
        // buildFile: 'docker-boxes-demo/build.gradle',             // This is the actual build script
        // useWrapper: true,                                        // We need the wrapper, it uses fakeroot for SYSTEM access
        priority: JobType.LOWERBASE,
    ).build(this)

    new GradleJobBuilder(
        name: 'Build Demo Installer',
        demo: [ 'docker-boxes-demo' ],
        priority: JobType.UPPERBASE,
    ).build(this)

    new GradleJobBuilder(
        name: 'Build Demo Plugin',
        demo: [ 'docker-boxes-demo' ],
        priority: JobType.SETUP,
    ).build(this)
