package com.tccmaster.projectccmaster.aplication.repository;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TecnicoRepository extends JpaRepository<TecnicoEntity, UUID> {
}