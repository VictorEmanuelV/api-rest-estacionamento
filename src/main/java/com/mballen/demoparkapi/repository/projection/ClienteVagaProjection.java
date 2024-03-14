package com.mballen.demoparkapi.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClienteVagaProjection {

     String getplaca();
     String getMarca();
     String getModelo();
     String getCor();
     String getClienteCpf();
     String getRecibo();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mn:ss")
     LocalDateTime getDataEntrada();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mn:ss")
     LocalDateTime getDataSaida();
     String getVagaCodigo();
     BigDecimal getValor();
     BigDecimal getDesconto();

}
