package freewind.github.gradle.jsbundler

import org.gradle.api.Plugin
import org.gradle.api.Project

class Extension {
    File outputBundleFile
}

class KotlinJsBundlerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply { plugin: 'kotlin2js' }

        Extension extension = project.extensions.create('kotlinJsBundler', Extension)

        project.task('extractDependentJsFiles') {
            dependsOn 'classes'
            doLast {
                project.sync {
                    configurations.compile.each { File file ->
                        from(zipTree(file.absolutePath), {
                            includeEmptyDirs = false
                            include { fileTreeElement ->
                                def path = fileTreeElement.path
                                isAcceptedJsFile(path)
                            }
                        })
                    }
                    into dirOfDependentJsFiles(project)
                }
            }
        }

        project.task('kotlinJsBundler') {
            dependsOn 'extractDependentJsFiles'
            doLast {
                def dependentJsFiles = dirOfDependentJsFiles(project).listFiles()
                def outputJsFile = project.tasks.getByName('compileKotlin2Js').outputs.files.findAll { it.isFile() && isAcceptedJsFile(it.name) }
                String bundleCode = KotlinJsBundler.combine(dependentJsFiles.toList() + outputJsFile)
                extension.outputBundleFile.write(bundleCode)
                println("write bundle file: ${extension.outputBundleFile}")
            }
        }

    }

    private static boolean isAcceptedJsFile(path) {
        path.endsWith(".js") && !path.endsWith(".meta.js")
    }

    private static File dirOfDependentJsFiles(Project project) {
        return new File("${project.buildDir}/dependentJsFiles")
    }
}
