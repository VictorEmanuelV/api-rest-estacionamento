package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.exception.PasswordInvalidException;
import com.mballen.demoparkapi.exception.UsernameUniqueViolationException;
import com.mballen.demoparkapi.web.exception.ApiExceptionHandler;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
public class ApiExceptionHandlerTestUnit {
    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @Mock
    private BindingResult result;

    @Test
    void whenAccessDeniedExceptionThenReturnErrorMessageWithStatus403(){
        AccessDeniedException ex = Mockito.mock(AccessDeniedException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<ErrorMessage> response = apiExceptionHandler.accessDeniedException(ex,request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(), ErrorMessage.class);
        Assertions.assertEquals(response.getBody().getStatus(),403);
        Assertions.assertEquals(response.getBody().getStatusText(),"Forbidden");
    }
    @Test
    void whenMethodArgumentsNotValidExceptionThenReturnErroMessageWithStatus422(){
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<ErrorMessage> response = apiExceptionHandler.methodArgumentNotValidException(ex,request,result);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.UNPROCESSABLE_ENTITY);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(),ErrorMessage.class);
        Assertions.assertEquals(response.getBody().getStatus(),422);
        Assertions.assertEquals(response.getBody().getStatusText(),"Unprocessable Entity");
    }
    @Test
    void whenUniqueViolationExceptionThenReturnErrorMessageWithStatus409(){
        UsernameUniqueViolationException ex = Mockito.mock(UsernameUniqueViolationException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<ErrorMessage>response = apiExceptionHandler.uniqueViolationException(ex,request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.CONFLICT);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(), ErrorMessage.class);
        Assertions.assertEquals(response.getBody().getStatus(),409);
        Assertions.assertEquals(response.getBody().getStatusText(),"Conflict");

    }
    @Test
    void whenEntityNotFoundExceptionThenReturnErrorMessageWithStatus404(){
        EntityNotFoundException ex = Mockito.mock(EntityNotFoundException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<ErrorMessage> response = apiExceptionHandler.entityNotFoundException(ex,request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(),ErrorMessage.class);
        Assertions.assertEquals(response.getBody().getStatus(),404);
        Assertions.assertEquals(response.getBody().getStatusText(),"Not Found");
    }
    @Test
    void whenPasswordInvalidExceptionThenReturnErrorMessageWithStatus400(){
        PasswordInvalidException ex = Mockito.mock(PasswordInvalidException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<ErrorMessage>response = apiExceptionHandler.passwordInvalidException(ex,request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(),ErrorMessage.class);
        Assertions.assertEquals(response.getBody().getStatus(),400);
        Assertions.assertEquals(response.getBody().getStatusText(),"Bad Request");
    }



}
