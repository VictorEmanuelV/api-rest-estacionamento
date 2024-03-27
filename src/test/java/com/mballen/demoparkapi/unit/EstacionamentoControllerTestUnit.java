package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.entity.ClienteVaga;
import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.jwt.JwtUserDetails;
import com.mballen.demoparkapi.repository.projection.ClienteVagaProjection;
import com.mballen.demoparkapi.service.ClienteVagaService;
import com.mballen.demoparkapi.service.EstacionamentoService;
import com.mballen.demoparkapi.util.ServletUriBuilderImpl;
import com.mballen.demoparkapi.web.controller.EstacionamentoController;
import com.mballen.demoparkapi.web.dto.EstacionamentoCreateDto;
import com.mballen.demoparkapi.web.dto.EstacionamentoResponseDto;
import com.mballen.demoparkapi.web.dto.PageableDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
public class EstacionamentoControllerTestUnit {
    private static final String USERNAME                = "usuarioTestUnitl@gmail.com";
    private static final String PASSWORD                = "123456";
    private static final Long ID = 1L;
    private static final String RECIBO = "12345678910";
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
    private static final String CPF = "12345678900";
    @InjectMocks
    private EstacionamentoController estacionamentoController;
    @Mock
    private EstacionamentoService estacionamentoService;
    @Mock
    private ClienteVagaService clienteVagaService;
    @Mock
    private ServletUriBuilderImpl servletUriBuilder;
    private ClienteVaga clienteVaga;
    private EstacionamentoCreateDto estacionamentoCreateDto;
    private URI location;

    private Usuario usuario;
    private JwtUserDetails jwtUserDetails;
    @BeforeEach
    void setUp() {
        startsEstacionamento();
    }

    @Test
    void whenCheckinThenReturnEstacionamentoRespondeDto() {
        Mockito.when(estacionamentoService.checkIn(Mockito.any())).thenReturn(clienteVaga);
        Mockito.when(servletUriBuilder.build("/{recibo}",null)).thenReturn(location);

        ResponseEntity<EstacionamentoResponseDto> response = estacionamentoController.checkin(estacionamentoCreateDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getCor(),COR);
        Assertions.assertEquals(response.getBody().getClienteCpf(), clienteVaga.getCliente().getCpf());

    }
    @Test
    void whenGetByReciboThenReturnEstacionamentoRespondeDto(){
        Mockito.when(clienteVagaService.buscarPorRecibo(RECIBO)).thenReturn(clienteVaga);
        ResponseEntity<EstacionamentoResponseDto>response = estacionamentoController.getByRecibo(RECIBO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getPlaca(),PLACA);
        Assertions.assertEquals(response.getBody().getMarca(),MARCA);
        Assertions.assertEquals(response.getBody().getModelo(),MODELO);
        Assertions.assertEquals(response.getBody().getRecibo(),RECIBO);
    }
    @Test
    void whenCheckoutThenReturnEstacionamentoResponseDto(){
        Mockito.when(estacionamentoService.checkOut(RECIBO)).thenReturn(clienteVaga);
        ResponseEntity<EstacionamentoResponseDto>response = estacionamentoController.checkout(RECIBO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getValor(),VALOR);
        Assertions.assertEquals(response.getBody().getDesconto(),DESCONTO);
        Assertions.assertEquals(response.getBody().getPlaca(),PLACA);
        Assertions.assertEquals(response.getBody().getMarca(),MARCA);
        Assertions.assertEquals(response.getBody().getModelo(),MODELO);
        Assertions.assertEquals(response.getBody().getCor(),COR);
    }
    @Test
    void whenGetAllEstacionamentoPorCpfThenReturnPageableOfEstacionamentos(){
        Pageable pageable = PageRequest.of(0, 5);
        Page<ClienteVagaProjection> clienteVagaProjections = Mockito.mock(Page.class);
        Mockito.when(clienteVagaService.buscarTodosPorClienteCpf(CPF, pageable)).thenReturn(clienteVagaProjections);

        ResponseEntity<PageableDto>response = estacionamentoController.getAllEstacionamentosPorCpf(CPF,pageable);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getClass(), PageableDto.class);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);

    }
    @Test
    void whenGetAllEstacionamentoDoClienteThenReturnPageableOfEstacionamento(){

        Pageable pageable = PageRequest.of(0,5);
        Page<ClienteVagaProjection> page = Mockito.mock(Page.class);
        Mockito.when(clienteVagaService.buscarTodosPorUsuarioId(ID,pageable)).thenReturn(page);

        ResponseEntity<PageableDto> response =
                estacionamentoController.getAllEstacionamentosDoCliente(jwtUserDetails,pageable);

        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getClass(), PageableDto.class);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
    }
    void startsEstacionamento() {
        this.clienteVaga =
                new ClienteVaga(ID, RECIBO, PLACA, MARCA, MODELO, COR, DATA_ENTRADA,
                        DATA_SAIDA, VALOR, DESCONTO, CLIENTE, VAGA, DATA_CRIACAO
                        , DATA_MODIFICACAO, CRIADO_POR, MODIFICADO_POR);
        this.location = URI.create("api/v1/estacionamento");

        this.estacionamentoCreateDto = new EstacionamentoCreateDto(PLACA, MARCA, MODELO, COR, CLIENTE.getCpf());
        this.usuario = new Usuario(ID,USERNAME,PASSWORD,DATA_CRIACAO,DATA_MODIFICACAO,
                CRIADO_POR,MODIFICADO_POR,Usuario.Role.ROLE_CLIENTE);

        this.jwtUserDetails = new JwtUserDetails(usuario);
    }
}
