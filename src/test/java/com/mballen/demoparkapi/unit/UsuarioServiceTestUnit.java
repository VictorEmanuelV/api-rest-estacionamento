package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.exception.CpfUniqueViolationException;
import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.exception.PasswordInvalidException;
import com.mballen.demoparkapi.exception.UsernameUniqueViolationException;
import com.mballen.demoparkapi.repository.UsuarioRepository;
import com.mballen.demoparkapi.service.UsuarioService;
import com.mballen.demoparkapi.web.dto.UsuarioCreateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTestUnit {

    private static final Long   ID = 1L;
    private static final String USERNAME                = "victoremanuel@gmail.com";
    private static final String PASSWORD                = "123456";
    private static final LocalDateTime DATA_CRIACAO     = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR              = "anonymusUser";

    private static final String MODIFICADO_POR          = "anonymusUser";
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioRepository usuarioRepository;
    private Usuario usuario;

    private Optional<Usuario> optionalUsuario;

    private List<Usuario> usuarios;

    @BeforeEach
    void setUp(){
        startUsuario();

    }
    @Test
    void whenSalvarThenReturnUsuario(){
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("123456");
        Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(usuario);

        Usuario response = usuarioService.salvar(usuario);

        Mockito.verify(usuarioRepository,Mockito.times(1)).save(usuario);
        Mockito.verify(passwordEncoder,Mockito.times(1)).encode("123456");

        Assertions.assertEquals(ID,response.getId());
        Assertions.assertEquals(USERNAME,response.getUsername());
        Assertions.assertEquals(PASSWORD,response.getPassword());
        Assertions.assertEquals(DATA_CRIACAO,response.getDataCriacao());
        Assertions.assertEquals(DATA_MODIFICACAO,response.getDataModificacao());
        Assertions.assertEquals(CRIADO_POR,response.getCriadoPor());
        Assertions.assertEquals(MODIFICADO_POR,response.getModificadoPor());

    }
    @Test
    void  whenSalvarThenReturnUsernameUniqueViolationException(){
        UsernameUniqueViolationException ex = new UsernameUniqueViolationException(String.format("Username {%s} ja cadastrado",usuario.getUsername()));
        Mockito.when(usuarioRepository.save(Mockito.any())).thenThrow(ex);

        try {
            usuarioService.salvar(usuario);
        }catch (Exception e){
            Assertions.assertEquals(e.getClass(), UsernameUniqueViolationException.class);
            Assertions.assertEquals(e.getMessage(),String.format("Username {%s} ja cadastrado",usuario.getUsername()));
        }
    }

    @Test
    void whenEditarSenhaThenReturnUsuarioWithThePasswordChanged(){

        Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(optionalUsuario);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("333333");
        Mockito.when(passwordEncoder.matches(PASSWORD, usuario.getPassword())).thenReturn(true);
        Usuario response = usuarioService.editarSenha(ID,PASSWORD,"333333","333333");

        Assertions.assertEquals(response.getPassword(),"333333");

    }

    @Test
    void whenEditarSenhaWithDifferentPasswordThenReturnPasswordInvalidException(){

        try {
            Usuario response = usuarioService.editarSenha(ID,PASSWORD,"555555","333333");
        }catch (Exception ex){
            Assertions.assertEquals(ex.getClass(), PasswordInvalidException.class);
            Assertions.assertEquals(ex.getMessage(),"Nova senha não confere com a confirmação de senha");
        }
    }
    @Test
    void whenEditarSenhaWithPasswordAcctualDifferentThenReturnPasswordInvalidException(){
       Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(optionalUsuario);

        try {
            Usuario response = usuarioService.editarSenha(ID,PASSWORD,"333333","333333");
        }catch (Exception ex){
            Assertions.assertEquals(ex.getMessage(),"Sua senha não confere");
            Assertions.assertEquals(ex.getClass(), PasswordInvalidException.class);
        }
    }

    @Test
    void whenBuscarTodosThenReturnListOfUsuario(){
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> response = usuarioService.buscarTodos();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getClass(),Usuario.class);
        Assertions.assertEquals(response.get(0).getId(),ID);
        Assertions.assertEquals(response.get(0).getUsername(),USERNAME);
        Assertions.assertEquals(response.get(0).getPassword(),PASSWORD);
        Assertions.assertEquals(response.get(0).getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.get(0).getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.get(0).getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.get(0).getModificadoPor(),MODIFICADO_POR);

    }

    @Test
    void whenBuscarPorUsernameThenReturnUsuario(){
        Mockito.when(usuarioRepository.findByUsername(Mockito.anyString())).thenReturn(optionalUsuario);

        Usuario response = usuarioService.buscarPorUsername(USERNAME);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getUsername(),USERNAME);
        Assertions.assertEquals(response.getPassword(),PASSWORD);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);
    }
    @Test
    void whenBuscarPorUsernameThenReturnEntityNotFoundException(){
        Mockito.when(usuarioRepository.findByUsername(Mockito.anyString())).thenThrow(new EntityNotFoundException(String.format("Usuario com '%s' não encontrado",USERNAME)));

        try {
            Usuario response = usuarioService.buscarPorUsername(USERNAME);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getMessage(),String.format("Usuario com '%s' não encontrado",USERNAME));
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    void whenBuscarRolePorUsernameThenReturnRole(){
        Mockito.when(usuarioRepository.findRoleByUsername(Mockito.anyString())).thenReturn(Usuario.Role.ROLE_CLIENTE);

        Usuario.Role response = usuarioService.buscarRolePorUsername(USERNAME);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getClass(), Usuario.Role.class);
        Assertions.assertEquals(response, Usuario.Role.ROLE_CLIENTE);

    }

    void startUsuario(){
        this.usuario =
        new Usuario
        (ID,USERNAME,PASSWORD,DATA_CRIACAO,DATA_MODIFICACAO
        ,CRIADO_POR,MODIFICADO_POR, Usuario.Role.ROLE_CLIENTE);

        this.optionalUsuario = Optional.of( new Usuario
                (ID,USERNAME,PASSWORD,DATA_CRIACAO,DATA_MODIFICACAO
                        ,CRIADO_POR,MODIFICADO_POR, Usuario.Role.ROLE_CLIENTE));

        usuarios = new ArrayList<>();
        usuarios.add(usuario);
    }


}