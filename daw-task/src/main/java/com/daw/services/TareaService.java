package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.TareaEntity;
import com.daw.persistence.entities.enums.Estado;
import com.daw.persistence.repositories.TareaRepository;
import com.daw.services.exceptions.TareaException;
import com.daw.services.exceptions.TareaNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {
	
	private final TareaRepository tareaRepository;
	
	public List<TareaEntity> findAll(){
		return this.tareaRepository.findAll();
	}
	
	public TareaEntity findById(long idTarea) {
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("La tarea con el id " + idTarea + " no existe.");
		}
		
		return this.tareaRepository.findById(idTarea).get();
	}
	
	public void deleteById(long idTarea) {
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("La tarea con el id " + idTarea + " no existe.");
		}
		
		this.tareaRepository.deleteById(idTarea);
	}
	
	public TareaEntity create(TareaEntity tareaEntity) {
		if(tareaEntity.getFechaCreacion() != null) {
			throw new TareaException("La fecha de creación no se puede modificar.");
		}
		if(tareaEntity.getEstado() != null) {
			throw new TareaException("El estado de la tarea no se puede modificar.");
		}
		if(tareaEntity.getFechaVencimiendo().isBefore(LocalDate.now())) {
			throw new TareaException("La fecha de vencimiento no puede ser antes que la fecha de ahora mismo.");
		}
		
		// Confirmar que la tarea lleve un id inexistente
		tareaEntity.setId(0L);
		tareaEntity.setEstado(Estado.PENDIENTE);
		tareaEntity.setFechaCreacion(LocalDate.now());
		return this.tareaRepository.save(tareaEntity);
	}

	public TareaEntity update(long idTarea, TareaEntity tareaEntity) {
		if(tareaEntity.getId() != idTarea) {
			throw new TareaException("El id del body y el id del path no coinciden. ");
		}
		if(tareaEntity.getFechaCreacion() != null) {
			throw new TareaException("La fecha de creación no se puede modificar.");
		}
		if(tareaEntity.getEstado() != null) {
			throw new TareaException("El estado de la tarea no se puede modificar.");
		}
		if(tareaEntity.getFechaVencimiendo().isBefore(LocalDate.now())) {
			throw new TareaException("La fecha de vencimiento no puede ser antes que la fecha de ahora mismo.");
		}
		
		return this.tareaRepository.save(tareaEntity);
	}
}