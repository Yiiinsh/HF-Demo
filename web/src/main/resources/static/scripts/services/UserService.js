services.service('UserService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        login: {
            method: 'POST',
            url: '/user/login'
        },

        get_current_user: {
            method: 'GET',
            url: '/console/user/current'
        },
        logout: {
            method: 'GET',
            url: '/console/user/logout'
        }
    });

    function getCurrentUser() {
        var d = $q.defer();
        resource.get_current_user({},
                                  function (result) {
                                      d.resolve(result);
                                  }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function logout() {
        var d = $q.defer();
        resource.logout({},
                        function (result) {
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
        getCurrentUser: getCurrentUser,
        logout: logout
    }
}]);
