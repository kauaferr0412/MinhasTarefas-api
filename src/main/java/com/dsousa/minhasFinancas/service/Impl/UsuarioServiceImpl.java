package com.dsousa.minhasFinancas.service.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsousa.minhasFinancas.exceptions.ErroAutenticacao;
import com.dsousa.minhasFinancas.exceptions.RegraDeNegocioException;
import com.dsousa.minhasFinancas.model.entity.Usuario;
import com.dsousa.minhasFinancas.model.repository.UsuarioRepository;
import com.dsousa.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements 	UsuarioService{

	private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {

		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Não foi encontrado um usuário com o e-mail digitado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");

		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);
	}
	
	@Override
	public void validarEmail(String email){
		boolean existeUsuario = usuarioRepository.existsByEmail(email);
		if(existeUsuario) {
			throw new RegraDeNegocioException("Já existe um usuário com este e-mail");
		}
	}
}
