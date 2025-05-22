package br.com.fiap.checkpoint3.service;

import br.com.fiap.checkpoint3.dto.ConsultaRequestDTO;
import br.com.fiap.checkpoint3.dto.ConsultaResponseDTO;
import br.com.fiap.checkpoint3.model.Consulta;
import br.com.fiap.checkpoint3.model.Paciente;
import br.com.fiap.checkpoint3.model.Profissional;
import br.com.fiap.checkpoint3.model.StatusConsulta;
import br.com.fiap.checkpoint3.repository.ConsultaRepository;
import br.com.fiap.checkpoint3.repository.PacienteRepository;
import br.com.fiap.checkpoint3.repository.ProfissionalRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    private Consulta toModel(ConsultaRequestDTO dto) {
        Consulta consulta = new Consulta();
        BeanUtils.copyProperties(dto, consulta);

        if (dto.getProfissionalId() != null) {
            Profissional profissional = profissionalRepository.findById(dto.getProfissionalId())
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado com id: " + dto.getProfissionalId()));
            consulta.setProfissional(profissional);
        }

   
        if (dto.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getPacienteId()));
            consulta.setPaciente(paciente);
        }

     
        if (consulta.getProfissional() != null && consulta.getQuantidadeHoras() != null) {
            BigDecimal valorHora = BigDecimal.valueOf(consulta.getProfissional().getValorHora());
            BigDecimal quantidadeHoras = BigDecimal.valueOf(consulta.getQuantidadeHoras());
            consulta.setValorConsulta(valorHora.multiply(quantidadeHoras));
        } else {
            consulta.setValorConsulta(BigDecimal.ZERO);
        }

        return consulta;
    }

    public ConsultaResponseDTO save(ConsultaRequestDTO consultaRequestDTO) {
        if (isProfissionalAvailable(consultaRequestDTO.getProfissionalId(), consultaRequestDTO.getDataConsulta())) {
            Consulta consulta = toModel(consultaRequestDTO);
            Consulta savedConsulta = consultaRepository.save(consulta);
            return new ConsultaResponseDTO().toDto(savedConsulta);
        } else {
            throw new RuntimeException("Profissional já tem uma consulta agendada neste horário.");
        }
    }

    private boolean isProfissionalAvailable(Long profissionalId, LocalDateTime dataConsulta) {
        List<Consulta> consultasAgendadas = consultaRepository.findByProfissionalIdAndStatusConsulta(profissionalId, StatusConsulta.AGENDADA);

        for (Consulta c : consultasAgendadas) {
            if (c.getDataConsulta().isEqual(dataConsulta) ||
                (dataConsulta.isAfter(c.getDataConsulta()) && dataConsulta.isBefore(c.getDataConsulta().plusHours(c.getQuantidadeHoras()))) ||
                (dataConsulta.plusHours(c.getQuantidadeHoras()).isAfter(c.getDataConsulta()) && dataConsulta.plusHours(c.getQuantidadeHoras())
                .isBefore(c.getDataConsulta().plusHours(c.getQuantidadeHoras())))) {
                return false;
            }
        }
        return true;
    }

    public List<ConsultaResponseDTO> findAll() {
        return consultaRepository.findAll().stream()
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta))
                .collect(Collectors.toList());
    }

    public Optional<ConsultaResponseDTO> findById(Long id) {
        return consultaRepository.findById(id)
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta));
    }

    public ConsultaResponseDTO update(Long id, ConsultaRequestDTO consultaDetailsDTO) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com id: " + id));

        Profissional profissional = profissionalRepository.findById(consultaDetailsDTO.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com id: " + consultaDetailsDTO.getProfissionalId()));
        
        Paciente paciente = pacienteRepository.findById(consultaDetailsDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + consultaDetailsDTO.getPacienteId()));

        consulta.setProfissional(profissional);
        consulta.setPaciente(paciente);
        
        BeanUtils.copyProperties(consultaDetailsDTO, consulta, "id", "profissionalId", "pacienteId");

        if (consulta.getProfissional() != null && consulta.getQuantidadeHoras() != null) {
            BigDecimal valorHora = BigDecimal.valueOf(consulta.getProfissional().getValorHora());
            BigDecimal quantidadeHoras = BigDecimal.valueOf(consulta.getQuantidadeHoras());
            consulta.setValorConsulta(valorHora.multiply(quantidadeHoras));
        } else {
            consulta.setValorConsulta(BigDecimal.ZERO);
        }

        Consulta updatedConsulta = consultaRepository.save(consulta);
        return new ConsultaResponseDTO().toDto(updatedConsulta);
    }

    public void deleteById(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new RuntimeException("Consulta não encontrada com id: " + id);
        }
        consultaRepository.deleteById(id);
    }

    
    public List<ConsultaResponseDTO> findConsultasByStatusAndDateRange(String status, String dataDe, String dataAte) {
        StatusConsulta statusEnum = StatusConsulta.valueOf(status.toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(dataDe + " 00:00", formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(dataAte + " 23:59", formatter);

        return consultaRepository.findConsultasByStatusAndDateRange(statusEnum, startDateTime, endDateTime).stream()
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta))
                .collect(Collectors.toList());
    }

    
    public List<ConsultaResponseDTO> findConsultasByPacienteIdAndStatusAndDateRange(Long pacienteId, String status, String dataDe, String dataAte) {
        StatusConsulta statusEnum = StatusConsulta.valueOf(status.toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(dataDe + " 00:00", formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(dataAte + " 23:59", formatter);

        return consultaRepository.findConsultasByPacienteIdAndStatusAndDateRange(pacienteId, statusEnum, startDateTime, endDateTime).stream()
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta))
                .collect(Collectors.toList());
    }

   
    public List<ConsultaResponseDTO> findConsultasByPacienteId(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId).stream()
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta))
                .collect(Collectors.toList());
    }


    
    public List<ConsultaResponseDTO> findConsultasByProfissionalIdAndStatusAndDateRange(Long profissionalId, String status, String dataDe, String dataAte) {
        StatusConsulta statusEnum = StatusConsulta.valueOf(status.toUpperCase());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(dataDe + " 00:00", formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(dataAte + " 23:59", formatter);

        return consultaRepository.findConsultasByProfissionalIdAndStatusAndDateRange(profissionalId, statusEnum, startDateTime, endDateTime).stream()
                .map(consulta -> new ConsultaResponseDTO().toDto(consulta))
                .collect(Collectors.toList());
    }

   
    public List<Consulta> findConsultasByProfissionalId(Long profissionalId) {
        return consultaRepository.findByProfissionalId(profissionalId);
    }
}
