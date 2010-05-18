// Runs cobertura to analyze the code coverage of the project.  This is a ALPHA version
package c2c.plugin;

import org.gradle.api.*;
import org.gradle.api.tasks.testing.Test;

class Cobertura implements Plugin<Project> {  

    def void apply(Project project) {
        def cobSerFile="${project.buildDir}/cobertura.ser"
        def srcOriginal="${project.sourceSets.main.classesDir}"
        def srcCopy="${srcOriginal}-copy"
        def version = '1.9.3'
        
        def ant = project.ant
        
        project.configurations {
            coberturaConf
        }

        project.dependencies {
            coberturaConf "net.sourceforge.cobertura:cobertura:$version"
            testRuntime "net.sourceforge.cobertura:cobertura:$version"
        }
        project.tasks.add(name:"coverage", type: Test) {
            systemProperties['net.sourceforge.cobertura.datafile']="${cobSerFile}"
            testClassesDir = project.test.testClassesDir
            classpath = project.test.classpath
            testSrcDirs = project.test.testSrcDirs
        }

        project.coverage.doFirst  {            
            // delete data file for cobertura, otherwise coverage would be added
            ant.delete(file:cobSerFile, failonerror:false)
            // delete copy of original classes
            ant.delete(dir: srcCopy, failonerror:false)
            // import cobertura task, so it is available in the script
            ant.taskdef(resource:'tasks.properties', classpath: project.configurations.coberturaConf.asPath)
            // create copy (backup) of original class files
            ant.copy(todir: srcCopy) {
                fileset(dir: srcOriginal)
            }
            
                // instrument the relevant classes in-place
            ant.'cobertura-instrument'(datafile:cobSerFile) {
                    fileset(dir: srcOriginal,
                           includes:"**/*.class",
                           excludes:"**/*Test.class")
            }
            
        }


        project.coverage.doLast {
            if (new File(srcCopy).exists()) {
                // replace instrumented classes with backup copy again
                ant.delete(file: srcOriginal)
                ant.move(file: srcCopy,
                         tofile: srcOriginal)
                // create cobertura reports
                ant.'cobertura-report'(destdir:"$project.buildDir/reports/test-coverage",
                     format:'html', datafile:cobSerFile) {
                         if( new File("src/main/java").exists()) fileset(dir: "src/main/java")
                         if( new File("src/main/scala").exists()) fileset(dir: "src/main/scala")
                         if( new File("src/main/groovy").exists()) fileset(dir: "src/main/groovy")
                     }
            }
        }
    }
}