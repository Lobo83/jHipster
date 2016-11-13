(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipomySuffix', {
            parent: 'entity',
            url: '/tipomySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myJHipsterApp.tipo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo/tiposmySuffix.html',
                    controller: 'TipoMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tipomySuffix-detail', {
            parent: 'entity',
            url: '/tipomySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myJHipsterApp.tipo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo/tipomySuffix-detail.html',
                    controller: 'TipoMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tipo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tipo', function($stateParams, Tipo) {
                    return Tipo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tipomySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tipomySuffix-detail.edit', {
            parent: 'tipomySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo/tipomySuffix-dialog.html',
                    controller: 'TipoMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tipo', function(Tipo) {
                            return Tipo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipomySuffix.new', {
            parent: 'tipomySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo/tipomySuffix-dialog.html',
                    controller: 'TipoMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                descripcion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipomySuffix', null, { reload: 'tipomySuffix' });
                }, function() {
                    $state.go('tipomySuffix');
                });
            }]
        })
        .state('tipomySuffix.edit', {
            parent: 'tipomySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo/tipomySuffix-dialog.html',
                    controller: 'TipoMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tipo', function(Tipo) {
                            return Tipo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipomySuffix', null, { reload: 'tipomySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipomySuffix.delete', {
            parent: 'tipomySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo/tipomySuffix-delete-dialog.html',
                    controller: 'TipoMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tipo', function(Tipo) {
                            return Tipo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipomySuffix', null, { reload: 'tipomySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
