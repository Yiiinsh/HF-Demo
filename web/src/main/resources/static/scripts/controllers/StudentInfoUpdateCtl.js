index_module.controller('StudentInfoUpdateCtl', ['$rootScope', '$scope','$window', 'toastr', 'AppUtil', 'StudentInfoService',
    function ($rootScope, $scope, $window, toastr, AppUtil, StudentInfoService) {

        $scope.currentUser = $rootScope.currentUser;
        $scope.newInternInfo = {}

        $scope.update = update;
        $scope.preAddInternInfo = preAddInternInfo;
        $scope.addInternInfo = addInternInfo;
        $scope.remove = remove;

        init();

        function init() {
            StudentInfoService.getStudentInfo($scope.currentUser.id, $scope.currentUser.token)
                .then(function(result) {
                    $scope.studentInfo = result;
                }, function(result) {
                    toastr.error(AppUtil.errorMsg(result), "Fail to get student info.");
                });
        }
        
        function update() {
            StudentInfoService.updateStudentInfo($scope.currentUser.id, $scope.currentUser.token, $scope.studentInfo)
                .then(function (result) {
                    toastr.success("Update success.");
                    $window.location.href ="/#/student/info";
                }, function (result) {
                    toastr.error(AppUtil.errorMsg(result), "Fail to update student info.");
                });
        }
        
        function preAddInternInfo() {
            $('#createInternInfoModal').modal('show');
        }

        function addInternInfo() {
            $scope.newInternInfo.studentId = $scope.studentInfo.id;
            $scope.newInternInfo.name = $scope.studentInfo.name;

            if($scope.newInternInfo.workingId && $scope.newInternInfo.company && $scope.newInternInfo.department
                && $scope.newInternInfo.position && $scope.newInternInfo.start && $scope.newInternInfo.end) {
                $scope.studentInfo.internInfos.push($scope.newInternInfo);
            } else {
                toastr.error("Please fill in all the blank");
            }
            $scope.newInternInfo = {};
            $('#createInternInfoModal').modal('hide');
        }

        function remove(intern) {
            var idx = $scope.studentInfo.internInfos.indexOf(intern);
            if (idx > -1) {
                $scope.studentInfo.internInfos.splice(idx, 1);
            }
        }

    }]);
