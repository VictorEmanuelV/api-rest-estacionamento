package com.mballen.demoparkapi.unit;
import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.service.VagaService;
import com.mballen.demoparkapi.util.ServletUriBuilderImpl;
import com.mballen.demoparkapi.web.controller.VagaController;
import com.mballen.demoparkapi.web.dto.VagaCreateDto;
import com.mballen.demoparkapi.web.dto.VagaResponseDto;
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
import java.net.URI;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
public class VagaControllerTestUnit {

    private static final Long ID = 1L;
    private static final String CODIGO = "A-01";
    private static final Vaga.StatusVaga STATUS = Vaga.StatusVaga.LIVRE;

    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR = "useranonymus";
    private static final String MODIFICADO_POR = "useranonymus";
    @InjectMocks
    private VagaController vagaController;
    @Mock
    private VagaService vagaService;

    @Mock
    private ServletUriBuilderImpl servletUriBuilder;
    private Vaga vaga;
    private VagaCreateDto vagaCreateDto;
    private URI location;

    @BeforeEach
    void setUp(){
        startVaga();
    }

    @Test
    void whenCreateThenReturnResponseEntityVoid(){
        Mockito.when(vagaService.salvar(Mockito.any())).thenReturn(vaga);
        Mockito.when(servletUriBuilder.build("/{codigo}", "A-01")).thenReturn(location);

        ResponseEntity<Void> response = vagaController.create(vagaCreateDto);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getHeaders().getLocation(),location);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);

    }
    @Test
    void whenGetByCodigoThenReturnVaga(){
        Mockito.when(vagaService.buscarPorCodigo(CODIGO)).thenReturn(vaga);

        ResponseEntity<VagaResponseDto> response = vagaController.getByCodigo(CODIGO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response.getClass(),ResponseEntity.class);
        Assertions.assertEquals(response.getBody().getClass(), VagaResponseDto.class);
        Assertions.assertEquals(response.getBody().getCodigo(),CODIGO);
        Assertions.assertEquals(response.getBody().getId(),ID);
        Assertions.assertEquals(response.getBody().getStatus(),"LIVRE");
    }
    void startVaga(){
        this.vaga = new Vaga(ID,CODIGO,STATUS,
                DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);

        this.location = URI.create("api/v1/vaga");

        this.vagaCreateDto = new VagaCreateDto(CODIGO,STATUS.name());

   }
}
