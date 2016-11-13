package org.lobo.myjhipsterapp.web.rest;

import org.lobo.myjhipsterapp.MyJHipsterApp;

import org.lobo.myjhipsterapp.domain.Pokemon;
import org.lobo.myjhipsterapp.repository.PokemonRepository;
import org.lobo.myjhipsterapp.service.PokemonService;
import org.lobo.myjhipsterapp.service.dto.PokemonDTO;
import org.lobo.myjhipsterapp.service.mapper.PokemonMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PokemonResource REST controller.
 *
 * @see PokemonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJHipsterApp.class)
public class PokemonResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private PokemonRepository pokemonRepository;

    @Inject
    private PokemonMapper pokemonMapper;

    @Inject
    private PokemonService pokemonService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPokemonMockMvc;

    private Pokemon pokemon;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PokemonResource pokemonResource = new PokemonResource();
        ReflectionTestUtils.setField(pokemonResource, "pokemonService", pokemonService);
        this.restPokemonMockMvc = MockMvcBuilders.standaloneSetup(pokemonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokemon createEntity(EntityManager em) {
        Pokemon pokemon = new Pokemon()
                .nombre(DEFAULT_NOMBRE)
                .descripcion(DEFAULT_DESCRIPCION);
        return pokemon;
    }

    @Before
    public void initTest() {
        pokemon = createEntity(em);
    }

    @Test
    @Transactional
    public void createPokemon() throws Exception {
        int databaseSizeBeforeCreate = pokemonRepository.findAll().size();

        // Create the Pokemon
        PokemonDTO pokemonDTO = pokemonMapper.pokemonToPokemonDTO(pokemon);

        restPokemonMockMvc.perform(post("/api/pokemons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pokemonDTO)))
                .andExpect(status().isCreated());

        // Validate the Pokemon in the database
        List<Pokemon> pokemons = pokemonRepository.findAll();
        assertThat(pokemons).hasSize(databaseSizeBeforeCreate + 1);
        Pokemon testPokemon = pokemons.get(pokemons.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPokemon.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pokemonRepository.findAll().size();
        // set the field null
        pokemon.setNombre(null);

        // Create the Pokemon, which fails.
        PokemonDTO pokemonDTO = pokemonMapper.pokemonToPokemonDTO(pokemon);

        restPokemonMockMvc.perform(post("/api/pokemons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pokemonDTO)))
                .andExpect(status().isBadRequest());

        List<Pokemon> pokemons = pokemonRepository.findAll();
        assertThat(pokemons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPokemons() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get all the pokemons
        restPokemonMockMvc.perform(get("/api/pokemons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pokemon.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getPokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get the pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", pokemon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pokemon.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPokemon() throws Exception {
        // Get the pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Update the pokemon
        Pokemon updatedPokemon = pokemonRepository.findOne(pokemon.getId());
        updatedPokemon
                .nombre(UPDATED_NOMBRE)
                .descripcion(UPDATED_DESCRIPCION);
        PokemonDTO pokemonDTO = pokemonMapper.pokemonToPokemonDTO(updatedPokemon);

        restPokemonMockMvc.perform(put("/api/pokemons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pokemonDTO)))
                .andExpect(status().isOk());

        // Validate the Pokemon in the database
        List<Pokemon> pokemons = pokemonRepository.findAll();
        assertThat(pokemons).hasSize(databaseSizeBeforeUpdate);
        Pokemon testPokemon = pokemons.get(pokemons.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPokemon.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deletePokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);
        int databaseSizeBeforeDelete = pokemonRepository.findAll().size();

        // Get the pokemon
        restPokemonMockMvc.perform(delete("/api/pokemons/{id}", pokemon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pokemon> pokemons = pokemonRepository.findAll();
        assertThat(pokemons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
