// Plugin containing geotools dependencies.  See the conventions file for configuration options
package c2c.plugin

import org.gradle.api.*

class Scala implements Plugin<Project> {
    def scalaVersion = '2.8.0.RC1'
//    List<String> scalaTestLibs = []
    String scalaLibrary() {return "org.scala-lang:scala-library:$scalaVersion"}
    String scalaCompiler() {return "org.scala-lang:scala-compiler:$scalaVersion"}
    
    public void apply(Project project) {
        project.apply plugin:'scala'
        project.repositories.mavenRepo name: 'scala-repo', urls: 'http://scala-tools.org/repo-releases/'
        project.dependencies {
            scalaTools scalaCompiler()
            scalaTools scalaLibrary()
            
            compile scalaLibrary()
//            test scalaTestLibs
        }
    }
}