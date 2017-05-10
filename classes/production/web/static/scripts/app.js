var services = angular.module('services', ['ngResource']);

var appUtil = angular.module('utils', ['toastr']);

var directive_module = angular.module('directive', ['toastr']);

var index_module = angular.module('index', ['services', 'ui.router', 'ui.bootstrap', 'toastr', 'utils','ngTable', 'directive', 'ngMaterial', 'ngCookies']);

