package com.mballen.demoparkapi;

import com.mballen.demoparkapi.web.dto.VagaCreateDto;
import com.mballen.demoparkapi.web.dto.VagaResponseDto;
import com.mballen.demoparkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarVaga_ComDadosValidos_RetornarLocationComStatus201(){
       testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com","123456"))
                .bodyValue(new VagaCreateDto("A-05","LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);

    }

    @Test
    public void criarVaga_ComCodigoJaExistente_RetornarErrorMessageComStatus409(){

        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-01", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);


    }

    @Test
    public void criarVaga_ComDadosInvalidos_RetornarErrorMessageComStatus422(){

        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-501", "DESOCUPADA"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }


    @Test
    public void buscarVaga_ComCodigoExistente_RetornarVagaComStatus200(){

        VagaResponseDto responseBody = testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VagaResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
        org.assertj.core.api.Assertions.assertThat(responseBody.getCodigo()).isEqualTo("A-01");
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo("LIVRE");

    }

    @Test
    public void buscarVaga_ComCodigoInexistente_RetornarErrorMessageComStatus404(){

        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
        org.assertj.core.api.Assertions.assertThat(responseBody.getMethod()).isEqualTo("GET");
        org.assertj.core.api.Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas/A-10");

    }

    @Test
    public void buscarVaga_ComUsuarioSemPermissaoDeAcesso_RetornarErrorMessageComStatus403(){

        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/vagas/{codigo}","A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        org.assertj.core.api.Assertions.assertThat(responseBody.getMethod()).isEqualTo("GET");
        org.assertj.core.api.Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas/A-01");

    }


    @Test
    public void criarVaga_ComUsuarioSemPermissaoDeAcesso_RetornarErrorMessageComStatus403(){

        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .bodyValue(new VagaCreateDto("A-05","OCUPADA"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        org.assertj.core.api.Assertions.assertThat(responseBody.getMethod()).isEqualTo("POST");
        org.assertj.core.api.Assertions.assertThat(responseBody.getPath()).isEqualTo("/api/v1/vagas");

    }
}
