index_module.controller('RecruitmentInfoCtl', ['$rootScope', '$scope','$window', '$cookies', 'toastr', 'AppUtil', 'RecruitmentInfoService',
    function ($rootScope, $scope, $window, $cookies, toastr, AppUtil, RecruitmentInfoService) {

        $rootScope.currentUser = $cookies.getObject('currentUser');
        $scope.currentUser = $cookies.getObject('currentUser');
        $scope.toShow = {}

        $scope.showRecruitmentDescription = showRecruitmentDescription

        init();

        function init() {
            $scope.currentUser = $cookies.getObject('currentUser');
            RecruitmentInfoService.getRecruitmentInfo()
            .then(function(result) {
                $scope.recruitmentInfo = result;
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Fail to get blockchain info.");
            });
        }

        function showRecruitmentDescription(info) {
            $scope.toShow = info.description;
            $('#descriptionModal').modal('show');
        } 
}]);
