package com.inet.dsl

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.views.ListView

/**
 * Creating a List View
 * Usage:
 *          new ViewBuilder(
 *              name: 'NAME'
 *              regex: '.*'
 *          ).build(this)
 */
class ViewBuilder {

    String name                                                 // Name of the Build Job in Jenkins
    String regex                                                // Regular expression for job filter

    /**
     * Builder for the view. Usage see above.
     * @param dslFactory - context we are running in
     */
    ListView build(DslFactory dslFactory) {
        dslFactory.listView(name) {
            filterBuildQueue()
            filterExecutors()
            jobs {
                regex(this.regex)
            }
            columns {
                status()
                weather()
                buildButton()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
            }
        }
    }
}
