package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.util.EstacionamentoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EstacionamentoUtilsTestUnit {
    @Test
    void whenGerarReciboThenReturnRecbibo(){
        String recibo =  EstacionamentoUtils.gerarRecibo();
        Assertions.assertNotNull(recibo);
        Assertions.assertTrue(recibo.contains("-"));
    }

    @Test
    void whenCalcularCustoThenReturnCusto(){

        BigDecimal custo = EstacionamentoUtils.calcularCusto(LocalDateTime.now(),LocalDateTime.now());
        Assertions.assertNotNull(custo);
        Assertions.assertEquals(custo,new BigDecimal("5.00"));
    }
    @Test
    void whenCalcularDescontoThenReturnDesconto(){

        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(new BigDecimal(10),10);

        Assertions.assertNotNull(desconto);
        Assertions.assertEquals(desconto,new BigDecimal("3.00"));
    }
}
