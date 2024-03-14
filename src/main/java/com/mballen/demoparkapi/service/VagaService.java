package com.mballen.demoparkapi.service;

import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.exception.CodigoUniqueViolationException;
import com.mballen.demoparkapi.exception.EntityNotFoundException;
import com.mballen.demoparkapi.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mballen.demoparkapi.entity.Vaga.StatusVaga.LIVRE;

@RequiredArgsConstructor
@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga){
        try {
            return vagaRepository.save(vaga);
        }catch (DataIntegrityViolationException ex){
            throw new CodigoUniqueViolationException(String.format("Vaga com codigo '%s' ja cadastrada",vaga.getCodigo()));
        }
    }
    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo){
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Vaga com codigo '%s' não foi encontrada",codigo))
        );
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorVagaLivre() {
        return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(
                ()-> new EntityNotFoundException("Nenhuma vaga Livre foi encontrada")
        );
    }
}
