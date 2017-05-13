index_module.controller('BlockchainInfoCtl', ['$rootScope', '$scope','$window', '$cookies', 'toastr', 'AppUtil', 'BlockchainInfoService',
    function ($rootScope, $scope, $window, $cookies, toastr, AppUtil, BlockchainInfoService) {

        $rootScope.currentUser = $cookies.getObject('currentUser');
        $scope.currentUser = $cookies.getObject('currentUser');
        $scope.toShow = {}

        $scope.showBlockData = showBlockData;

        init();

        function init() {
            $scope.currentUser = $cookies.getObject('currentUser');
            BlockchainInfoService.getBlockchainInfo()
            .then(function(result) {
                $scope.blockchainInfo = result;
            }, function(result) {
                toastr.error(AppUtil.errorMsg(result), "Fail to get blockchain info.");
            });
        }

        function showBlockData(data) {
            $scope.toShow = data.data;
            $('#blockDataModal').modal('show');
        } 
}]);
