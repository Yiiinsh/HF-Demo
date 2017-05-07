services.service('UserService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        login: {
            method: 'POST',
            url: '/user/login'
        },
        logout: {
            method: 'POST',
            url: '/user/logout'
        }
    });

    function logout(userId, token) {
        var d = $q.defer();
        resource.logout({},{
            userId: userId,
            token: token
        }, function (result) {
           d.resolve(result);
           }, function (result) {
            d.reject(result);
        });
        return d.promise;
    }

    function login(userId, userSecret) {
        var d = $q.defer();
        resource.login({},{
            userId: userId,
            userSecret: userSecret
        }, function(result) {
            d.resolve(result);
        }, function(result) {
            d.reject(result);
        });
        return d.promise;
    }

    return {
        login: login,
        logout: logout
    }
}]);
