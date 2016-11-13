package org.lobo.myjhipsterapp.web.rest;

import org.lobo.myjhipsterapp.MyJHipsterApp;

import org.lobo.myjhipsterapp.domain.Tipo;
import org.lobo.myjhipsterapp.repository.TipoRepository;
import org.lobo.myjhipsterapp.service.TipoService;
import org.lobo.myjhipsterapp.service.dto.TipoDTO;
import org.lobo.myjhipsterapp.service.mapper.TipoMapper;

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
 * Test class for the TipoResource REST controller.
 *
 * @see TipoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJHipsterApp.class)
public class TipoResourceIntTest {

    private static final String DEFAULT_TIPO = "AAAAA";
    private static final String UPDATED_TIPO = "BBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private TipoRepository tipoRepository;

    @Inject
    private TipoMapper tipoMapper;

    @Inject
    private TipoService tipoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTipoMockMvc;

    private Tipo tipo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoResource tipoResource = new TipoResource();
        ReflectionTestUtils.setField(tipoResource, "tipoService", tipoService);
        this.restTipoMockMvc = MockMvcBuilders.standaloneSetup(tipoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createEntity(EntityManager em) {
        Tipo tipo = new Tipo()
                .tipo(DEFAULT_TIPO)
                .descripcion(DEFAULT_DESCRIPCION);
        return tipo;
    }

    @Before
    public void initTest() {
        tipo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipo() throws Exception {
        int databaseSizeBeforeCreate = tipoRepository.findAll().size();

        // Create the Tipo
        TipoDTO tipoDTO = tipoMapper.tipoToTipoDTO(tipo);

        restTipoMockMvc.perform(post("/api/tipos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
                .andExpect(status().isCreated());

        // Validate the Tipo in the database
        List<Tipo> tipos = tipoRepository.findAll();
        assertThat(tipos).hasSize(databaseSizeBeforeCreate + 1);
        Tipo testTipo = tipos.get(tipos.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTipo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoRepository.findAll().size();
        // set the field null
        tipo.setTipo(null);

        // Create the Tipo, which fails.
        TipoDTO tipoDTO = tipoMapper.tipoToTipoDTO(tipo);

        restTipoMockMvc.perform(post("/api/tipos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
                .andExpect(status().isBadRequest());

        List<Tipo> tipos = tipoRepository.findAll();
        assertThat(tipos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipos() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipos
        restTipoMockMvc.perform(get("/api/tipos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get the tipo
        restTipoMockMvc.perform(get("/api/tipos/{id}", tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipo.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipo() throws Exception {
        // Get the tipo
        restTipoMockMvc.perform(get("/api/tipos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo
        Tipo updatedTipo = tipoRepository.findOne(tipo.getId());
        updatedTipo
                .tipo(UPDATED_TIPO)
                .descripcion(UPDATED_DESCRIPCION);
        TipoDTO tipoDTO = tipoMapper.tipoToTipoDTO(updatedTipo);

        restTipoMockMvc.perform(put("/api/tipos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tipoDTO)))
                .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipos = tipoRepository.findAll();
        assertThat(tipos).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipos.get(tipos.size() - 1);
        assertThat(testTipo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTipo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);
        int databaseSizeBeforeDelete = tipoRepository.findAll().size();

        // Get the tipo
        restTipoMockMvc.perform(delete("/api/tipos/{id}", tipo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tipo> tipos = tipoRepository.findAll();
        assertThat(tipos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
