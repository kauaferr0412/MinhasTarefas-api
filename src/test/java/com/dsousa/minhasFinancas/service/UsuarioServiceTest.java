package com.dsousa.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minhasFinancas.exceptions.ErroAutenticacao;
import com.dsousa.minhasFinancas.exceptions.RegraDeNegocioException;
import com.dsousa.minhasFinancas.model.entity.Usuario;
import com.dsousa.minhasFinancas.model.repository.UsuarioRepository;
import com.dsousa.minhasFinancas.service.Impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {

	private UsuarioService userService;
	
	@Autowired
	private UsuarioRepository userRepository;
		
	private static final String MENSAGEM_EXCEPTION = "Já existe um usuário com este e-mail";
	private static final String MENSAGEM_EXCEPTION_SENHA_INVALIDA = "Senha inválida";
	private static final String MENSAGEM_EXCEPTION_USUARIO_NAO_ENCONTRADO = "Não foi encontrado um usuário com o e-mail digitado";

	
	@BeforeEach
	public void init(){
		userService = new UsuarioServiceImpl(userRepository);
	}
	
	
	@Test
	public void test01deveAutenticarUsuarioComSucesso() {
		String email = "kaua@email.com";
		String senha="senha";
			
		Usuario user = Usuario.builder().email(email).senha(senha).id(1L).build();
		userService.salvarUsuario(user);
		Usuario result = userService.autenticar(email, senha);
		Assertions.assertNotNull(result);
				
		
	}
	
	@Test
	public void test02deveAutenticarUsuarioComErro() {
		String email = "kaua@email.com";
		String senha="senha";
			
		Usuario user = Usuario.builder().email(email).senha("teste").id(1L).build();
		userService.salvarUsuario(user);
		ErroAutenticacao thrown = Assertions.assertThrows(ErroAutenticacao.class,() -> userService.autenticar(email, senha));
		assertEquals(MENSAGEM_EXCEPTION_SENHA_INVALIDA, thrown.getMessage());
		
		Usuario user2 = Usuario.builder().email("kaua@ferr").senha("teste").id(1L).build();
		ErroAutenticacao thrown2 = Assertions.assertThrows(ErroAutenticacao.class,() -> userService.autenticar(user2.getEmail(), user2.getSenha()));
		assertEquals(MENSAGEM_EXCEPTION_USUARIO_NAO_ENCONTRADO, thrown2.getMessage());
	}
	
	
	@Test
	public void test03verificaUsuarioJaTemEsseEmail() {
		userService.validarEmail("email@email.com");
	}
		
	@Test
	public void test04verificaErroUsuarioJaTemEsseEmail() {
		Usuario user = Usuario.builder().nome("userTEST").email("user@gmail.com").senha("user").build();
		userRepository.save(user);
		RegraDeNegocioException thrown = Assertions.assertThrows(RegraDeNegocioException.class,() -> userService.validarEmail("user@gmail.com"));
		assertEquals(MENSAGEM_EXCEPTION, thrown.getMessage());
	}	
		
	@Test
	public void test05verificaCadastroCorretoUsuario() {
		Usuario user1 = Usuario.builder().nome("userTEST1").email("user@gmail.com").senha("usuario").build();
		userService.salvarUsuario(user1);
		Usuario user2 = Usuario.builder().nome("userTEST2").email("user@gmail.com").senha("user").build();
		RegraDeNegocioException thrown = Assertions.assertThrows(RegraDeNegocioException.class,() -> userService.salvarUsuario(user2));
		assertEquals(MENSAGEM_EXCEPTION, thrown.getMessage());
		
	}
}
