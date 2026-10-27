package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.persistence.entities.enums.Capturado;
import com.daw.persistence.entities.enums.Tipo;
import com.daw.persistence.repositories.PokemonRepository;
import com.daw.services.exceptions.PokemonException;
import com.daw.services.exceptions.PokemonNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PokemonService {

	private final PokemonRepository pokemonRepository;

	// Obtener todos los pokemon
	public List<PokemonEntity> findAll() {
		return this.pokemonRepository.findAll();
	}

	// Obtener un pokemon por su id
	public PokemonEntity findById(long id) {
		if (!this.pokemonRepository.existsById(id)) {
			throw new PokemonNotFoundException("El pokemon con el id " + id + " no se encuentra.");
		}

		return this.pokemonRepository.findById(id).get();
	}

	// Post de pokemon
	public PokemonEntity create(PokemonEntity pokemonEntity) {
		validarTipos(pokemonEntity);

		if (pokemonEntity.getCapturado() == null) {
			pokemonEntity.setCapturado(Capturado.POKEBALL);
		}

		return this.pokemonRepository.save(pokemonEntity);
	}

	// Put de pokemon
	public PokemonEntity update(long id, PokemonEntity pokemonEntity) {
		validarTipos(pokemonEntity);

		if (pokemonEntity.getId() != id) {
			throw new PokemonException("El id del cuerpo y el del path no coinciden.");
		}
		if (pokemonEntity.getFechaCaptura() != null) {
			throw new PokemonException("La fecha de captura no se puede modificar.");
		}

		PokemonEntity existente = findById(id);

		existente.setNumeroPokedex(pokemonEntity.getNumeroPokedex());
		existente.setTitulo(pokemonEntity.getTitulo());
		existente.setTipo1(pokemonEntity.getTipo1());
		existente.setTipo1(pokemonEntity.getTipo2());
		existente.setCapturado(pokemonEntity.getCapturado());

		return this.pokemonRepository.save(existente);
	}

	// Borrar un pokemon
	public void deleteById(long id) {
		if (!this.pokemonRepository.existsById(id)) {
			throw new PokemonNotFoundException("No existe el pokemon con el id: " + id);
		}

		this.pokemonRepository.deleteById(id);
	}

	// Buscar un pokemon según su número de pokedex
	public List<PokemonEntity> findByNumeroPokedex(int numeroPokedex) {
		return this.pokemonRepository.findByNumeroPokedex(numeroPokedex);
	}

	// Buscar por un rango de fechas de captura
	public List<PokemonEntity> findByFechaCaptura(LocalDate desde, LocalDate hasta) {

		if (desde == null || hasta == null || desde.isAfter(hasta)) {
			throw new PokemonException("El rango de fechas no es válido.");
		}

		return this.pokemonRepository.findByFechaCapturaBetween(desde, hasta);
	}

	// Buscar un pokemon de un tipo determinado
	public List<PokemonEntity> findByTipo(String tipo) {
		Tipo tipoEnum = Tipo.valueOf(tipo);
		return this.pokemonRepository.findByTipo1OrTipo2(tipoEnum, tipoEnum);
	}

	// Validar que el primer tipo sea obligatorio y no sea ninguno
	private void validarTipos(PokemonEntity pokemon) {
		Tipo tipo1 = pokemon.getTipo1();
		Tipo tipo2 = pokemon.getTipo2();

		if (tipo1 == null || tipo1 == Tipo.NINGUNO) {
			throw new PokemonException("El tipo1 es obligatorio y no puede ser NINGUNO.");
		}

		if (tipo2 == null || tipo2 == Tipo.NINGUNO || tipo1 == tipo2) {
			pokemon.setTipo2(Tipo.NINGUNO);
		}
	}
}
