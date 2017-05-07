index_module.controller('IndexCtl', ['$rootScope', '$scope','$window', 'toastr', 'AppUtil', 'UserService',
    function ($rootScope, $scope, $window, toastr, AppUtil, UserService) {
    
    $scope.currentUser = $rootScope.currentUser;
    
    $scope.logout = logout;
    
    init();

    function init() {
        if(!$rootScope.currentUser) {
            $window.location.href="/#/login"
        }
    }

    function logout() {
        UserService.logout($scope.currentUser.id, $scope.currentUser.token)
            .then(function(result) {
                toastr.success("Logout success.")
                $window.location.href ="/#/login";
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Logout failed.");
            });
    }
}]);
