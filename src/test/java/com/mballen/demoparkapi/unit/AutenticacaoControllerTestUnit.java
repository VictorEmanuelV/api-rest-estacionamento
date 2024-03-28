package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.jwt.JwtToken;
import com.mballen.demoparkapi.jwt.JwtUserDetailsService;
import com.mballen.demoparkapi.web.controller.AutenticacaoController;
import com.mballen.demoparkapi.web.dto.UsuarioLoginDto;
import com.mballen.demoparkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoControllerTestUnit {

    @InjectMocks
    private AutenticacaoController autenticacaoController;
    @Mock
    private  JwtUserDetailsService detailsService;
    @Mock
    private  AuthenticationManager authenticationManager;

    @Test
    void whenAutenticarThenReturnTokenWithStatus200(){
        UsuarioLoginDto loginDto = Mockito.mock(UsuarioLoginDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        UsernamePasswordAuthenticationToken authenticationToken =
                Mockito.mock(UsernamePasswordAuthenticationToken.class);

        JwtToken jwtToken = Mockito.mock(JwtToken.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authenticationToken);
        Mockito.when(detailsService.getTokenAuthenticated(Mockito.isNull())).thenReturn(jwtToken);

        ResponseEntity<?> response = autenticacaoController.autenticar(loginDto,request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertTrue(response.getBody() instanceof JwtToken);
    }
    @Test
    void whenAutenticarThenReturnErrorMessageWithStatus400(){
        AuthenticationException authenticationException = Mockito.mock(AuthenticationException.class);
        UsuarioLoginDto usuarioLoginDto = Mockito.mock(UsuarioLoginDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(authenticationException);
        ResponseEntity<?> response = autenticacaoController.autenticar(usuarioLoginDto,request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getClass(), ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(),ErrorMessage.class);
        Assertions.assertFalse(response.getBody() instanceof  JwtToken);



    }

}
