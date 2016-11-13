(function() {
    'use strict';

    angular
        .module('myJHipsterApp')
        .controller('PokemonMySuffixController', PokemonMySuffixController);

    PokemonMySuffixController.$inject = ['$scope', '$state', 'Pokemon', 'ParseLinks', 'AlertService'];

    function PokemonMySuffixController ($scope, $state, Pokemon, ParseLinks, AlertService) {
        var vm = this;
        
        vm.pokemons = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();


        function loadAll () {
            Pokemon.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                	vm.pokemon=data[i];
                	if(null != data[i].evolucionaAId){
                		vm.pokemon.evolucion=Pokemon.get({id:data[i].evolucionaAId});
                	}
                	
                    vm.pokemons.push(vm.pokemon);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.pokemons = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
