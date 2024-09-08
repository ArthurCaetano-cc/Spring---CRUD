package com.example.Pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Pessoa.Entity.Pessoa;
import com.example.Pessoa.Repository.PessoaRepository;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class PessoaController {
    @Autowired
	private PessoaRepository pessoaRepository;

    @ResponseBody 
    @GetMapping("/pessoas") 
    @Operation(summary = "Lista pessoas", description = "Lista todas as pessoas que foram cadastradas no banco de dados da API.")
    public List<Pessoa> listar(){
        List<Pessoa> pessoas = pessoaRepository.findAll();

		return pessoas;
    }

    // Requisição para achar uma pessoa especifica
    @ResponseBody
    @Transactional
    @GetMapping("/pessoas/{cpf}")
    @Operation(summary = "Busca uma pessoa pelo cpf", description = "Pesquisa uma determinada pessoa já cadastrada no banco de dados da API a partir do cpf dela.")
    public ResponseEntity<Pessoa> acharPessoa(@PathVariable Long cpf) {
        Pessoa pessoaExistente = pessoaRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o cpf: " + cpf));

        return ResponseEntity.status(HttpStatus.OK).body(pessoaExistente);
    }

    @ResponseBody
    @Transactional
    @PostMapping("/pessoas")
    @Operation(summary = "Cadastrar Pessoa", description = "Cadastra uma nova pessoa no banco de dados. O sistema fará uma verificação antes de cadastrar: caso o cpf já esteja presente no banco de dados, irá ocorrer um erro de conflito. Caso o contrário, a pessoa será cadastrada.")
	public ResponseEntity<String> salvar(@RequestBody Pessoa pessoa) {
       long cpf = pessoa.getCpf();
       boolean cpfExistente = pessoaRepository.existsById(cpf);
       if(cpfExistente){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pessoa já cadastrada com o CPF: " + cpf);
       } else{
        pessoaRepository.save(pessoa);
        return ResponseEntity.status(HttpStatus.OK).body("Pessoa cadastrada com sucesso.");
       }
	}

    @ResponseBody
    @Transactional
    @PutMapping("/pessoas/{cpf}")
    @Operation(summary = "Atualizar Pessoa", description = "Atualiza as informações de uma pessoa a partir do cpf fornecido. Caso o sistema não ache o cpf, um erro irá acontecer. Uma característica importante desse endpoint é que ele não permitirá a alteração do cpf da pessoa, para evitar que seja possível cadastrar cpfs duplicados no banco de dados. Dessa forma, o usuário pode fornecer ou não o cpf, mas mesmo assim essa informação não será alterada")
    public ResponseEntity<String> atualizar(@PathVariable Long cpf, @RequestBody Pessoa pessoa) {
        boolean cpfExistente = pessoaRepository.existsById(cpf);

        if (!cpfExistente) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF não encontrado");
        } else {
            Pessoa pessoaExistente = pessoaRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o CPF: " + cpf));

            // Atualiza os campos, exceto o CPF
            pessoaExistente.setNome(pessoa.getNome());
            pessoaExistente.setData_nascimento(pessoa.getData_nascimento());
            

            pessoaRepository.save(pessoaExistente);

            return ResponseEntity.status(HttpStatus.OK).body("Pessoa atualizada com sucesso!");
        }
    }

    //Requisição do tipo DELETE

    @ResponseBody
    @Transactional
    @DeleteMapping("/pessoas/{cpf}")
    @Operation(summary = "Deletar Pessoa", description = "Deleta uma determinada pessoa no banco de dados. O sistema faz a busca dessa pessoa a partir do cpf fornecido")
    public ResponseEntity<Pessoa> deletar(@PathVariable Long cpf) {
        Pessoa pessoaExistente = pessoaRepository.findById(cpf)
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o cpf: " + cpf));
        
        pessoaRepository.delete(pessoaExistente);
        return ResponseEntity.status(HttpStatus.OK).body(pessoaExistente);
    }
}
