index_module.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index");
    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'index.html',
            controller: 'IndexCtl'
        })
        .state('login', {
            url: '/login',
            templateUrl: 'views/index/login.html',
            controller: 'LoginCtl'
        })
        .state('student_info', {
            url: '/student/info',
            templateUrl: 'views/index/student_info.html',
            controller: 'StudentInfoCtl'
        })
        .state('student_info_update', {
            url: '/student/info/update',
            templateUrl: 'views/index/student_info_update.html',
            controller: 'StudentInfoUpdateCtl'
        })
        .state('internship_info', {
            url: '/internship/info',
            templateUrl: 'views/index/internship_info.html',
            controller: 'InternshipInfoCtl'
        })
        .state('blockchain_info', {
            url: '/blockchain/info',
            templateUrl: 'views/index/blockchain_info.html',
            controller: 'BlockchainInfoCtl'
        });
});