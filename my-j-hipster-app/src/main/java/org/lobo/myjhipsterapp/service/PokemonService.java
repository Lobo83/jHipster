package org.lobo.myjhipsterapp.service;

import org.lobo.myjhipsterapp.service.dto.PokemonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Pokemon.
 */
public interface PokemonService {

    /**
     * Save a pokemon.
     *
     * @param pokemonDTO the entity to save
     * @return the persisted entity
     */
    PokemonDTO save(PokemonDTO pokemonDTO);

    /**
     *  Get all the pokemons.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PokemonDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" pokemon.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PokemonDTO findOne(Long id);

    /**
     *  Delete the "id" pokemon.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
