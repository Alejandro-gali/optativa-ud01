package com.daw.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistence.entities.enums.TareaEntity;

public interface TareaRepository extends JpaRepository<TareaEntity, Long>{

}
