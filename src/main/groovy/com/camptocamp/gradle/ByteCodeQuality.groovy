package com.camptocamp.gradle;

import org.gradle.api.*;
import org.gradle.api.plugins.*;

class ByteCodeQuality implements Plugin {  
    String version = "1.3.9"
  
    def void use(Project project, org.gradle.api.plugins.ProjectPluginsContainer container) {
        project.configurations {
            findbugsConf
        }

        project.dependencies {
            findbugsConf "com.google.code.findbugs:findbugs:$version", "com.google.code.findbugs:findbugs-ant:$version"
        }

        project.task("findbugs", dependsOn: project.jar) << {
            gradleUserHomeDir = project.gradle.gradleUserHomeDir
            ant = project.ant
            home = new File(gradleUserHomeDir,"findbugs")

            libs = new File(home, "lib")
            findBugsJar = new File(libs,"findbugs.jar")
            if(!findBugsJar.exists()) {
                archive = "findbugs-${version}.zip"
                ant.get(src: "http://downloads.sourceforge.net/project/findbugs/findbugs/1.3.9/$archive?use_mirror=switch",
                        dest: "$gradleUserHomeDir/$archive", verbose: "on", usetimestamp: "on")
                ant.unzip(src: "$gradleUserHomeDir/$archive",
                          dest: "$gradleUserHomeDir")
                ant.move(file: "$gradleUserHomeDir/findbugs-$version",
                         tofile: "$gradleUserHomeDir/findbugs")
                ant.delete(file: "$gradleUserHomeDir/$archive")
            }

            result = ant {
                taskdef(name:'findbugs', classname:'edu.umd.cs.findbugs.anttask.FindBugsTask', 
                        classpath: project.configurations.findbugsConf.asPath)

                findbugs(home: home.path, output:'html', outputFile: "$project.buildDir/findbugs.html") {
                    sourcePath(path: sourceSets.main.java)
                    "class"(location: sourceSets.main.classes.asPath)
                }
            }
            
            println(result)
        }   
    }
}
