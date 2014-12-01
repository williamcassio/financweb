package br.com.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.entity.Usuario;
import br.com.utils.HibernateUtil;

public class UsuarioDAO {

	public Long salvar(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.save(usuario);
			session.getTransaction().commit();
			session.close();
			return usuario.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}
	}

	public Long atualizar(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.update(usuario);
			session.getTransaction().commit();
			session.close();
			return usuario.getId();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (session.isOpen()) {
				session.close();
			}
			return 0l;
		}
	}

	public boolean excluir(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.getTransaction().begin();
			session.delete(usuario);
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
	
	public Usuario carregarPorId(Long id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Usuario usuario = (Usuario) session.get(Usuario.class, id);
		session.close();
		return usuario;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listar(){
		List<Usuario> lista;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Usuario.class);
		cr.addOrder(Order.asc("nome"));
		lista = cr.list();
		session.close();
		return lista;
	}
	
	public boolean efetuarLogin(String login, String senha) {
		boolean bRetorno;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Usuario.class);
		cr.add(Restrictions.eq("login", login));
		cr.add(Restrictions.eq("senha", senha));
		bRetorno = cr.list().size() > 0;
		session.close();
		return bRetorno;
	}
	
	public Usuario carregarPorLogin(String login){
		Usuario usuario;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria cr = session.createCriteria(Usuario.class);
		cr.add(Restrictions.eq("login", login));
		usuario = (Usuario) cr.uniqueResult();
		session.close();
		return usuario;		
	}

}
