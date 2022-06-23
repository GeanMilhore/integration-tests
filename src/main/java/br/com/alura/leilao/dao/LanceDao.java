package br.com.alura.leilao.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;

@Repository
public class LanceDao {

	private EntityManager em;

	@Autowired
	LanceDao(EntityManager em){
		this.em = em;
	}
	
	public Lance salvar(Lance lance) {
		return em.merge(lance);
	}
	
	public Lance buscarPorId(Long id) {
		return em.find(Lance.class, id);
	}

	public Lance buscarMaiorLanceDoLeilao(Leilao leilao) {
		return em.createQuery("SELECT l FROM Lance l WHERE l.valor = (SELECT MAX(lance.valor) FROM Lance lance) and l.leilao = :leilao ", Lance.class)
				.setParameter("leilao", leilao)
				.getSingleResult();
	}
	
}
