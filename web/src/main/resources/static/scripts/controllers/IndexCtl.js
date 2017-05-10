index_module.controller('IndexCtl', ['$rootScope', '$scope','$window', '$cookies', 'toastr', 'AppUtil', 'UserService',
    function ($rootScope, $scope, $window, $cookies, toastr, AppUtil, UserService) {

    $rootScope.currentUser = $cookies.getObject('currentUser');
    $scope.currentUser = $cookies.getObject('currentUser');
    $rootScope.logout = logout;
    
    init();

    function init() {
        $scope.currentUser = $cookies.getObject('currentUser');
        if(!$scope.currentUser) {
            $window.location.href="/#/login"
        }
    }

    function logout() {
        UserService.logout($scope.currentUser.id, $scope.currentUser.token)
            .then(function(result) {
                toastr.success("Logout success.");
                $cookies.remove('currentUser');
                delete $rootScope.currentUser;
                $window.location.href ="/#/login";
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Logout failed.");
            });
    }
}]);
