package org.lobo.myjhipsterapp.service.impl;

import org.lobo.myjhipsterapp.service.PokemonService;
import org.lobo.myjhipsterapp.domain.Pokemon;
import org.lobo.myjhipsterapp.repository.PokemonRepository;
import org.lobo.myjhipsterapp.service.dto.PokemonDTO;
import org.lobo.myjhipsterapp.service.mapper.PokemonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Pokemon.
 */
@Service
@Transactional
public class PokemonServiceImpl implements PokemonService{

    private final Logger log = LoggerFactory.getLogger(PokemonServiceImpl.class);
    
    @Inject
    private PokemonRepository pokemonRepository;

    @Inject
    private PokemonMapper pokemonMapper;

    /**
     * Save a pokemon.
     *
     * @param pokemonDTO the entity to save
     * @return the persisted entity
     */
    public PokemonDTO save(PokemonDTO pokemonDTO) {
        log.debug("Request to save Pokemon : {}", pokemonDTO);
        Pokemon pokemon = pokemonMapper.pokemonDTOToPokemon(pokemonDTO);
        pokemon = pokemonRepository.save(pokemon);
        PokemonDTO result = pokemonMapper.pokemonToPokemonDTO(pokemon);
        return result;
    }

    /**
     *  Get all the pokemons.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PokemonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pokemons");
        Page<Pokemon> result = pokemonRepository.findAll(pageable);
        return result.map(pokemon -> pokemonMapper.pokemonToPokemonDTO(pokemon));
    }

    /**
     *  Get one pokemon by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PokemonDTO findOne(Long id) {
        log.debug("Request to get Pokemon : {}", id);
        Pokemon pokemon = pokemonRepository.findOneWithEagerRelationships(id);
        PokemonDTO pokemonDTO = pokemonMapper.pokemonToPokemonDTO(pokemon);
        return pokemonDTO;
    }

    /**
     *  Delete the  pokemon by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Pokemon : {}", id);
        pokemonRepository.delete(id);
    }
}
