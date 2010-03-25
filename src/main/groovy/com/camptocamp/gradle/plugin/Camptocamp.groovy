package com.camptocamp.gradle.plugin;

import org.gradle.api.*;
import org.gradle.api.plugins.*;
import com.camptocamp.gradle.task.*

class Camptocamp implements Plugin<Project> {  
    
    final String FILTER_RESOURCES_NAME = "filterResources"
    final String FILTER_RESOURCES_IN = "src/main/filtered-resources"
    final String FILTER_RESOURCES_OUT = "classes/main/"

    final String FILTER_WEBAPP_NAME = "filterWebapp"
    final String FILTER_WEBAPP_IN = "src/main/filtered-webapp"
    final String FILTER_WEBAPP_OUT = "filtered/webapp"
    
    def void use(Project project) {
        project.convention.plugins.camptocamp = new CamptocampConvention()

        configureProjectLayout(project)

        configureFiltering(project, FILTER_RESOURCES_NAME, FILTER_RESOURCES_IN, FILTER_RESOURCES_OUT)
        configureFiltering(project, FILTER_WEBAPP_NAME, FILTER_WEBAPP_IN, FILTER_WEBAPP_OUT)

        configureWarProjectLayout(project)
        configureAddSecurityProxy(project)
        configureWarPlugins(project)
    }

    def configureWarPlugins(Project project) {
         project.tasks.withType(org.gradle.api.tasks.bundling.War.class).allTasks {
             task.from pluginConvention.getWebAppDir();
         }
    }
    
    def configureProjectLayout(Project project) {
        ProjectLayout layout = project.tasks.add("layout", ProjectLayout.class)
        layout.description = "Adds files for a default Camptocamp application configuration"
    }
    
    def configureFiltering(Project project, String name, String input, String output) {
        Filtering filtering = project.tasks.add("$project.buildDir/$name", Filtering.class)
        filtering.description = "copies all the files that need to have strings updated from the "+
                                "filter files to the build dir for inclusion into the webapp"
        
        filtering.from input
        filtering.into output
        
        project.ant.delete(dir: "$project.buildDir/filtered")
        
        if(System.getProperty("server") == null) {
            project.logger.info("system property 'server' is not defined so defaulting to 'local'")
        }
        def server = System.getProperty("server") ?: "local"
        
        project.fileTree {
            from 'filters'
            include "$server/*.filter"
        }.each {
            filtering.filterFile(it)
        }
        
        def global = project.file("filters/global-resource.filter") 
        if (global.exists()) {
            filtering.filterFile(global)
        } else {
            project.logger.info("$global does not exist. Verify this is not an error")
        }
        
    }
    
    def configureWarProjectLayout(Project project) {
        
    }
    
    def configureAddSecurityProxy(Project project) {
        ProjectLayout proxy = project.tasks.add("addSecurityProxy", AddSecurityProxy.class)
        proxy.description = "adds the proxy submodules to a proxy directory"
        
    }
}
