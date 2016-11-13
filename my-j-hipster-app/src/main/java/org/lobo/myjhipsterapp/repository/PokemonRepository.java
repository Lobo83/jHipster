package org.lobo.myjhipsterapp.repository;

import org.lobo.myjhipsterapp.domain.Pokemon;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Pokemon entity.
 */
@SuppressWarnings("unused")
public interface PokemonRepository extends JpaRepository<Pokemon,Long> {

    @Query("select distinct pokemon from Pokemon pokemon left join fetch pokemon.tipoPokemons")
    List<Pokemon> findAllWithEagerRelationships();

    @Query("select pokemon from Pokemon pokemon left join fetch pokemon.tipoPokemons where pokemon.id =:id")
    Pokemon findOneWithEagerRelationships(@Param("id") Long id);

}
