services.service('RecruitmentInfoService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        getBlockchainInfo: {
            method: 'GET',
            url: '/info/recruitment',
            isArray: true
        }
    });

    function getRecruitmentInfo() {
        var d = $q.defer();
        resource.getBlockchainInfo({},
        function(result) {
            d.resolve(result);
        }, function(result) {
            d.reject(result);
        });
        return d.promise;
    }

    return {
        getRecruitmentInfo: getRecruitmentInfo
    }
}]);
