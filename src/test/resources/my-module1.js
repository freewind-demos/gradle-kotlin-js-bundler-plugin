if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'my-module1'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'my-module1'.");
}
this['my-module1'] = function (_, Kotlin) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package$plain_demo = _.plain_demo || (_.plain_demo = {});
  package$plain_demo.main_kand9s$ = main;
  main([]);
  Kotlin.defineModule('my-module1', _);
  return _;
}(typeof this['my-module1'] === 'undefined' ? {} : this['my-module1'], kotlin);
