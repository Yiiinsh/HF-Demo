index_module.controller('LoginCtl', ['$rootScope', '$scope', '$window', '$cookies', 'toastr', 'AppUtil', 'UserService', 
    function($rootScope, $scope, $window, $cookies, toastr, AppUtil, UserService) {
    $scope.user = {}

    $scope.login = login;

    function login() {
        UserService.login($scope.user.id, $scope.user.secret)
            .then(function(result) {
                toastr.success("Login success.")
                var currentUser = {
                    id: result.userId,
                    token: result.token
                };
                $cookies.putObject('currentUser', currentUser);
                $rootScope.currentUser = currentUser;
                $window.location.href ="/#/index";
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Login failed.");
            });
    }
}]);