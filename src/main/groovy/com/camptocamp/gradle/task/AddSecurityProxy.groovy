package com.camptocamp.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

class AddSecurityProxy extends ProjectLayout {
    
    def proxyDir = System.getProperty('proxyDir') ?: 'security-proxy'
    
    def AddSecurityProxy() {
        // this is to layout the config with the standard options
        // see ProjectLayout properties
        baseDir = proxyDir+'/config'
        directories = [ ]
    }

    def basicLayout() {
        
        new File(proxyDir).mkdirs()

        def settingsFile = new File("$project.projectDir/settings.gradle")
        settingsFile.append(settings)

        
        def url = 'git@github.com:jesseeichar/security-proxy.git'
        url = "/Users/jeichar/Local_Projects/security-proxy"
        project.logger.quiet("cloning project: $url")
        project.ant.exec(executable: 'git', failonerror: true) {
            arg(value: 'clone')
            arg(value: url)
            arg(value: "$proxyDir")
        }
        project.ant.delete(dir: "$proxyDir/proxy/config/.git")                
        project.ant.delete(dir: "$proxyDir/.git")                
        
        super.basicLayout()
    }
}