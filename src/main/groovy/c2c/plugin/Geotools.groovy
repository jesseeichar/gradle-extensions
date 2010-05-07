// Plugin containing geotools dependencies.  See the conventions file for configuration optionsfile.bufferedReader()
package c2c.plugin;

import org.gradle.api.*;

class Geotools implements Plugin<Project> {  
    def void apply(Project project) {
        def convention = new c2c.convention.Geotools()
        project.convention.plugins.camptocamp = convention
        
        project.configurations.geotools
        project.configurations.geotools.transitive = true
        
        project.dependencies.geotools (convention.modules.collect {"org.geotools:gt-$it:$convention.version:jar"})
        
        project.dependencies.compile << project.dependencies.geotools
    }
}