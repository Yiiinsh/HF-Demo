index_module.controller('IndexCtl', ['$rootScope', '$scope','$window', 
    function ($rootScope, $scope, $window) {
    $scope.currentUser = $rootScope.currentUser;
    init();

    function init() {
        if(!$rootScope.currentUser) {
            $window.location.href="/#/login"
        }
    }
}]);
