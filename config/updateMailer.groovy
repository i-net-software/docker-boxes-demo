import jenkins.model.*

def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.tasks.Mailer")

// desc.setSmtpHost("<HOST>")
// desc.setUseSsl(false)
// desc.setCharset("UTF-8")

desc.save()
inst.save()