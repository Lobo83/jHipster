(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('TipoMySuffixDeleteController',TipoMySuffixDeleteController);

    TipoMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tipo'];

    function TipoMySuffixDeleteController($uibModalInstance, entity, Tipo) {
        var vm = this;

        vm.tipo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tipo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
