package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.exception.CpfUniqueViolationException;
import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.repository.ClienteRepository;
import com.mballen.demoparkapi.repository.projection.ClienteProjection;
import com.mballen.demoparkapi.service.ClienteService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTestUnit {

     private static final Long ID = 1L;
     private static final String NOME = "Usuario teste";
     private static final String CPF = "25945427098";
     private static final Usuario USUARIO = new Usuario();
     private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
     private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
     private static final String CRIADO_POR = "useranonymus";
     private static final String MODIFICADO_POR = "useranonymus";


    @InjectMocks
    private ClienteService clienteService;
    @Mock
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    private Optional<Cliente> optionalCliente;
    Page<ClienteProjection> page;
    @BeforeEach
    void setUp(){
        startsCliente();
    }

    @Test
    void whenSalvarThenReturnCliente(){
        Mockito.when(clienteRepository.save(Mockito.any())).thenReturn(cliente);

        Cliente response = clienteService.salvar(cliente);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getNome(),NOME);
        Assertions.assertEquals(response.getCpf(),CPF);
        Assertions.assertEquals(response.getUsuario(),USUARIO);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);
    }

    @Test
    void whenSalvarThenReturnCpfUniqueViolationException(){
        Mockito.when(clienteRepository.save(Mockito.any())).thenThrow
        (new CpfUniqueViolationException(String.format
                ("CPF '%s' não pode ser cadastrado, ja existe no sistema",cliente.getCpf())));

        try {
            Cliente response = clienteService.salvar(cliente);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getClass(), CpfUniqueViolationException.class);
            Assertions.assertEquals(ex.getMessage(),String.format
                    ("CPF '%s' não pode ser cadastrado, ja existe no sistema",cliente.getCpf()));
        }
    }
    @Test
    void whenBuscarPorIdThenReturnCliente(){
        Mockito.when(clienteRepository.findById(Mockito.anyLong())).thenReturn(optionalCliente);

        Cliente response = clienteService.buscarPorId(ID);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getNome(),NOME);
        Assertions.assertEquals(response.getCpf(),CPF);
        Assertions.assertEquals(response.getUsuario(),USUARIO);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);
    }

    @Test
    void whenBuscarPorIdThenReturnEntityNotFoundException(){
        Mockito.when(clienteRepository.findById(Mockito.anyLong())).thenThrow
        (new EntityNotFoundException(String.format
                ("Cliente id=%s não encontrado no sistema",ID)));

        try {
            Cliente response = clienteService.buscarPorId(ID);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(),String.format("Cliente id=%s não encontrado no sistema",ID));
        }

    }

    @Test
    void whenBuscarTodosThenReturnPageableOfCliente(){
        Mockito.when(clienteRepository.findAllPageable(Mockito.any(Pageable.class))).thenReturn(page);
        Page<ClienteProjection> response = clienteService.buscarTodos(PageRequest.of(0,10));

        Assertions.assertEquals(response,page);
        Assertions.assertEquals(response.getTotalElements(),1L);
        Assertions.assertEquals(response.getContent().get(0).getId(),1L);
        Assertions.assertEquals(response.getContent().get(0).getNome(),"Cliente");
        Assertions.assertEquals(response.getContent().get(0).getCpf(),"123456789");

    }

    @Test
    void whenBuscarUsuarioPordIdThenReturnCliente(){
        Mockito.when(clienteRepository.findByUsuarioId(Mockito.anyLong())).thenReturn(cliente);

        Cliente response = clienteService.buscarUsuarioPorId(ID);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getNome(),NOME);
        Assertions.assertEquals(response.getCpf(),CPF);
        Assertions.assertEquals(response.getUsuario(),USUARIO);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);
    }

    @Test
    void whenBuscarPorCpfThenReturnCliente(){
        Mockito.when(clienteRepository.findByCpf(Mockito.anyString())).thenReturn(optionalCliente);

        Cliente response = clienteService.buscarPorCpf(CPF);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getNome(),NOME);
        Assertions.assertEquals(response.getCpf(),CPF);
        Assertions.assertEquals(response.getUsuario(),USUARIO);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);


    }
    @Test
    void whenBuscarPorCpfThenReturnEntityNotFoundException (){
        Mockito.when(clienteRepository.findByCpf(Mockito.anyString())).thenThrow(new EntityNotFoundException(String.format("Cliente com CPF '%s' não encontrado",CPF)));

        try {
            Cliente response = clienteService.buscarPorCpf(CPF);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getMessage(),String.format("Cliente com CPF '%s' não encontrado",CPF));
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
        }
    }

    void startsCliente(){
        this.cliente = new Cliente(ID,NOME,CPF,USUARIO,DATA_CRIACAO,
                DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);
        this.optionalCliente = Optional.of(new Cliente(ID,NOME,CPF,
                USUARIO,DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR));

        ClienteProjection clienteP = new ClienteProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getNome() {
                return "Cliente";
            }

            @Override
            public String getCpf() {
                return "123456789";
            }
        };
        this.page = new PageImpl<>(Collections.singletonList(clienteP));

    }

}
