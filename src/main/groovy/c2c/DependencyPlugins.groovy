// Plugin containing geotools dependencies.  See the conventions file for configuration options
package c2c.plugin

import org.gradle.api.*

class DependencyPlugins {
    
    static def apply(project, convention, pluginName) {
        convention.repositories.each { project.repositories.mavenRepo (name: it.key, urls: it.value)}

        if(!project.configurations.properties.containsKey ("compile")){
            throw new AssertionError("The '$pluginName' plugin can only be added to a project that already has a compile configuration.  \nOne solution is to add: \n\tapply plugin: 'java'\n *before* applying the '$pluginName' plugin ")
        }
        convention.modules.each {
            project.dependencies.compile (convention.dependencyString(it)){
                transitive=convention.transitive
                if (convention.excludes.containsKey (it)){
                    exclude convention.excludes.get (it)
                }
            }
        }
        convention.otherDeps.each {
            project.dependencies.compile (it){
                transitive=convention.transitive
                if (convention.excludes.containsKey (it)){
                    exclude convention.excludes.get (it)
                }
            }
        }
    }
}