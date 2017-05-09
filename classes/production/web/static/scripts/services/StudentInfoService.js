services.service('StudentInfoService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        getStudentInfo: {
            method: 'POST',
            url: '/info/student/get'
        }
    });

    function getStudentInfo(userId, token) {
        var d = $q.defer();
        resource.getStudentInfo({},{
            userId: userId,
            token: token
        }, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });
        return d.promise;
    }

    return {
        getStudentInfo: getStudentInfo
    }
}]);
