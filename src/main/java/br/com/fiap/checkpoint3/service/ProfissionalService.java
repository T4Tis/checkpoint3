package br.com.fiap.checkpoint3.service;



import br.com.fiap.checkpoint3.dto.ProfissionalRequestDTO;
import br.com.fiap.checkpoint3.dto.ProfissionalResponseDTO;
import br.com.fiap.checkpoint3.model.Profissional;
import br.com.fiap.checkpoint3.repository.ProfissionalRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    private ProfissionalResponseDTO toResponseDTO(Profissional profissional) {
        ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
        BeanUtils.copyProperties(profissional, dto);
        return dto;
    }

    private Profissional toModel(ProfissionalRequestDTO dto) {
        Profissional profissional = new Profissional();
        BeanUtils.copyProperties(dto, profissional);
        return profissional;
    }

    public ProfissionalResponseDTO save(ProfissionalRequestDTO profissionalRequestDTO) {
        Profissional profissional = toModel(profissionalRequestDTO);
        Profissional savedProfissional = profissionalRepository.save(profissional);
        return toResponseDTO(savedProfissional);
    }

    public List<ProfissionalResponseDTO> findAll(String sortOrder) {
        Sort sort = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Direction.ASC, "nome");
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Direction.DESC, "nome");
        }
        return profissionalRepository.findAll(sort).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfissionalResponseDTO> findById(Long id) {
        return profissionalRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public ProfissionalResponseDTO update(Long id, ProfissionalRequestDTO profissionalDetailsDTO) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com id: " + id));

        BeanUtils.copyProperties(profissionalDetailsDTO, profissional, "id"); 

        Profissional updatedProfissional = profissionalRepository.save(profissional);
        return toResponseDTO(updatedProfissional);
    }

    public void deleteById(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new RuntimeException("Profissional não encontrado com id: " + id);
        }
        profissionalRepository.deleteById(id);
    }
}