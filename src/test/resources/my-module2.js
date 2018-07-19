if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'my-module2'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'my-module2'.");
}
if (typeof this['my-module1'] === 'undefined') {
  throw new Error("Error loading module 'my-module2'. Its dependency 'my-module1' was not found. Please, check whether 'my-module1' is loaded prior to 'my-module2'.");
}
this['my-module2'] = function (_, Kotlin, $module$my_lib1) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package$plain_demo = _.plain_demo || (_.plain_demo = {});
  package$plain_demo.main_kand9s$ = main;
  main([]);
  Kotlin.defineModule('my-module2', _);
  return _;
}(typeof this['my-module2'] === 'undefined' ? {} : this['my-module2'], kotlin, this['my-module1']);
