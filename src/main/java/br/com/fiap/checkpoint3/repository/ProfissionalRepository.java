package br.com.fiap.checkpoint3.repository;

import br.com.fiap.checkpoint3.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    List<Profissional> findAll(Sort sort);
}