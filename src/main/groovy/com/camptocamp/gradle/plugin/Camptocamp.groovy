package com.camptocamp.gradle.plugin;

import org.gradle.api.*;
import org.gradle.api.plugins.*;
import com.camptocamp.gradle.task.*

class Camptocamp implements Plugin<Project> {  
  
    def void use(Project project) {
        project.convention.plugins.camptocamp = new CamptocampConvention()
        
        configureProjectLayout(project)
        configureFiltering(project)
        configureWarProjectLayout(project)
        configureAddSecurityProxy(project)
    }
    
    def configureProjectLayout(Project project) {
        ProjectLayout layout = project.tasks.add("layout", ProjectLayout.class)
        layout.description = "Adds files for a default Camptocamp application configuration"
    }
    
    def configureFiltering(Project project) {
        Filtering filtering = project.tasks.add("filtering", Filtering.class)
        filtering.description = "copies all the files that need to have strings updated from the "+
                                "filter files to the build dir for inclusion into the webapp"
        
    }
    
    def configureWarProjectLayout(Project project) {
        
    }
    
    def configureAddSecurityProxy(Project project) {
        ProjectLayout proxy = project.tasks.add("addSecurityProxy", AddSecurityProxy.class)
        proxy.description = "adds the proxy submodules to a proxy directory"
        
    }
}
