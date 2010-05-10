package c2c.convention;

import org.gradle.api.*;

class Geotools {
    def transitive = true
    def version = "2.6.1"
    def modules = ["epsg-hsql", "shapefile", "render"]
    def otherDeps = []
    def repositories = ["ibiblio":"http://www.ibiblio.org/maven2", 
                        "osgeo":"http://download.osgeo.org/webdav/geotools"]
    def excludes = [:]
    
    def dependencyString(module) {return "org.geotools:gt-$module:$version"}
}