package com.mballen.demoparkapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDto {
    private Long id;

    private String nome;

    private String cpf;
}
