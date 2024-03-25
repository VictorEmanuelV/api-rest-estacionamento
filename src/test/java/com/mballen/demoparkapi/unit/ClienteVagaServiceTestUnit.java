package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.entity.ClienteVaga;
import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.repository.ClienteVagaRepository;
import com.mballen.demoparkapi.repository.projection.ClienteProjection;
import com.mballen.demoparkapi.repository.projection.ClienteVagaProjection;
import com.mballen.demoparkapi.service.ClienteService;
import com.mballen.demoparkapi.service.ClienteVagaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteVagaServiceTestUnit {
    private static final Long ID = 1L;
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
    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR = "useranonymus";
    private static final String MODIFICADO_POR = "useranonymus";

    Page<ClienteVagaProjection> page;

    private static final String CPF = "98866317080";
    @InjectMocks
    private ClienteVagaService clienteVagaService;
    @Mock
    private ClienteVagaRepository clienteVagaRepository;

    private ClienteVaga clienteVaga;

    private Pageable pageable;
    private Optional<ClienteVaga> optionalClienteVaga;
    @BeforeEach
    void setUp(){
            startClienteVaga();
    }
    @Test
    void whenSalvarThenReturnClienteVaga(){
        Mockito.when(clienteVagaRepository.save(Mockito.any())).thenReturn(clienteVaga);
        ClienteVaga response = clienteVagaService.salvar(clienteVaga);
        clienteVagaEquals(response);

    }
    @Test
    void whenBuscarPorReciboThenReturnClienteVaga(){
        Mockito.when(clienteVagaRepository.findByReciboAndDataSaidaIsNull
                (Mockito.anyString())).thenReturn(optionalClienteVaga);

        ClienteVaga response = clienteVagaService.buscarPorRecibo(RECIBO);
        clienteVagaEquals(response);

    }

    @Test
    void whenBuscarPorReciboThenReturnEntityNotFoundException(){
        Mockito.when(clienteVagaRepository.findByReciboAndDataSaidaIsNull(Mockito.anyString())).thenThrow(
                (new EntityNotFoundException(String.format
                        ("Recibo '%s' não encontrado no sistema ou check-out ja realizado",RECIBO))));

              try{
                  ClienteVaga response = clienteVagaService.buscarPorRecibo(RECIBO);
              }catch (Exception ex){
                  Assertions.assertEquals(ex.getClass(),EntityNotFoundException.class);
                  Assertions.assertEquals(ex.getMessage(),String.format
                          ("Recibo '%s' não encontrado no sistema ou check-out ja realizado",RECIBO));
              }
    }
    @Test
    void getTotalDeVezesEstacionamentoCompleto(){
        Mockito.when(clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull
                (Mockito.anyString())).thenReturn(1L);

        Long response = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(CPF);

        Assertions.assertEquals(response,1L);
    }
    @Test
    void whenBuscarTodosPorClienteCpfThenReturnPageable(){
        Mockito.when(clienteVagaRepository.findAllByClienteCpf(Mockito.anyString(),Mockito.any())).thenReturn(page);

        Page<ClienteVagaProjection> response = clienteVagaService.buscarTodosPorClienteCpf(CPF,pageable);
        clienteVagaEqualsPageable(response);

    }
    @Test
    void whenBuscarTodosPorUsuarioIdThenReturnPageable(){
        Mockito.when(clienteVagaRepository.findAllByClienteUsuarioId(Mockito.anyLong(),Mockito.any())).thenReturn(page);
        Page<ClienteVagaProjection>response = clienteVagaService.buscarTodosPorUsuarioId(ID,pageable);
        clienteVagaEqualsPageable(response);
    }

    void startClienteVaga(){
        this.clienteVaga =
                new ClienteVaga(ID,RECIBO,PLACA,MARCA,MODELO,COR,DATA_ENTRADA,
                        DATA_SAIDA,VALOR,DESCONTO,CLIENTE,VAGA,DATA_CRIACAO
                        ,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);
        this.optionalClienteVaga = Optional.of(new ClienteVaga(ID,RECIBO,PLACA,MARCA,MODELO,COR,DATA_ENTRADA,
                DATA_SAIDA,VALOR,DESCONTO,CLIENTE,VAGA,DATA_CRIACAO
                ,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR));

        ClienteVagaProjection clienteVagaP = new ClienteVagaProjection() {
            @Override
            public String getplaca() {
                return "1111";
            }

            @Override
            public String getMarca() {
                return "FORD";
            }

            @Override
            public String getModelo() {
                return "FIESTA";
            }

            @Override
            public String getCor() {
                return "AZUL";
            }

            @Override
            public String getClienteCpf() {
                return CPF;
            }

            @Override
            public String getRecibo() {
                return RECIBO;
            }

            @Override
            public LocalDateTime getDataEntrada() {
                return DATA_ENTRADA;
            }

            @Override
            public LocalDateTime getDataSaida() {
                return DATA_SAIDA;
            }

            @Override
            public String getVagaCodigo() {
                return "A-01";
            }

            @Override
            public BigDecimal getValor() {
                return VALOR;
            }

            @Override
            public BigDecimal getDesconto() {
                return DESCONTO;
            }

        };

        this.page = new PageImpl<>(Collections.singletonList(clienteVagaP));
    }

    void clienteVagaEquals(ClienteVaga response){
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getRecibo(),RECIBO);
        Assertions.assertEquals(response.getPlaca(),PLACA);
        Assertions.assertEquals(response.getMarca(),MARCA);
        Assertions.assertEquals(response.getModelo(),MODELO);
        Assertions.assertEquals(response.getCor(),COR);
        Assertions.assertEquals(response.getDataEntrada(),DATA_ENTRADA);
        Assertions.assertEquals(response.getDataSaida(),DATA_SAIDA);
        Assertions.assertEquals(response.getValor(),VALOR);
        Assertions.assertEquals(response.getDesconto(),DESCONTO);
        Assertions.assertEquals(response.getCliente(),CLIENTE);
        Assertions.assertEquals(response.getVaga(),VAGA);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);
    }
    void clienteVagaEqualsPageable(Page<ClienteVagaProjection>response){

        Assertions.assertEquals(response.getContent().get(0).getClienteCpf(),CPF);
        Assertions.assertEquals(response.getContent().get(0).getCor(),"AZUL");
        Assertions.assertEquals(response.getContent().get(0).getplaca(),"1111");
        Assertions.assertEquals(response.getContent().get(0).getMarca(),"FORD");
        Assertions.assertEquals(response.getContent().get(0).getModelo(),"FIESTA");
        Assertions.assertEquals(response.getContent().get(0).getRecibo(),RECIBO);
        Assertions.assertEquals(response.getContent().get(0).getVagaCodigo(),"A-01");
        Assertions.assertEquals(response.getContent().get(0).getValor(),VALOR);
        Assertions.assertEquals(response.getContent().get(0).getDataEntrada(),DATA_ENTRADA);
        Assertions.assertEquals(response.getContent().get(0).getDataSaida(),DATA_SAIDA);
        Assertions.assertEquals(response.getContent().get(0).getDesconto(),DESCONTO);
    }
}
