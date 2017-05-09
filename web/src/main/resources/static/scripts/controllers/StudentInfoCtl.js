index_module.controller('StudentInfoCtl', ['$rootScope', '$scope','$window', 'toastr', 'AppUtil', 'StudentInfoService',
    function ($rootScope, $scope, $window, toastr, AppUtil, StudentInfoService) {
    
    $scope.currentUser = $rootScope.currentUser;

    init();

    function init() {
        StudentInfoService.getStudentInfo($scope.currentUser.id, $scope.currentUser.token)
            .then(function(result) {
                $scope.studentInfo = result;
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Fail to get student info.");
            });
    }
}]);
