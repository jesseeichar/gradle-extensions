package c2c.convention;

import org.gradle.api.*;

class Camptocamp {
    String filterResourcesIn
    String filterResourcesOut

    String filterWebappIn
    String filterWebappOut

    public Camptocamp(Project project) {
        filterResourcesIn = project.file("src/main/filtered-resources")
        filterResourcesOut = project.file("$project.buildDir/classes/main/")

        filterWebappIn = project.file("src/main/filtered-webapp")
        filterWebappOut = project.file("$project.buildDir/filtered/webapp")
    }
}
