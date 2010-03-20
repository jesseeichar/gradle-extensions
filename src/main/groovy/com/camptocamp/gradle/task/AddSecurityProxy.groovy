package com.camptocamp.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

class AddSecurityProxy extends ProjectLayout {
    private def repos = [
        core: 'git@github.com:jesseeichar/proxy-core.git',
        config: 'git@github.com:jesseeichar/proxy-config.git',
        cas: 'git@github.com:jesseeichar/cas-server-webapp.git',
        ldap: 'git@github.com:jesseeichar/embedded-ldap.git',
        scripts: 'git@github.com:jesseeichar/jvm-security-scripts.git'
    ]
    
    def proxyDir = 'proxy'
    
    def AddSecurityProxy() {
        // this is to layout the config with the standard options
        baseDir = proxyDir+'/config'
        directories = [ ]
    }

    def basicLayout() {
        
        new File(proxyDir).mkdirs()
        
        def sepStart = "\n// ---------  Added by AddSecurityProxy task  --------- //\n"
        def sepEnd =   "\n// ---------  ------------------------------  ---------- //\n"

        def settingsFile = new File("$project.projectDir/settings.gradle")
        def settings = ""
        
        if(settingsFile.exists()) {
            settings = settingsFile.text + sepStart
        } else {
            settings = sepStart
        }
        settings += 'include '
        def addComma = false
        
        repos.each { repo -> 
            def name = repo.key
            def url = repo.value
            
            def dir = "$proxyDir/$name"
            if (new File(dir).exists() && new File(dir).listFiles().length > 0) {
                throw new AssertionError("dir exists and is not empty")
            }
            
            if(name != 'config') {
                project.logger.quiet("adding submodule: $url $dir")
                project.ant.exec(executable: 'git', failonerror: true) {
                    arg(value: 'submodule')
                    arg(value: 'add')
                    arg(value: url)
                    arg(value: "$dir")
                }
            } else {
                project.logger.quiet("cloning project: $url")
                project.ant.exec(executable: 'git', failonerror: true) {
                    arg(value: 'clone')
                    arg(value: url)
                    arg(value: "$dir")
                }
                project.ant.delete(dir: "$proxyDir/config/.git")                
            }
            if(addComma) settings += ','
            settings += " '${dir.replaceAll('/',':')}' "
            addComma = true
        }
                
        settings += "$sepEnd"
        
        settingsFile.write(settings)
        
        
        def build = new URL("http://github.com/jesseeichar/security-proxy/raw/master/build.gradle").text
        
        new File("$project.projectDir/build.gradle").append(sepStart + build + sepEnd)
        super.basicLayout()
    }
}