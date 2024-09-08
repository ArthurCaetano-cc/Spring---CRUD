package com.example.Pessoa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Pessoa.Entity.Pessoa;

@Repository
public interface PessoaRepository  extends JpaRepository <Pessoa, Long>{

    
} 

