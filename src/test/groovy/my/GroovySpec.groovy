package my

import org.junit.Test

import static org.fest.assertions.api.Assertions.assertThat

class KotlinJsBundlerSpec {

    @Test
    void testKotlinJs() {
        def module = KotlinJsBundler.checkModule(new File("./src/test/resources/kotlin.js"))
        assertThat(module.file).isEqualTo(new File("./src/test/resources/kotlin.js"))
        assertThat(module.name).isEqualTo("kotlin")
        assertThat(module.dependencies).isEmpty()
    }

    @Test
    void testMyModule1() {
        def module = KotlinJsBundler.checkModule(new File("./src/test/resources/my-module1.js"))
        assertThat(module.file).isEqualTo(new File("./src/test/resources/my-module1.js"))
        assertThat(module.name).isEqualTo("my-module1")
        assertThat(module.dependencies).containsExactly("kotlin")
    }

    @Test
    void testMyModule2() {
        def module = KotlinJsBundler.checkModule(new File("./src/test/resources/my-module2.js"))
        assertThat(module.file).isEqualTo(new File("./src/test/resources/my-module2.js"))
        assertThat(module.name).isEqualTo("my-module2")
        assertThat(module.dependencies).containsExactly("kotlin", "my-module1")
    }

    @Test
    void testNonKotlinJsModule() {
        def module = KotlinJsBundler.checkModule(new File("./src/test/resources/non-kotlin-module.js"))
        assertThat(module.file).isEqualTo(new File("./src/test/resources/non-kotlin-module.js"))
        assertThat(module.name).isNull()
        assertThat(module.dependencies).isEmpty()
        assertThat(module.isKotlinModule()).isFalse()
    }

    @Test
    void testCombine() {
        def bundle = KotlinJsBundler.combine([
                new File("./src/test/resources/my-module2.js"),
                new File("./src/test/resources/my-module1.js"),
                new File("./src/test/resources/kotlin.js"),
                new File("./src/test/resources/non-kotlin-module.js")
        ])
        println bundle
        assertThat(bundle.trim()).isEqualTo("""
// ------------------- non-kotlin-module.js ---------------------- 
console.log("I'm not kotlin js module");

// ------------------- kotlin.js ---------------------- 
this['kotlin'] = function (_, Kotlin) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package\$plain_demo = _.plain_demo || (_.plain_demo = {});
  package\$plain_demo.main_kand9s\$ = main;
  main([]);
  Kotlin.defineModule('kotlin', _);
  return _;
}(typeof this['kotlin'] === 'undefined' ? {} : this['kotlin'], kotlin);


// ------------------- my-module1.js ---------------------- 
if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'my-module1'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'my-module1'.");
}
this['my-module1'] = function (_, Kotlin) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package\$plain_demo = _.plain_demo || (_.plain_demo = {});
  package\$plain_demo.main_kand9s\$ = main;
  main([]);
  Kotlin.defineModule('my-module1', _);
  return _;
}(typeof this['my-module1'] === 'undefined' ? {} : this['my-module1'], kotlin);


// ------------------- my-module2.js ---------------------- 
if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'my-module2'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'my-module2'.");
}
if (typeof this['my-module1'] === 'undefined') {
  throw new Error("Error loading module 'my-module2'. Its dependency 'my-module1' was not found. Please, check whether 'my-module1' is loaded prior to 'my-module2'.");
}
this['my-module2'] = function (_, Kotlin, \$module\$my_lib1) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package\$plain_demo = _.plain_demo || (_.plain_demo = {});
  package\$plain_demo.main_kand9s\$ = main;
  main([]);
  Kotlin.defineModule('my-module2', _);
  return _;
}(typeof this['my-module2'] === 'undefined' ? {} : this['my-module2'], kotlin, this['my-module1']);
""".trim())
    }

}
