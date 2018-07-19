package freewind.github.gradle.jsbundler

import org.gradle.api.Plugin
import org.gradle.api.Project

class Extension {
    File sourceDir
    File targetFile
}

class KotlinJsBundlerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create('kotlinJsBundler', Extension)
        project.task('kotlinJsBundler') {
            doLast {
                String bundleCode = KotlinJsBundler.combine(extension.sourceDir.listFiles().toList())
                extension.targetFile.write(bundleCode)
                println("write bundle file: ${extension.targetFile}")
            }
        }
    }
    
}
