package org.lobo.myjhipsterapp.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Tabla de pokemon                                                            
 * 
 */
@ApiModel(description = "Tabla de pokemon")
@Entity
@Table(name = "pokemon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToOne
    @JoinColumn(unique = true)
    private Pokemon evolucionaA;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pokemon_tipo_pokemon",
               joinColumns = @JoinColumn(name="pokemons_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tipo_pokemons_id", referencedColumnName="ID"))
    private Set<Tipo> tipoPokemons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Pokemon nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Pokemon descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pokemon getEvolucionaA() {
        return evolucionaA;
    }

    public Pokemon evolucionaA(Pokemon pokemon) {
        this.evolucionaA = pokemon;
        return this;
    }

    public void setEvolucionaA(Pokemon pokemon) {
        this.evolucionaA = pokemon;
    }

    public Set<Tipo> getTipoPokemons() {
        return tipoPokemons;
    }

    public Pokemon tipoPokemons(Set<Tipo> tipos) {
        this.tipoPokemons = tipos;
        return this;
    }

    public Pokemon addTipoPokemon(Tipo tipo) {
        tipoPokemons.add(tipo);
        return this;
    }

    public Pokemon removeTipoPokemon(Tipo tipo) {
        tipoPokemons.remove(tipo);
        return this;
    }

    public void setTipoPokemons(Set<Tipo> tipos) {
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
        Pokemon pokemon = (Pokemon) o;
        if(pokemon.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pokemon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
