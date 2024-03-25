package com.mballen.demoparkapi.unit;

import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.exception.CodigoUniqueViolationException;
import com.mballen.demoparkapi.exception.CpfUniqueViolationException;
import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.repository.VagaRepository;
import com.mballen.demoparkapi.service.VagaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class VagaServiceTestUnit {
    private static final Long ID = 1L;
    private static final String CODIGO = "A-01";
    private static final Vaga.StatusVaga STATUS = Vaga.StatusVaga.LIVRE;

    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    private static final String CRIADO_POR = "useranonymus";
    private static final String MODIFICADO_POR = "useranonymus";

    @InjectMocks
    private VagaService vagaService;
    @Mock
    private VagaRepository vagaRepository;
    private Vaga vaga;
    private Optional<Vaga> optionalVaga;
    @BeforeEach
    void setUp(){
        startVaga();;
    }
    @Test
    void whenSalvarThenReturnVaga(){
        Mockito.when(vagaRepository.save(Mockito.any())).thenReturn(vaga);
        Vaga response = vagaService.salvar(vaga);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getCodigo(),CODIGO);
        Assertions.assertEquals(response.getStatus(),STATUS);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);

    }

    @Test
    void whenSalvarThenReturnCodigoUniqueViolationException(){
        Mockito.when(vagaRepository.save(Mockito.any())).thenThrow
                (new CodigoUniqueViolationException(
                        String.format("Vaga com codigo '%s' ja cadastrada",vaga.getCodigo())));

        try {
            Vaga response = vagaService.salvar(vaga);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getClass(), CodigoUniqueViolationException.class);
            Assertions.assertEquals(ex.getMessage(),String.format("Vaga com codigo '%s' ja cadastrada",vaga.getCodigo()));
        }
    }
    @Test
    void whenBuscarPorCodigoThenReturnVaga(){
        Mockito.when(vagaRepository.findByCodigo(Mockito.anyString())).thenReturn(optionalVaga);

        Vaga response = vagaService.buscarPorCodigo(CODIGO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getCodigo(),CODIGO);
        Assertions.assertEquals(response.getStatus(),STATUS);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);

    }
    @Test
    void whenBuscarPorCodigoThenReturnEntityNotFoundException(){
        Mockito.when(vagaRepository.findByCodigo(Mockito.anyString())).thenThrow
                (new EntityNotFoundException
                        (String.format("Vaga com codigo '%s' não foi encontrada",CODIGO)));

        try {
            Vaga response = vagaService.buscarPorCodigo(CODIGO);
        }catch (Exception ex){
            Assertions.assertEquals(ex.getMessage(),String.format("Vaga com codigo '%s' não foi encontrada",CODIGO));
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    void whenBuscarPorVagaLivreThenReturnVaga(){
        Mockito.when(vagaRepository.findFirstByStatus(Mockito.any())).thenReturn(optionalVaga);

        Vaga response = vagaService.buscarPorVagaLivre();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(),ID);
        Assertions.assertEquals(response.getCodigo(),CODIGO);
        Assertions.assertEquals(response.getStatus(),STATUS);
        Assertions.assertEquals(response.getDataCriacao(),DATA_CRIACAO);
        Assertions.assertEquals(response.getDataModificacao(),DATA_MODIFICACAO);
        Assertions.assertEquals(response.getCriadoPor(),CRIADO_POR);
        Assertions.assertEquals(response.getModificadoPor(),MODIFICADO_POR);

    }
    @Test
    void whenBuscarPorVagaLivreThenReturnEntityNotFoundException(){
        Mockito.when(vagaRepository.findFirstByStatus(Mockito.any())).thenThrow
                (new EntityNotFoundException("Nenhuma vaga livre foi encontrada"));

        try {
            Vaga response = vagaService.buscarPorVagaLivre();
        }catch (Exception ex){
            Assertions.assertEquals(ex.getMessage(),"Nenhuma vaga livre foi encontrada");
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
        }
    }

    void startVaga(){
        this.vaga = new Vaga(ID,CODIGO,STATUS,
                DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR);
        this.optionalVaga = Optional.of(new Vaga(ID,CODIGO,STATUS,
                DATA_CRIACAO,DATA_MODIFICACAO,CRIADO_POR,MODIFICADO_POR));
    }


}
