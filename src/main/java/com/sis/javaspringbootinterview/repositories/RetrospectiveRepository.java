package com.sis.javaspringbootinterview.repositories;

import com.sis.javaspringbootinterview.models.Retrospective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {
}

