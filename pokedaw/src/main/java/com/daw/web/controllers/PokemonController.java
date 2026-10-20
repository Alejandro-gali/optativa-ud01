package com.daw.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.services.PokemonService;
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
}
