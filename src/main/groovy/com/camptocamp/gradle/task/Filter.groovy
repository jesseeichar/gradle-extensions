package com.camptocamp.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.AbstractCopyTask;

import java.io.File

class WebappFilter extends AbstractCopyTask {
    
    def File toDir
    def File sourceDir
    def String[] includes
    def String[] excludes
    
    def String beginToken = "@"
    def String endToken = "@"
    
    def filterFiles
    @TaskAction
    def filter() {
        project.ant.copy (todir: "$buildDir/webappSource") {
            fileset(dir: "src/main/filtered-webapp")
            filterset (begintoken: '@', endtoken: '@'){
                filtersfile(file: "${project(':proxy:config').projectDir}/filters/global-resource.filter")
            }
        }
    }
}