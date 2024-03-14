package com.mballen.demoparkapi.entity;

import jakarta.persistence.*;
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

@Getter @Setter @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "usuarios")
@Entity
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username",nullable = false,unique = true,length = 100)
    private String username;

    @Column(name = "password",nullable = false,length = 200)
    private String password;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false,length = 25)
    private Role role = Role.ROLE_CLIENTE;
    public enum Role{
        ROLE_ADMIN,ROLE_CLIENTE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuarios = (Usuario) o;
        return Objects.equals(id, usuarios.id) && Objects.equals(username, usuarios.username) && Objects.equals(password, usuarios.password) && Objects.equals(criadoPor, usuarios.criadoPor) && Objects.equals(modificadoPor, usuarios.modificadoPor) && role == usuarios.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                '}';
    }
}
