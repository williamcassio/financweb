package br.com.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.entity.Modalidade;
import br.com.entity.Usuario;
import br.com.enums.TipoOperacao;
import br.com.utils.HibernateUtil;

public class ModalidadeDAO {

	public Long salvar(Modalidade modalidade) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.save(modalidade);
			session.getTransaction().commit();
			session.close();
			return modalidade.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}
	}

	public Long atualizar(Modalidade modalidade) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.update(modalidade);
			session.getTransaction().commit();
			session.close();
			return modalidade.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}
	}

	public boolean excluir(Modalidade modalidade) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.delete(modalidade);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return false;
		}
	}

	public Modalidade carregarPorId(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Modalidade modalidade = (Modalidade) session.get(Modalidade.class, id);
		session.close();
		return modalidade;
	}

	@SuppressWarnings("unchecked")
	public List<Modalidade> listarGeral() {
		List<Modalidade> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Modalidade.class);
		cr.addOrder(Order.asc("tipoOepracao"));
		cr.addOrder(Order.asc("descricao"));
		lista = cr.list();
		session.close();
		return lista;
	}
	@SuppressWarnings("unchecked")
	public List<Modalidade> listarPorModalidade(TipoOperacao tipo) {
		List<Modalidade> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Modalidade.class);
		cr.add(Restrictions.eq("tipoOperacao", tipo));
		cr.addOrder(Order.asc("descricao"));
		lista = cr.list();
		session.close();
		return lista;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Modalidade> listarPorModalidadeUsuario(TipoOperacao tipo, Usuario usuario, boolean naofixas) {
		List<Modalidade> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Modalidade.class);
		cr.add(Restrictions.eq("tipoOperacao", tipo));
		cr.add(Restrictions.eq("usuario", usuario));
		if (naofixas == false){
			cr.add(Restrictions.eq("fixo", false));
		}
		cr.addOrder(Order.asc("descricao"));
		lista = cr.list();
		session.close();
		return lista;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Modalidade> listarModalidadesFixasPorUsuario(Usuario usuario) {
		List<Modalidade> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Modalidade.class);
		cr.add(Restrictions.eq("usuario", usuario));
		cr.add(Restrictions.eq("fixo", true));
		cr.addOrder(Order.asc("descricao"));
		lista = cr.list();
		session.close();
		return lista;
	}	
}
