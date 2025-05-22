package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.ConsultaRequestDTO;
import br.com.fiap.checkpoint3.dto.ConsultaResponseDTO;
import br.com.fiap.checkpoint3.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@RequestBody ConsultaRequestDTO consultaRequestDTO) {
        try {
            ConsultaResponseDTO savedConsulta = consultaService.save(consultaRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConsulta); 
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> getAllConsultas(
           @RequestParam(required = false) String status,
           @RequestParam(required = false) String dataDe,
           @RequestParam(required = false)String dataAte) {

        if (status != null && dataDe != null && dataAte != null) {
            List<ConsultaResponseDTO> consultas = consultaService.findConsultasByStatusAndDateRange(status, dataDe, dataAte);
            return ResponseEntity.ok(consultas);
        } else {
            List<ConsultaResponseDTO> consultas = consultaService.findAll();
            return ResponseEntity.ok(consultas);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> getConsultaById(@PathVariable Long id) {
        return consultaService.findById(id)
                .map(consulta -> ResponseEntity.ok(consulta))
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> updateConsulta(@PathVariable Long id, @RequestBody ConsultaRequestDTO consultaDetailsDTO) {
        try {
            ConsultaResponseDTO updatedConsulta = consultaService.update(id, consultaDetailsDTO);
            return ResponseEntity.ok(updatedConsulta); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        try {
            consultaService.deleteById(id);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
    }
}