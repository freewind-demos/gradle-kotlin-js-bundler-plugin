package freewind.github.gradle.jsbundler

static String combine(List<File> files) {
    def modules = sort(files.collect { checkModule(it) })
    return modules.collect { "// ------------------- ${it.file.name} ---------------------- \n${it.file.text}" }.join("\n\n")
}

private static List<Module> sort(List<Module> modules) {
    List<Module> sorted = []
    def move = { List<Module> items ->
        sorted.addAll(items)
        modules.removeAll(items)
    }
    move(modules.findAll { !it.isKotlinModule() })
    while (!modules.isEmpty()) {
        def validModules = modules.findAll { sorted.collect { it.name }.containsAll(it.dependencies) }
        move(validModules)
    }
    return sorted
}

static Module checkModule(File file) {
    String code = file.text
    def module = new Module()
    module.name = findModuleName(code)
    module.dependencies = findDependencies(code)
    module.file = file
    return module
}

private static String findModuleName(String code) {
    def matches = code =~ /Kotlin\.defineModule\('(.+?)', _\);/
    if (matches.size() == 0) {
        return null
    } else {
        return matches[0][1]
    }
}

private static List<String> findDependencies(String code) {
    def matches = code =~ /Its dependency '(.+?)' was not found\./
    return matches.collect { it[1].toString() }
}

class Module {
    String name
    List<String> dependencies
    File file

    boolean isKotlinModule() {
        return this.name != null
    }
}