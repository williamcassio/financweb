package br.com.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.entity.Lancamento;
import br.com.entity.Usuario;
import br.com.enums.TipoOperacao;
import br.com.utils.HibernateUtil;

public class LancamentoDAO {
	
	public Long salvar(Lancamento lancamento){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.save(lancamento);
			session.getTransaction().commit();
			session.close();
			return lancamento.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}		
	}
	
	public Long atualizar(Lancamento lancamento){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.update(lancamento);
			session.getTransaction().commit();
			session.close();
			return lancamento.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}		
	}
	
	public boolean excluir(Lancamento lancamento){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.delete(lancamento);
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
	
	public Lancamento carregarPorId(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Lancamento lancamento = (Lancamento) session.get(Lancamento.class, id);
		session.close();
		return lancamento;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> listarGeralPorUsuario(Usuario usuario){
		List<Lancamento> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Lancamento.class);
		Criteria crOper = cr.createCriteria("modalidade");
		crOper.addOrder(Order.asc("tipoOperacao"));
		cr.add(Restrictions.eq("usuario", usuario));
		cr.addOrder(Order.asc("modalidade"));
		lista = cr.list();
		session.close();
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> listarPorDataEUsuario(Date dtini, Date dtfim, Usuario usuario){
		List<Lancamento> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Lancamento.class);
		Criteria crOper = cr.createCriteria("modalidade");
		crOper.addOrder(Order.asc("tipoOperacao"));
		crOper.addOrder(Order.asc("descricao"));
		cr.add(Restrictions.eq("usuario", usuario));
		cr.add(Restrictions.between("data", dtini, dtfim));
		
		lista = cr.list();
		session.close();
		return lista;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> listarGeralPorData(Date dtini, Date dtfim){
		List<Lancamento> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Lancamento.class);
		Criteria crOper = cr.createCriteria("modalidade");
		crOper.addOrder(Order.asc("tipoOperacao"));
		cr.add(Restrictions.between("data", dtini, dtfim));
		cr.addOrder(Order.asc("modalidade"));
		lista = cr.list();
		session.close();
		return lista;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Lancamento> listarPorDataEUsuarioETipo(Date dtini, Date dtfim, Usuario usuario, TipoOperacao tipoOperacao){
		List<Lancamento> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Lancamento.class);
		Criteria crOper = cr.createCriteria("modalidade");
		crOper.add(Restrictions.eq("tipoOperacao", tipoOperacao));
		crOper.addOrder(Order.asc("descricao"));
		cr.add(Restrictions.eq("usuario", usuario));
		cr.add(Restrictions.between("data", dtini, dtfim));
		
		lista = cr.list();
		session.close();
		return lista;		
	}
	
	
	
}
