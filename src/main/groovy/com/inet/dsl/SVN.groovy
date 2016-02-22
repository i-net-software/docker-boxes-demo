package com.inet.dsl

import javaposse.jobdsl.dsl.helpers.scm.SvnCheckoutStrategy

/**
 * SVN provider
 * Usage:
 *  multiscm {
 *          SVN.helpdesk( delegate, 'HelpDeskClient')
 *          SVN.shared( delegate, 'InetServerCore')
 *  }
 *
 * Attention: do not forget to import com.inet.dsl.*
 */ 
class SVN {
    
    public static BOXES_DEMO = "https://github.com/i-net-software/"
    public static BRANCH     = System.getProperty('BRANCH')?:"trunk"
    
    /**
     * Basic checkout.
     * @param context - delegate from scm
     * @param svnLocation - svn://-URL
     * @param svnDirectory - where to put the checkout artefact
     */
    static void checkout(context, String svnLocation, String svnDirectory) {
        context.with {
            svn {
                location( svnLocation ) {
                    // Credentials ID used in Jenkins. See /config/updateCredentials.groovy
                    // credentials( "..." )
                    checkoutStrategy( SvnCheckoutStrategy.UPDATE_WITH_CLEAN )
                    if ( svnDirectory ) {
                        directory( svnDirectory )
                    }
                }
            }
        }
    }
    
    private static String getBranch() {
        return (BRANCH=='trunk'?BRANCH:'branches/'+BRANCH) + "/"
    }
    
    /**
     * Checkout for demo projects
     * @param context - delegate from scm
     * @param project - HelpDesk Project in svn://
     */
    static void demo(context, String project) {
        // Do not use the branch typology here
        checkout( context, HELPDESK + /* getBranch() +*/ project , project)
    }
}