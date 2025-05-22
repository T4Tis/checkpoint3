package br.com.fiap.checkpoint3.service;

import br.com.fiap.checkpoint3.dto.PacienteRequestDTO;
import br.com.fiap.checkpoint3.dto.PacienteResponseDTO;
import br.com.fiap.checkpoint3.model.Paciente;
import br.com.fiap.checkpoint3.repository.PacienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    private PacienteResponseDTO toResponseDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        BeanUtils.copyProperties(paciente, dto);
        return dto;
    }

    private Paciente toModel(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        BeanUtils.copyProperties(dto, paciente);
        return paciente;
    }

    public PacienteResponseDTO save(PacienteRequestDTO pacienteRequestDTO) {
        Paciente paciente = toModel(pacienteRequestDTO);
        Paciente savedPaciente = pacienteRepository.save(paciente);
        return toResponseDTO(savedPaciente);
    }

    public List<PacienteResponseDTO> findAll(String sortOrder) {
        Sort sort = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Direction.ASC, "nome");
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Direction.DESC, "nome");
        }
        return pacienteRepository.findAll(sort).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteResponseDTO> findById(Long id) {
        return pacienteRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public PacienteResponseDTO update(Long id, PacienteRequestDTO pacienteDetailsDTO) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + id));

        BeanUtils.copyProperties(pacienteDetailsDTO, paciente, "id"); 

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        return toResponseDTO(updatedPaciente);
    }

    public void deleteById(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado com id: " + id);
        }
        pacienteRepository.deleteById(id);
    }
}