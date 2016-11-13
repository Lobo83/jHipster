(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('TipoMySuffixDetailController', TipoMySuffixDetailController);

    TipoMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tipo'];

    function TipoMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Tipo) {
        var vm = this;

        vm.tipo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myJHipsterApp:tipoUpdate', function(event, result) {
            vm.tipo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
