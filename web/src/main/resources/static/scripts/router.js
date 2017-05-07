index_module.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index");
    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'index.html',
            controller: 'IndexCtl',
            allowAnonymous: false
        })
        .state('login', {
            url: '/login',
            templateUrl: 'views/index/login.html',
            controller: 'LoginCtl',
            allowAnonymous: true
        })
        .state('student_info', {
            url: '/student/info',
            templateUrl: 'views/index/student_info.html',
            controller: 'StudentInfoCtl',
            allowAnonymous: false
        })
        .state('student_info_update', {
            url: '/student/info/update',
            templateUrl: 'views/index/student_info_update.html',
            controller: 'StudentInfoUpdateCtl',
            allowAnonymous: false
        })
        .state('internship_info', {
            url: '/internship/info',
            templateUrl: 'views/index/internship_info.html',
            controller: 'InternshipInfoCtl',
            allowAnonymous: false
        })
        .state('cluster_shards', {
            url: '/cluster_shards?clusterName',
            templateUrl: 'views/index/cluster_shards.html',
            controller: 'ClusterShardCtl'
        })
        .state('cluster_dc_shards', {
            url: '/cluster_dc_shards/:clusterName/:currentDcName',
            params: {
                clusterName: {
                    value: '',
                    squash: false
                },
                currentDcName: {
                    value: '',
                    squash: false
                }
            },
            templateUrl: 'views/index/cluster_dc_shards.html',
            controller: 'ClusterCtl'
        })
        .state('cluster_dc_shard_update', {
        	url: '/cluster_dc_shard_update?clusterName&shardName&currentDcName',
        	templateUrl: 'views/index/cluster_dc_shard_update.html',
        	controller: 'ClusterDcShardUpdateCtl'
        })
        .state('migration_event_details', {
        	url: '/migration_event_details/:eventId',
            params: {
                eventId: {
                    value: '',
                    squash: false
                }
            },
        	templateUrl: 'views/index/migration_details.html',
        	controller: 'ActiveDcMigrationEventDetailsCtl'
        })
        .state('migration_event_details.details', {
        	url: '/details',
            params: {
                migrationCluster: {
                    value: {},
                    squash: false
                }
            },
        	templateUrl: 'views/index/migration_details_content.html',
        	controller : 'ActiveDcMigrationEventDetailsContentCtl'
        });
})
.run(['$rootScope', '$state', '$stateParams', function($rootScope, $state, $stateParams) {
    $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
        if(!allowAnonymous) {
            if(!$rootScope.currentUser) {
                event.preventDefault();
                $state.go('login');
            } else {
                if(!$rootScope.currentUser.token) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
        }
    });
}]);
