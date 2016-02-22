import com.inet.dsl.*
    
    new ViewBuilder(
        name: "All",
        regex: ".*",
    ).build(this)
    
    new ViewBuilder(
        name: "Plugins",
        regex: ".*Plugin",
    ).build(this)
    
    new ViewBuilder(
        name: "Installer",
        regex: ".*Installer",
    ).build(this)

    buildPipelineView( 'Build All Pipeline' ) {
        selectedJob('___ Build All ___')
    }