import jenkins.model.*

def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.plugins.disk_usage.DiskUsageProjectActionFactory")
desc?desc.setShowGraph(true):null
