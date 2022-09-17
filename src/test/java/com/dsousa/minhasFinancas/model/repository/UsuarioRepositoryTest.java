package com.dsousa.minhasFinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minhasFinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository userRepository;
	
	@Autowired
	private TestEntityManager em;
	
	@Test
	public void deveVerificarSeExisteUsuarioNaBasePeloEmail() {
		Usuario user = Usuario.builder().nome("userTEST").email("user@gmail.com").senha("user").build();
		em.persist(user);
		boolean existeUsuario = userRepository.existsByEmail("user@gmail.com");
		Assertions.assertThat(existeUsuario).isTrue();
	}
	
	@Test
	public void deveVerificarSeNaoExisteUsuarioNoBanco() {
		boolean existeUsuario = userRepository.existsByEmail("user@gmail.com");
		Assertions.assertThat(existeUsuario).isFalse();
	}
	
	@Test
	public void devePeristirUmUsuarioNoBanco() {
		Usuario user = userRepository.save(criarUser());
		Assertions.assertThat(user.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		em.persist(criarUser());
		Optional<Usuario> user = userRepository.findByEmail("user@gmail.com");
		Assertions.assertThat(user.isPresent()).isTrue();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmailCenarioErro() {
		Optional<Usuario> user = userRepository.findByEmail("user@gmail.com");
		Assertions.assertThat(user.isPresent()).isFalse();
	}
	
	
	public static Usuario criarUser() {
		return Usuario
				.builder()
				.nome("userTEST")
				.email("user@gmail.com")
				.senha("user")
				.build();

	}
}