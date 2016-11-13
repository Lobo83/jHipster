package org.lobo.myjhipsterapp.service.mapper;

import org.lobo.myjhipsterapp.domain.*;
import org.lobo.myjhipsterapp.service.dto.TipoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tipo and its DTO TipoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoMapper {

    TipoDTO tipoToTipoDTO(Tipo tipo);

    List<TipoDTO> tiposToTipoDTOs(List<Tipo> tipos);

    Tipo tipoDTOToTipo(TipoDTO tipoDTO);

    List<Tipo> tipoDTOsToTipos(List<TipoDTO> tipoDTOs);
}
