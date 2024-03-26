package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.entity.ClienteVaga;
import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.service.ClienteService;
import com.mballen.demoparkapi.service.ClienteVagaService;
import com.mballen.demoparkapi.service.EstacionamentoService;
import com.mballen.demoparkapi.service.VagaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class EstacionamentoServiceTestUnit {

    //------------------------Cliente-------------------------------------
    private static final Long ID = 1L;
    private static final String NOME = "Usuario teste";
    private static final String CPF = "25945427098";
    private static final Usuario USUARIO = new Usuario();
    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR = "useranonymus";
    private static final String MODIFICADO_POR = "useranonymus";
    //-------------------------------------------------------------------------
     //-----------------------------ClienteVaga-------------------------------
    private static final String RECIBO =  "12345678910";
    private static final String PLACA = "3209";
    private static final String MARCA = "FORD";
    private static final String MODELO = "FIESTA";
    private static final String COR = "AZUL";
    private static final LocalDateTime DATA_ENTRADA = LocalDateTime.now();
    private static final LocalDateTime DATA_SAIDA = LocalDateTime.now();
    private static final BigDecimal VALOR = new BigDecimal(25);
    private static final BigDecimal DESCONTO = new BigDecimal(5);
    private static final Cliente CLIENTE = new Cliente();
    private static final Vaga VAGA = new Vaga();
    //-----------------------------------------------------------
    //--------------------Vaga----------------------------------
    private static final String CODIGO = "A-01";
    private static final Vaga.StatusVaga STATUS = Vaga.StatusVaga.LIVRE;
    @InjectMocks
    private EstacionamentoService estacionamentoService;
    @Mock
    private ClienteVagaService clienteVagaService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private  VagaService vagaService;

    private Cliente cliente;
    private ClienteVaga clienteVaga;
    private Vaga vaga;
    @BeforeEach
    void setUp(){
        startEstacionamento();;
    }
    @Test
    void whenCheckInThenReturnClienteVaga(){
        Mockito.when(clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf())).thenReturn(cliente);
        Mockito.when(vagaService.buscarPorVagaLivre()).thenReturn(vaga);
        Mockito.when(clienteVagaService.salvar(Mockito.any())).thenReturn(clienteVaga);
        ClienteVaga response = estacionamentoService.checkIn(clienteVaga);

        Assertions.assertEquals(response.getVaga().getStatus(),Vaga.StatusVaga.OCUPADA);
        Assertions.assertEquals(response.getVaga().getCodigo(),vaga.getCodigo());
    }

    @Test
    void whenCheckOutThenReturnClienteVaga(){
        Mockito.when(clienteVagaService.buscarPorRecibo(Mockito.anyString())).thenReturn(clienteVaga);
        Mockito.when(clienteVagaService.getTotalDeVezesEstacionamentoCompleto
                (clienteVaga.getCliente().getCpf())).thenReturn(10L);
        Mockito.when(clienteVagaService.salvar(Mockito.any())).thenReturn(clienteVaga);
        ClienteVaga response = estacionamentoService.checkOut(RECIBO);
        Assertions.assertEquals(response.getVaga().getStatus(), Vaga.StatusVaga.LIVRE);
    }
    void startEstacionamento(){
        this.cliente = new Cliente(ID,NOME,CPF,USUARIO,
                DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);

        this.vaga = new Vaga(ID,CODIGO,STATUS,DATA_CRIACAO,
                DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);

        this.clienteVaga = new ClienteVaga(ID,RECIBO,PLACA,MARCA,MODELO,COR,DATA_ENTRADA,DATA_SAIDA,
                VALOR,DESCONTO,CLIENTE,VAGA,DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);
    }

}
