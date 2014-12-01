package br.com.tests;

import br.com.dao.UsuarioDAO;
import br.com.entity.Usuario;
import br.com.utils.SenhaUtil;
public class ConectarBanco {

	public static void main(String[] args) {
/*		
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			System.out.println("Conectou!!");
		} finally {
			session.close();
			System.out.println("Fechou Conexão");
		}
	
		
		Usuario user = new Usuario();
		UsuarioDAO userDAO = new UsuarioDAO();
		SenhaUtil su = new SenhaUtil();
		String sc = su.encriptaSenha("sql");
		user.setNome("William Cassio");
		user.setLogin("william");
		user.setSenha(sc);
		user.setAdministrador(true);
		user.setEmail("william.gomes@uninorte.com.br");
		userDAO.salvar(user);
		System.out.println("Usuário Cadastrado com sucesso!");
		
		*/
		Usuario user = new Usuario();
		UsuarioDAO userDAO = new UsuarioDAO();
		user = userDAO.carregarPorLogin("william");
		SenhaUtil su = new SenhaUtil();
		String sc = su.encriptaSenha("sql");		
		user.setSenha(sc);
		userDAO.atualizar(user);
		System.out.println("Nome          " + user.getNome());
		System.out.println("Login         " + user.getLogin());
		System.out.println("Senha         " + user.getSenha());
		System.out.println("Email         " + user.getEmail());
		System.out.println("Administrador " + user.isAdministrador());
		
	}

}
