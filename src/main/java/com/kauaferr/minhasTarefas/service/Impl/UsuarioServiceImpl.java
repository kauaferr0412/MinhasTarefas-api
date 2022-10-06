package com.kauaferr.minhasTarefas.service.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kauaferr.minhasTarefas.exceptions.ErroAutenticacao;
import com.kauaferr.minhasTarefas.exceptions.RegraDeNegocioException;
import com.kauaferr.minhasTarefas.model.entity.Usuario;
import com.kauaferr.minhasTarefas.model.repository.UsuarioRepository;
import com.kauaferr.minhasTarefas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements 	UsuarioService{

	private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
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
	
	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return usuarioRepository.findById(id);
	}
}
