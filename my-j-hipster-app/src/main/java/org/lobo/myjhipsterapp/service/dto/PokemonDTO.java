package org.lobo.myjhipsterapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Pokemon entity.
 */
public class PokemonDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private String descripcion;


    private Long evolucionaAId;
    
    private Set<TipoDTO> tipoPokemons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getEvolucionaAId() {
        return evolucionaAId;
    }

    public void setEvolucionaAId(Long pokemonId) {
        this.evolucionaAId = pokemonId;
    }

    public Set<TipoDTO> getTipoPokemons() {
        return tipoPokemons;
    }

    public void setTipoPokemons(Set<TipoDTO> tipos) {
        this.tipoPokemons = tipos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PokemonDTO pokemonDTO = (PokemonDTO) o;

        if ( ! Objects.equals(id, pokemonDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PokemonDTO{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
