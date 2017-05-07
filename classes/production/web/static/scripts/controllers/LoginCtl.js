index_module.controller('LoginCtl', ['$rootScope', '$scope', '$window', 'toastr', 'AppUtil', 'UserService', 
    function($rootScope, $scope, $window, toastr, AppUtil, UserService) {
    $scope.user = {}

    $scope.login = login;

    function login() {
        UserService.login($scope.user.id, $scope.user.secret)
            .then(function(result) {
                toastr.success("Login success.")
                $rootScope.currentUser = {
                    id: result.userId,
                    token: result.token
                };
                $window.location.href ="/#/index";
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Login failed.");
            });
    }
}]);