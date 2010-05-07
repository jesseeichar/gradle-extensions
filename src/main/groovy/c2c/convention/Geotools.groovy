package c2c.convention;

import org.gradle.api.*;

class Geotools implements Plugin<Project> {
    
    String version = "2.6.1"
    def modules = ["epsg-hsql", "shapefile", ]
// TODO    def excludes = [:]
}