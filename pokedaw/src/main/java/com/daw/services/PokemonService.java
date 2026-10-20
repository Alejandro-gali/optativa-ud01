package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.persistence.repositories.PokemonRepository;
import com.daw.services.exceptions.PokemonNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PokemonService {

	private final PokemonRepository pokemonRepository;
	
	public List<PokemonEntity> findAll(){
		return this.pokemonRepository.findAll();
	}
	
	public PokemonEntity findById(long id){
		if(!this.pokemonRepository.existsById(id)) {
			throw new PokemonNotFoundException("El pokemon con el id " + id + " no se encuentra.");
		}
		
		return this.pokemonRepository.findById(id).get();
	}
}
