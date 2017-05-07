index_module.controller('StudentInfoCtl', ['$rootScope', '$scope','$window', 'toastr', 'AppUtil', 
    function ($rootScope, $scope, $window, toastr, AppUtil) {
    
    $scope.currentUser = $rootScope.currentUser;
}]);
