package br.com.fiap.checkpoint3.dto;

import br.com.fiap.checkpoint3.model.StatusConsulta;
import com.fasterxml.jackson.annotation.JsonFormat; 
import com.fasterxml.jackson.annotation.JsonFormat.Shape; 
import java.time.LocalDateTime;

public class ConsultaRequestDTO {
    private Long profissionalId;
    private Long pacienteId;

    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm") 
    private LocalDateTime dataConsulta;

    private StatusConsulta statusConsulta;
    private Integer quantidadeHoras;

    
    public ConsultaRequestDTO() {}

   
    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public StatusConsulta getStatusConsulta() {
        return statusConsulta;
    }

    public void setStatusConsulta(StatusConsulta statusConsulta) {
        this.statusConsulta = statusConsulta;
    }

    public Integer getQuantidadeHoras() {
        return quantidadeHoras;
    }

    public void setQuantidadeHoras(Integer quantidadeHoras) {
        this.quantidadeHoras = quantidadeHoras;
    }
}