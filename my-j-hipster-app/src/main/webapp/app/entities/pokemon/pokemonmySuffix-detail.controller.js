(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('PokemonMySuffixDetailController', PokemonMySuffixDetailController);

    PokemonMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pokemon', 'Tipo'];

    function PokemonMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Pokemon, Tipo) {
        var vm = this;

        vm.pokemon = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myJHipsterApp:pokemonUpdate', function(event, result) {
            vm.pokemon = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
