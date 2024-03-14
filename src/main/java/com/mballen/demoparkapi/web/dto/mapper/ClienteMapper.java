package com.mballen.demoparkapi.web.dto.mapper;

import com.mballen.demoparkapi.entity.Cliente;
import com.mballen.demoparkapi.web.dto.ClienteCreateDto;
import com.mballen.demoparkapi.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto){
        return new ModelMapper().map(dto, Cliente.class);
    }
    public static ClienteResponseDto toDto(Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }


}
