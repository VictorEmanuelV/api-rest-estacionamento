package com.mballen.demoparkapi.web.controller;

import com.mballen.demoparkapi.entity.Vaga;
import com.mballen.demoparkapi.service.VagaService;
import com.mballen.demoparkapi.util.ServletUriBuilderImpl;
import com.mballen.demoparkapi.util.UriBuilder;
import com.mballen.demoparkapi.web.dto.VagaCreateDto;
import com.mballen.demoparkapi.web.dto.VagaResponseDto;
import com.mballen.demoparkapi.web.dto.mapper.VagaMapper;
import com.mballen.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Vagas",description = "Contem todas as operações relativas ao recurso de uma vaga")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    private final UriBuilder uriBuilder;

    @Operation(summary = "Criar uma nova vaga",description = "Recurso para criar uma nova vaga ." +
            "Requisição exige uso de um bearer token. Acesso restro a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION,description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409",description = "Vaga ja cadastrada",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",description = "Recurso não processado por falta de dados ou dados invalidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403",description = "Vaga não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>create(@RequestBody @Valid VagaCreateDto dto){
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);

        URI location = uriBuilder.build("/{codigo}", vaga.getCodigo());

        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "Localizar uma vaga",description = "Recurso para retornar uma vaga pelo seu codigo" +
            "Requisição exige uso de um bearer token. Acesso restro a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = VagaResponseDto.class))),
                    @ApiResponse(responseCode = "404",description = "Vaga não localizada",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403",description = "Vaga não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto>getByCodigo(@PathVariable String codigo){
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        vagaService.salvar(vaga);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
