package com.mballen.demoparkapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@Entity
@Table(name = "clientes")
@EntityListeners(AuditingEntityListener.class)
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false,length = 100)
    private String nome;

    @Column(name = "cpf",nullable = false,unique = true,length = 11)
    private String cpf;

    @OneToOne
    @JoinColumn(name = "id_usuario",nullable = false)
    private Usuario usuario;
    // private static final Long ID = 1L;
    // private static final String NOME = "Victor emanuel"
    // private static final String CPF = "25945427098";
    // private static final Long ID_USUARIO = 1L;
    // private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now();
    // private static final LocalDateTime DATA_MODIFICACAO = LocalDateTime.now();
    // private static final String CRIADO_POR = "useranonymus";
    // private static final String MODIFICADO_POR = "useranonymus";
    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
