package com.daw.services;

import java.time.LocalDate;
import java.util.List;

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

	public List<TareaEntity> findAll() {
		return this.tareaRepository.findAll();
	}

	public TareaEntity findById(long idTarea) {
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("La tarea con el id " + idTarea + " no existe.");
		}

		return this.tareaRepository.findById(idTarea).get();
	}

	public void deleteById(long idTarea) {
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("La tarea con el id " + idTarea + " no existe.");
		}

		this.tareaRepository.deleteById(idTarea);
	}

	public TareaEntity create(TareaEntity tareaEntity) {
		if (tareaEntity.getFechaCreacion() != null) {
			throw new TareaException("La fecha de creación no se puede modificar. ");
		}
		if (tareaEntity.getEstado() != null) {
			throw new TareaException("El estado no se puede modificar. ");
		}
		if (tareaEntity.getFechaVencimiento().isBefore(LocalDate.now())) {
			throw new TareaException("La fecha de vencimiento debe ser posterior a la fecha de creación.");
		}

		// La tarea debe llevar un id que no existe
		tareaEntity.setId(0L);
		tareaEntity.setEstado(Estado.PENDIENTE);
		tareaEntity.setFechaCreacion(LocalDate.now());

		return this.tareaRepository.save(tareaEntity);
	}

	public TareaEntity update(long idTarea, TareaEntity tareaEntity) {
		if (tareaEntity.getId() != idTarea) {
			throw new TareaException("El id del body y el id del path no coinciden. ");
		}
		if (tareaEntity.getFechaCreacion() != null) {
			throw new TareaException("La fecha de creación no se puede modificar. ");
		}
		if (tareaEntity.getEstado() != null) {
			throw new TareaException("El estado no se puede modificar. ");
		}
		if (tareaEntity.getFechaVencimiento().isBefore(LocalDate.now())) {
			throw new TareaException("La fecha de vencimiento debe ser posterior a la fecha de creación.");
		}

		TareaEntity tareaBD = this.findById(idTarea);

		tareaBD.setTitulo(tareaEntity.getTitulo());
		tareaBD.setDescripcion(tareaEntity.getDescripcion());
		tareaBD.setFechaVencimiento(tareaEntity.getFechaVencimiento());

		return this.tareaRepository.save(tareaBD);
	}

	public TareaEntity iniciar(long idTarea) {
		TareaEntity tarea = this.findById(idTarea);

		if (!tarea.getEstado().equals(Estado.PENDIENTE)) {
			throw new TareaException("No se puede iniciar una tarea en progreso o completada.");
		}

		tarea.setEstado(Estado.EN_PROGRESO);
		return this.tareaRepository.save(tarea);

	}

	public TareaEntity completar(long idTarea) {
		TareaEntity tarea = this.findById(idTarea);

		if (!tarea.getEstado().equals(Estado.EN_PROGRESO)) {
			throw new TareaException("No se puede completar una tarea que no está en progreso.");
		}

		tarea.setEstado(Estado.COMPLETADA);
		return this.tareaRepository.save(tarea);
	}

	public List<TareaEntity> findPendientes() {
		return this.tareaRepository.findByEstado(Estado.PENDIENTE);
	}

	public List<TareaEntity> findEnProgreso() {
		return this.tareaRepository.findByEstado(Estado.EN_PROGRESO);
	}

	public List<TareaEntity> findCompletadas() {
		return this.tareaRepository.findByEstado(Estado.COMPLETADA);
	}

	public List<TareaEntity> findVencidas() {
		return this.tareaRepository.findByFechaVencimientoBefore(LocalDate.now());
	}

	public List<TareaEntity> findNoVencidas() {
		// Se usa "minusDays(1)" para que actúe como un mayor o igual que hoy de forma
		// inclusiva
		return this.tareaRepository.findByFechaVencimientoAfter(LocalDate.now().minusDays(1));
	}

	public List<TareaEntity> findByTituloContaining(String titulo) {
		// Validamos primero que el parámetro no venga vacío o con espacios para evitar
		// búsquedas raras
		if (titulo == null || titulo.trim().isEmpty()) {
			throw new TareaException("El título no puede estar vacío. ");
		}
		return this.tareaRepository.findByTituloContainingIgnoreCase(titulo);
	}
}