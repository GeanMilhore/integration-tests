package br.com.alura.leilao.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.LanceBuilder;
import br.com.alura.leilao.util.builder.LeilaoBuilder;
import br.com.alura.leilao.util.builder.UsuarioBuilder;

class LanceDaoTest {

	private LanceDao dao;
	private EntityManager em;

	@BeforeEach
	public void beforeEach() {
		this.em = JPAUtil.getEntityManager();
		this.dao = new LanceDao(em);
		em.getTransaction().begin();
	}

	@AfterEach
	public void afterEach() {
		em.getTransaction().rollback();
	}
	
	@Test
	public void deveriaCriarUmLance() {
		Usuario usuario = new UsuarioBuilder()
					.comNome("fulano")
					.comEmail("fulano@email.com")
					.comSenha("123456789")
					.criar();
		em.persist(usuario);
		
		Leilao leilao = new LeilaoBuilder()
				.comNome("Mochila")
				.comValorInicial("500")
				.comUsuario(usuario)
				.comData(LocalDate.now())
				.criar();
		em.persist(leilao);
		
		Lance lance = new LanceBuilder()
				.comUsuario(usuario)
				.comValor("505")
				.comLeilao(leilao)
				.criar();
		lance = dao.salvar(lance);
		
		Lance salvo = dao.buscarPorId(lance.getId());
		Assert.assertNotNull(salvo);
	}
	

	@Test
	public void deveriaBuscarMaiorLance() {
		Usuario usuario = new UsuarioBuilder()
					.comNome("fulano")
					.comEmail("fulano@email.com")
					.comSenha("123456789")
					.criar();
		em.persist(usuario);
		
		Usuario usuario2 = new UsuarioBuilder()
				.comNome("bruceWayne")
				.comEmail("bruce@email.com")
				.comSenha("batmin")
				.criar();
		em.persist(usuario2);
		
		Leilao leilao = new LeilaoBuilder()
				.comNome("Mochila")
				.comValorInicial("500")
				.comUsuario(usuario)
				.comData(LocalDate.now())
				.criar();
		em.persist(leilao);
		
		Lance lance = new LanceBuilder()
				.comUsuario(usuario)
				.comValor("505")
				.comLeilao(leilao)
				.criar();
		lance = dao.salvar(lance);
		

		Lance lance2 = new LanceBuilder()
				.comUsuario(usuario2)
				.comValor("510")
				.comLeilao(leilao)
				.criar();
		lance = dao.salvar(lance2);
		
		Lance maiorLance = dao.buscarMaiorLanceDoLeilao(leilao);
		Assert.assertNotNull(maiorLance);
		Assert.assertEquals(new BigDecimal(510), maiorLance.getValor());
		Assert.assertEquals("bruceWayne", maiorLance.getUsuario().getNome());
		
	}

}
