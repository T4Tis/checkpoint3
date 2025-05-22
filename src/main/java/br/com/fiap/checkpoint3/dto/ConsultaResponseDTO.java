package br.com.fiap.checkpoint3.dto;

import br.com.fiap.checkpoint3.model.StatusConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import java.time.LocalDateTime;


import br.com.fiap.checkpoint3.model.Consulta;

public class ConsultaResponseDTO {
    private Long id;
    private Long profissionalId;
    private Long pacienteId;

    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dataConsulta;

    private StatusConsulta statusConsulta;
    private Integer quantidadeHoras;
    private BigDecimal valorConsulta;

    
    public ConsultaResponseDTO() {}

    
    public ConsultaResponseDTO toDto(Consulta consulta) {
        this.setId(consulta.getId());
        this.setDataConsulta(consulta.getDataConsulta());
        this.setStatusConsulta(consulta.getStatusConsulta());
        this.setQuantidadeHoras(consulta.getQuantidadeHoras());
        this.setValorConsulta(consulta.getValorConsulta());

        
        if (consulta.getProfissional() != null) {
            this.setProfissionalId(consulta.getProfissional().getId());
        }
        if (consulta.getPaciente() != null) {
            this.setPacienteId(consulta.getPaciente().getId());
        }
        return this;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(BigDecimal valorConsulta) {
        this.valorConsulta = valorConsulta;
    }
}