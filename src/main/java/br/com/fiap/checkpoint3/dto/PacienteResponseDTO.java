package br.com.fiap.checkpoint3.dto;

import com.fasterxml.jackson.annotation.JsonFormat; // Importar JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape; // Importar Shape
import java.time.LocalDate;

public class PacienteResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String bairro;
    private String email;
    private String telefoneCompleto;

    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy") // Adicionado JsonFormat
    private LocalDate dataNascimento;

    
    public PacienteResponseDTO() {}

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneCompleto() {
        return telefoneCompleto;
    }

    public void setTelefoneCompleto(String telefoneCompleto) {
        this.telefoneCompleto = telefoneCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}