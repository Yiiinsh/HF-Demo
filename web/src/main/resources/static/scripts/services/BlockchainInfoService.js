services.service('BlockchainInfoService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        getBlockchainInfo: {
            method: 'GET',
            url: '/info/block',
            isArray: true
        }
    });

    function getBlockchainInfo() {
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
        getBlockchainInfo: getBlockchainInfo
    }
}]);
