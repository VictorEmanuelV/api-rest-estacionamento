package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Usuario;
import com.mballen.demoparkapi.service.UsuarioService;
import com.mballen.demoparkapi.web.controller.UsuarioController;
import com.mballen.demoparkapi.web.dto.UsuarioCreateDto;
import com.mballen.demoparkapi.web.dto.UsuarioResponseDto;
import com.mballen.demoparkapi.web.dto.UsuarioSenhaDto;
import com.mballen.demoparkapi.web.dto.mapper.UsuarioMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTestUnit {
    private static final Long   ID = 1L;
    private static final String USERNAME                = "usuarioTestUnitl@gmail.com";
    private static final String PASSWORD                = "123456";
    private static final LocalDateTime DATA_CRIACAO     = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR              = "anonymusUser";

    private static final String MODIFICADO_POR          = "anonymusUser";
    @InjectMocks
    private UsuarioController usuarioController;
    @Mock
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioCreateDto usuarioCreateDto;

    private UsuarioSenhaDto usuarioSenhaDto;

    private List<Usuario> usuarioList;

    @BeforeEach
    void setUp(){
        startUsuario();
    }
    @Test
    void whenCreateThenReturnResponseEntityUsuario(){
        Mockito.when(usuarioService.salvar(Mockito.any())).thenReturn(UsuarioMapper.toUsuario(usuarioCreateDto));

        ResponseEntity<UsuarioResponseDto> response = usuarioController.create(usuarioCreateDto);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getBody().getClass(), UsuarioResponseDto.class);
        Assertions.assertEquals(response.getBody().getRole(), "CLIENTE");
        Assertions.assertEquals(response.getBody().getUsername(),USERNAME);

    }
    @Test
    void whenGetByIdThenReturnResponseEntityUsuario(){
        Mockito.when(usuarioService.buscarPorId(Mockito.anyLong())).thenReturn(usuario);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.getById(ID);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getClass(), UsuarioResponseDto.class);
        Assertions.assertEquals(response.getBody().getRole(), "CLIENTE");
        Assertions.assertEquals(response.getBody().getUsername(),USERNAME);

    }
    @Test
    void whenUpdatePassowrdThenReturnNoContent(){
       Mockito.when(usuarioService.editarSenha
               (Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(usuario);

        ResponseEntity<Void> response = usuarioController.updatePassword(ID,usuarioSenhaDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.NO_CONTENT);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
    }
    @Test
    void whenGetAllThenReturnListOfUsuariosResponseEntity(){
        Mockito.when(usuarioService.buscarTodos()).thenReturn(usuarioList);

        ResponseEntity<List<UsuarioResponseDto>> response = usuarioController.getAll();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBody().get(0).getClass(),UsuarioResponseDto.class);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getBody().get(0).getId(),ID);
        Assertions.assertEquals(response.getBody().get(0).getRole(),"CLIENTE");
        Assertions.assertEquals(response.getBody().get(0).getUsername(),USERNAME);
    }
    void startUsuario(){
        this.usuario = new Usuario(ID,USERNAME,PASSWORD,DATA_CRIACAO,
                DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR, Usuario.Role.ROLE_CLIENTE);

        this.usuarioCreateDto = new UsuarioCreateDto(USERNAME,PASSWORD);
        this.usuarioSenhaDto = new UsuarioSenhaDto("123456","123456","123456");

        this.usuarioList = new ArrayList<>();
        this.usuarioList.add(usuario);
    }

}
