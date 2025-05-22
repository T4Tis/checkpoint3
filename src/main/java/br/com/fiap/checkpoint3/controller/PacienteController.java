package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.PacienteRequestDTO;
import br.com.fiap.checkpoint3.dto.PacienteResponseDTO;
import br.com.fiap.checkpoint3.dto.ConsultaResponseDTO; 
import br.com.fiap.checkpoint3.service.PacienteService;
import br.com.fiap.checkpoint3.service.ConsultaService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService; 

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> createPaciente(@RequestBody PacienteRequestDTO pacienteRequestDTO) {
        PacienteResponseDTO savedPaciente = pacienteService.save(pacienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaciente);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAllPacientes(
            @RequestParam(required = false) String sort) {
        List<PacienteResponseDTO> pacientes = pacienteService.findAll(sort);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        return pacienteService.findById(id)
                .map(paciente -> ResponseEntity.ok(paciente))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(@PathVariable Long id, @RequestBody PacienteRequestDTO pacienteDetailsDTO) {
        try {
            PacienteResponseDTO updatedPaciente = pacienteService.update(id, pacienteDetailsDTO);
            return ResponseEntity.ok(updatedPaciente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        try {
            pacienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByPacienteId(
            @PathVariable Long id,
      
            String status,
            String dataDe,
            String dataAte) {

        if (status != null && !status.isEmpty() && dataDe != null && !dataDe.isEmpty() && dataAte != null && !dataAte.isEmpty()) {
            try {
                List<ConsultaResponseDTO> consultas = consultaService.findConsultasByPacienteIdAndStatusAndDateRange(id, status, dataDe, dataAte);
                return ResponseEntity.ok(consultas);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            List<ConsultaResponseDTO> consultas = consultaService.findConsultasByPacienteId(id);
            return ResponseEntity.ok(consultas);
        }
    }
}
