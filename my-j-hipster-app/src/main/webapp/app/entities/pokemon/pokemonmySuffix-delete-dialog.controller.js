(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('PokemonMySuffixDeleteController',PokemonMySuffixDeleteController);

    PokemonMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pokemon'];

    function PokemonMySuffixDeleteController($uibModalInstance, entity, Pokemon) {
        var vm = this;

        vm.pokemon = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pokemon.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
