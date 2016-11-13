package org.lobo.myjhipsterapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.lobo.myjhipsterapp.service.PokemonService;
import org.lobo.myjhipsterapp.web.rest.util.HeaderUtil;
import org.lobo.myjhipsterapp.web.rest.util.PaginationUtil;
import org.lobo.myjhipsterapp.service.dto.PokemonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Pokemon.
 */
@RestController
@RequestMapping("/api")
public class PokemonResource {

    private final Logger log = LoggerFactory.getLogger(PokemonResource.class);
        
    @Inject
    private PokemonService pokemonService;

    /**
     * POST  /pokemons : Create a new pokemon.
     *
     * @param pokemonDTO the pokemonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pokemonDTO, or with status 400 (Bad Request) if the pokemon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pokemons")
    @Timed
    public ResponseEntity<PokemonDTO> createPokemon(@Valid @RequestBody PokemonDTO pokemonDTO) throws URISyntaxException {
        log.debug("REST request to save Pokemon : {}", pokemonDTO);
        if (pokemonDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pokemon", "idexists", "A new pokemon cannot already have an ID")).body(null);
        }
        PokemonDTO result = pokemonService.save(pokemonDTO);
        return ResponseEntity.created(new URI("/api/pokemons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pokemon", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pokemons : Updates an existing pokemon.
     *
     * @param pokemonDTO the pokemonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pokemonDTO,
     * or with status 400 (Bad Request) if the pokemonDTO is not valid,
     * or with status 500 (Internal Server Error) if the pokemonDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pokemons")
    @Timed
    public ResponseEntity<PokemonDTO> updatePokemon(@Valid @RequestBody PokemonDTO pokemonDTO) throws URISyntaxException {
        log.debug("REST request to update Pokemon : {}", pokemonDTO);
        if (pokemonDTO.getId() == null) {
            return createPokemon(pokemonDTO);
        }
        PokemonDTO result = pokemonService.save(pokemonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pokemon", pokemonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pokemons : get all the pokemons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pokemons in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/pokemons")
    @Timed
    public ResponseEntity<List<PokemonDTO>> getAllPokemons(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pokemons");
        Page<PokemonDTO> page = pokemonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pokemons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pokemons/:id : get the "id" pokemon.
     *
     * @param id the id of the pokemonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pokemonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pokemons/{id}")
    @Timed
    public ResponseEntity<PokemonDTO> getPokemon(@PathVariable Long id) {
        log.debug("REST request to get Pokemon : {}", id);
        PokemonDTO pokemonDTO = pokemonService.findOne(id);
        return Optional.ofNullable(pokemonDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pokemons/:id : delete the "id" pokemon.
     *
     * @param id the id of the pokemonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pokemons/{id}")
    @Timed
    public ResponseEntity<Void> deletePokemon(@PathVariable Long id) {
        log.debug("REST request to delete Pokemon : {}", id);
        pokemonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pokemon", id.toString())).build();
    }

}
