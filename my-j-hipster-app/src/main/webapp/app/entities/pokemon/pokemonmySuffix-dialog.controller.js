(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('PokemonMySuffixDialogController', PokemonMySuffixDialogController);

    PokemonMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Pokemon', 'Tipo'];

    function PokemonMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Pokemon, Tipo) {
        var vm = this;

        vm.pokemon = entity;
        vm.clear = clear;
        vm.save = save;
        vm.evolucionaas = Pokemon.query({filter: 'pokemon-is-null'});
        $q.all([vm.pokemon.$promise, vm.evolucionaas.$promise]).then(function() {
            if (!vm.pokemon.evolucionaAId) {
                return $q.reject();
            }
            return Pokemon.get({id : vm.pokemon.evolucionaAId}).$promise;
        }).then(function(evolucionaA) {
            vm.evolucionaas.push(evolucionaA);
        });

        vm.tipos = Tipo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pokemon.id !== null) {
                Pokemon.update(vm.pokemon, onSaveSuccess, onSaveError);
            } else {
                Pokemon.save(vm.pokemon, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myJHipsterApp:pokemonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
