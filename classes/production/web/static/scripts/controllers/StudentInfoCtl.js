index_module.controller('StudentInfoCtl', ['$rootScope', '$scope','$window', '$interval', 'toastr', 'AppUtil', 'StudentInfoService',
    function ($rootScope, $scope, $window, $interval, toastr, AppUtil, StudentInfoService) {
    
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

    var cnt = 0;
    $scope.refreshStudentInfo = $interval(function() {
        init();
        ++cnt;
        if(cnt == 4) {
            $interval.cancel($scope.refreshStudentInfo);
        }
    } , 2000);
    // $scope.$on('$destroy', function() {
    //     $interval.cancel($scope.refreshStudentInfo);
    // });
}]);
