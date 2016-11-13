(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pokemonmySuffix', {
            parent: 'entity',
            url: '/pokemonmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myJHipsterApp.pokemon.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pokemon/pokemonsmySuffix.html',
                    controller: 'PokemonMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pokemon');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pokemonmySuffix-detail', {
            parent: 'entity',
            url: '/pokemonmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myJHipsterApp.pokemon.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pokemon/pokemonmySuffix-detail.html',
                    controller: 'PokemonMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pokemon');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pokemon', function($stateParams, Pokemon) {
                    return Pokemon.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pokemonmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pokemonmySuffix-detail.edit', {
            parent: 'pokemonmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pokemon/pokemonmySuffix-dialog.html',
                    controller: 'PokemonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pokemon', function(Pokemon) {
                            return Pokemon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pokemonmySuffix.new', {
            parent: 'pokemonmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pokemon/pokemonmySuffix-dialog.html',
                    controller: 'PokemonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                descripcion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pokemonmySuffix', null, { reload: 'pokemonmySuffix' });
                }, function() {
                    $state.go('pokemonmySuffix');
                });
            }]
        })
        .state('pokemonmySuffix.edit', {
            parent: 'pokemonmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pokemon/pokemonmySuffix-dialog.html',
                    controller: 'PokemonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pokemon', function(Pokemon) {
                            return Pokemon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pokemonmySuffix', null, { reload: 'pokemonmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pokemonmySuffix.delete', {
            parent: 'pokemonmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pokemon/pokemonmySuffix-delete-dialog.html',
                    controller: 'PokemonMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pokemon', function(Pokemon) {
                            return Pokemon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pokemonmySuffix', null, { reload: 'pokemonmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
