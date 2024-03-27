package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.jwt.JwtUserDetails;
import com.mballen.demoparkapi.repository.projection.ClienteProjection;
import com.mballen.demoparkapi.service.ClienteService;
import com.mballen.demoparkapi.service.UsuarioService;
import com.mballen.demoparkapi.web.controller.ClienteController;
import com.mballen.demoparkapi.web.dto.ClienteCreateDto;
import com.mballen.demoparkapi.web.dto.ClienteResponseDto;
import com.mballen.demoparkapi.web.dto.PageableDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTestUnit {
    private static final String USERNAME                = "usuarioTestUnitl@gmail.com";
    private static final String PASSWORD                = "123456";

    private static final Long ID = 1L;
    private static final String NOME = "Usuario teste";
    private static final String CPF = "25945427098";
    private static final Usuario USUARIO = new Usuario();
    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR = "useranonymus";
    private static final String MODIFICADO_POR = "useranonymus";
    @InjectMocks
    private ClienteController clienteController;
    @Mock
    private ClienteService clienteService;
    @Mock
    private UsuarioService usuarioService;

    private Cliente cliente;

    private JwtUserDetails userDetails;
    private ClienteCreateDto clienteCreateDto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        startCliente();
        startUsuario();
        startsUserDetails();
    }
    @Test
    void whenCreateThenReturnClienteResponseEntity() {
        Mockito.when(usuarioService.buscarPorId(Mockito.anyLong())).thenReturn(usuario);
        ResponseEntity<ClienteResponseDto> response = clienteController.create(clienteCreateDto,userDetails);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBody().getClass(), ClienteResponseDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getNome(),NOME);
        Assertions.assertEquals(response.getBody().getCpf(),CPF);
    }
    @Test
    void whenFindByIdThenReturnClienteResponseEntity(){
        Mockito.when(clienteService.buscarPorId(Mockito.anyLong())).thenReturn(cliente);

        ResponseEntity<ClienteResponseDto> response = clienteController.findById(ID);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBody().getClass(), ClienteResponseDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getNome(),NOME);
        Assertions.assertEquals(response.getBody().getCpf(),CPF);
    }
    @Test
    void whenGetAllThenReturnResponseEntityPageableOfClientes(){
        Pageable pageable = PageRequest.of(0,5);
        Page<ClienteProjection>page = Mockito.mock(Page.class);
        Mockito.when(clienteService.buscarTodos(Mockito.any())).thenReturn(page);
        ResponseEntity<PageableDto> response = clienteController.getAll(pageable);

        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(), PageableDto.class);

    }
    @Test
    void whenGetDetalhesThenReturnClienteRespondeDto(){
        Mockito.when(clienteService.buscarUsuarioPorId(Mockito.anyLong())).thenReturn(cliente);
        ResponseEntity<ClienteResponseDto>response = clienteController.getDetalhes(userDetails);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(), ClienteResponseDto.class);
        Assertions.assertEquals(response.getBody().getCpf(),CPF);
        Assertions.assertEquals(response.getBody().getNome(),NOME);

    }
    void startCliente() {
        this.cliente = new Cliente(ID, NOME, CPF, USUARIO, DATA_CRIACAO,
                DATA_MODIFICACAO, CRIADO_POR, MODIFICADO_POR);

        this.clienteCreateDto = new ClienteCreateDto(NOME,CPF);

    }

    void startUsuario() {
        this.usuario =
                new Usuario
                        (ID, USERNAME, PASSWORD, DATA_CRIACAO, DATA_MODIFICACAO
                                , CRIADO_POR, MODIFICADO_POR, Usuario.Role.ROLE_CLIENTE);
    }
    void startsUserDetails(){
        this.userDetails = new JwtUserDetails(usuario);
    }
}
