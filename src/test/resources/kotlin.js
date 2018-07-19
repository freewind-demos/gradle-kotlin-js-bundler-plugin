this['kotlin'] = function (_, Kotlin) {
  'use strict';
  function main(args) {
    console.log('Hello, plain_demo');
  }
  var package$plain_demo = _.plain_demo || (_.plain_demo = {});
  package$plain_demo.main_kand9s$ = main;
  main([]);
  Kotlin.defineModule('kotlin', _);
  return _;
}(typeof this['kotlin'] === 'undefined' ? {} : this['kotlin'], kotlin);
