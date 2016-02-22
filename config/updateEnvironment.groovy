import hudson.model.*
    
def env = System.getenv()
System.setProperty("BRANCH", env['BRANCH'])
