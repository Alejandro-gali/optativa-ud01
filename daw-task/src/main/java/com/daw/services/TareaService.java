package com.daw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.TareaEntity;
import com.daw.persistence.repositories.TareaRepository;

@Service
public class TareaService {
	
	@Autowired
	private TareaRepository tareaRepository;
	
	public List<TareaEntity> findAll(){
		return this.tareaRepository.findAll();
	}
	
	public TareaEntity findById(long idTarea) {
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("La tarea con el id " + idTarea + " no existe.");
		}
		
		return this.tareaRepository.findById(idTarea).get();
	}
}
