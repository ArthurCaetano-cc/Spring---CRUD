package com.example.Pessoa.Entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Pessoa {
    @Id
    @Schema(description = "O cpf foi escolhido como o identificador único da pessoa", example = "12345678900")
    private Long cpf;

    @Schema(description = "Atributo que representa o nome da pessoa", example = "Bernardo")
    private String nome;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date data_nascimento;

    public Pessoa(){

    }

    public Pessoa(Long cpf, String nome, Date data_nascimento){
        this.cpf = cpf;
        this.nome = nome;
        this.data_nascimento = data_nascimento;
    }
    
    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    
}
