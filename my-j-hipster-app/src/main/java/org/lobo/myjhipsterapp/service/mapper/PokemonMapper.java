package org.lobo.myjhipsterapp.service.mapper;

import org.lobo.myjhipsterapp.domain.*;
import org.lobo.myjhipsterapp.service.dto.PokemonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Pokemon and its DTO PokemonDTO.
 */
@Mapper(componentModel = "spring", uses = {TipoMapper.class, })
public interface PokemonMapper {

    @Mapping(source = "evolucionaA.id", target = "evolucionaAId")
    PokemonDTO pokemonToPokemonDTO(Pokemon pokemon);

    List<PokemonDTO> pokemonsToPokemonDTOs(List<Pokemon> pokemons);

    @Mapping(source = "evolucionaAId", target = "evolucionaA")
    Pokemon pokemonDTOToPokemon(PokemonDTO pokemonDTO);

    List<Pokemon> pokemonDTOsToPokemons(List<PokemonDTO> pokemonDTOs);

    default Pokemon pokemonFromId(Long id) {
        if (id == null) {
            return null;
        }
        Pokemon pokemon = new Pokemon();
        pokemon.setId(id);
        return pokemon;
    }

    default Tipo tipoFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tipo tipo = new Tipo();
        tipo.setId(id);
        return tipo;
    }
}
