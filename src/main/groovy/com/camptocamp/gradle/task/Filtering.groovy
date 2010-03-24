package com.camptocamp.gradle.task;

import org.apache.tools.ant.filters.ReplaceTokens

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Copy;

import java.io.File

class Filtering extends Copy {
    
    def String beginToken = "@"
    def String endToken = "@"
    def Iterable filterFiles = []
    
    @InputFiles
     def Iterable getFilterFiles() {
         return filterFiles
     }

    def filterFile(File f) {
        filterFiles += f
        
        def properties = new Properties()
        f.withReader { properties.load(it) }
        
        filter(ReplaceTokens, tokens: properties)
    }
    
}