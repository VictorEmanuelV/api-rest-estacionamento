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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ClienteVaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_recibo",nullable = false,unique = true,length = 15)
    private String recibo;

    //private static final Long ID = 1L;
    //private static final String RECIBO =  "12345678910";
    //private static final String PLACA = "3209";
    //private static final String MARCA = "FORD";
    //private static final String MODELO = "FIESTA";
    //private static final String COR = "AZUL";
    //private static final LocalDateTime DATA_ENTRADA = LocalDateTime.now();
    //private static final LocalDateTime DATA_SAIDA = LocalDateTime.now();
    //private static final BigDecimal VALOR = new BigDecimal(25);
    //private static final BigDecimal DESCONTO = new BigDecimal(5);
    //private static final Cliente CLIENTE = new Cliente();
    //private static final Vaga VAGA = new Vaga();
    @Column(name = "placa",nullable = false,length = 8)
    private String placa;

    @Column(name= "marca",nullable = false,length = 45)
    private String marca;

    @Column(name = "modelo",nullable = false,length = 45)
    private String modelo;

    @Column(name ="cor",nullable = false,length = 45)
    private String cor;

    @Column(name = "data_entrada",nullable = false)
    private LocalDateTime dataEntrada;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    @Column(name = "valor",columnDefinition = "decimal(7,2)")
    private BigDecimal valor;

    @Column(name = "desconto",columnDefinition = "decimal(7,2)")
    private BigDecimal desconto;

    @ManyToOne
    @JoinColumn(name = "id_cliente",nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_vaga")
    private Vaga vaga;

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
        ClienteVaga that = (ClienteVaga) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
