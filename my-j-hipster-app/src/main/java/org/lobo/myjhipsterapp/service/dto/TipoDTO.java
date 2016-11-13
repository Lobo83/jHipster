package org.lobo.myjhipsterapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Tipo entity.
 */
public class TipoDTO implements Serializable {

    private Long id;

    @NotNull
    private String tipo;

    private String descripcion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TipoDTO tipoDTO = (TipoDTO) o;

        if ( ! Objects.equals(id, tipoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TipoDTO{" +
            "id=" + id +
            ", tipo='" + tipo + "'" +
            ", descripcion='" + descripcion + "'" +
            '}';
    }
}
