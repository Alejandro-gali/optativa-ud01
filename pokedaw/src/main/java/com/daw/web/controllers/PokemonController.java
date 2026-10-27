package com.daw.web.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.services.PokemonService;
import com.daw.services.exceptions.PokemonException;
import com.daw.services.exceptions.PokemonNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pokemons")
@RequiredArgsConstructor
public class PokemonController {

	private final PokemonService pokemonService;

	@GetMapping
	public ResponseEntity<List<PokemonEntity>> list() {
		return ResponseEntity.ok(this.pokemonService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable long id) {
		try {
			return ResponseEntity.ok(this.pokemonService.findById(id));
		} catch (PokemonNotFoundException ae) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ae.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody PokemonEntity pokemonEntity) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.pokemonService.create(pokemonEntity));
		} catch (PokemonException ae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ae.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody PokemonEntity pokemonEntity) {
		try {
			return ResponseEntity.ok(this.pokemonService.update(id, pokemonEntity));
		} catch (PokemonNotFoundException ae) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ae.getMessage());
		} catch (PokemonException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable long id) {
		try {
			this.pokemonService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (PokemonNotFoundException ae) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ae.getMessage());
		}
	}

	@GetMapping("/pokedex")
	public ResponseEntity<?> findByNumeroPokedex(@RequestParam int numeroPokedex) {

		List<PokemonEntity> lista = this.pokemonService.findByNumeroPokedex(numeroPokedex);

		if (lista.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No se encontró ningún pokemon con el número de Pokédex: " + numeroPokedex + ".");
		}

		return ResponseEntity.ok(lista);
	}

	@GetMapping("/fecha")
	public ResponseEntity<?> findByFechaCaptura(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

		try {
			List<PokemonEntity> lista = this.pokemonService.findByFechaCaptura(desde, hasta);

			if (lista.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No se encontraron pokemon capturados entre " + desde + " y " + hasta + ".");
			}

			return ResponseEntity.ok(lista);

		} catch (PokemonException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}

	@GetMapping("/tipo")
	public ResponseEntity<?> findByTipo(@RequestParam String tipo) {
		try {
			return ResponseEntity.ok(this.pokemonService.findByTipo(tipo));
		} catch (PokemonException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El tipo de indicado no existe. ");
		}
	}
}
