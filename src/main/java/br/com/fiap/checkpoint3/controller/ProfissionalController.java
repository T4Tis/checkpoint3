package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.ProfissionalRequestDTO;
import br.com.fiap.checkpoint3.dto.ProfissionalResponseDTO;
import br.com.fiap.checkpoint3.dto.ConsultaResponseDTO;
import br.com.fiap.checkpoint3.service.ProfissionalService;
import br.com.fiap.checkpoint3.service.ConsultaService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> createProfissional(@RequestBody ProfissionalRequestDTO profissionalRequestDTO) {
        ProfissionalResponseDTO savedProfissional = profissionalService.save(profissionalRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfissional);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> getAllProfissionais(@RequestParam(required = false) String sort) {
        List<ProfissionalResponseDTO> profissionais = profissionalService.findAll(sort);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> getProfissionalById(@PathVariable Long id) {
        return profissionalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> updateProfissional(@PathVariable Long id, @RequestBody ProfissionalRequestDTO profissionalDetailsDTO) {
        try {
            ProfissionalResponseDTO updatedProfissional = profissionalService.update(id, profissionalDetailsDTO);
            return ResponseEntity.ok(updatedProfissional);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfissional(@PathVariable Long id) {
        try {
            profissionalService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getProfissionalStats(@PathVariable Long id) {
        return profissionalService.findById(id)
                .map(profissionalDTO -> {
                    List<br.com.fiap.checkpoint3.model.Consulta> consultas = consultaService.findConsultasByProfissionalId(id);
                    int totalHoras = consultas.stream()
                            .mapToInt(br.com.fiap.checkpoint3.model.Consulta::getQuantidadeHoras)
                            .sum();
                    BigDecimal valorTotal = consultas.stream()
                            .map(br.com.fiap.checkpoint3.model.Consulta::getValorConsulta)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    
                    return ResponseEntity.ok(Map.of(
                            "totalHoras", (Object) totalHoras, 
                            "valorTotalConsultas", (Object) valorTotal 
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByProfissionalId(
            @PathVariable Long id,
            String status,
            String dataDe,
            String dataAte) {

        List<ConsultaResponseDTO> consultas = consultaService.findConsultasByProfissionalIdAndStatusAndDateRange(id, status, dataDe, dataAte);
        return ResponseEntity.ok(consultas);
    }
}