package br.com.fiap.checkpoint3.repository;

import br.com.fiap.checkpoint3.model.Consulta;
import br.com.fiap.checkpoint3.model.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query; 
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

  
    @Query("SELECT c FROM Consulta c WHERE c.statusConsulta = ?1 AND c.dataConsulta BETWEEN ?2 AND ?3")
    List<Consulta> findConsultasByStatusAndDateRange(
            StatusConsulta status,
            LocalDateTime dataDe,
            LocalDateTime dataAte);

   
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = ?1 AND c.statusConsulta = ?2 AND c.dataConsulta BETWEEN ?3 AND ?4")
    List<Consulta> findConsultasByPacienteIdAndStatusAndDateRange(
            Long pacienteId,
            StatusConsulta status,
            LocalDateTime dataDe,
            LocalDateTime dataAte);

    
    @Query("SELECT c FROM Consulta c WHERE c.profissional.id = ?1 AND c.statusConsulta = ?2 AND c.dataConsulta BETWEEN ?3 AND ?4")
    List<Consulta> findConsultasByProfissionalIdAndStatusAndDateRange(
            Long profissionalId,
            StatusConsulta status,
            LocalDateTime dataDe,
            LocalDateTime dataAte);

  
    List<Consulta> findByPacienteId(Long pacienteId);

   
    List<Consulta> findByProfissionalId(Long profissionalId);

    
    List<Consulta> findByProfissionalIdAndStatusConsulta(Long profissionalId, StatusConsulta statusConsulta);
}
