import jenkins.model.*

def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.tasks.Shell")

desc.setShell("bash")

desc.save()
inst.save()