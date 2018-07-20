package freewind.github.gradle.jsbundler

import org.gradle.api.Plugin
import org.gradle.api.Project

class Extension {
    List<String> additionalJsFiles = []
    String outputFile
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
                    project.configurations.compile.each { File file ->
                        from(project.zipTree(file.absolutePath), {
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
                if (extension.outputFile == null) {
                    throw new IllegalArgumentException('kotlinJsBundler.outputFile should not be null')
                }
                def dependentJsFiles = dirOfDependentJsFiles(project).listFiles()
                def outputJsFile = project.tasks.getByName('compileKotlin2Js').outputs.files.findAll { it.isFile() && isAcceptedJsFile(it.name) }
                def allJsFiles = dependentJsFiles.toList() + outputJsFile + extension.additionalJsFiles.collect { new File(it) }
                String bundleCode = KotlinJsBundler.combine(allJsFiles)
                new File(extension.outputFile).write(bundleCode)
                println("write bundle file: ${extension.outputFile}")
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
