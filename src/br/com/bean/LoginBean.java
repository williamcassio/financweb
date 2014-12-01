package br.com.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.dao.UsuarioDAO;
import br.com.entity.Usuario;
import br.com.utils.EnviarEmail;
import br.com.utils.MensagensUtil;
import br.com.utils.SenhaUtil;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean {
	private String login;
	private String senha;
	private String confirmaSenha;

	private Usuario usuarioLogado;
	private boolean novoUsuario;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public boolean isNovoUsuario() {
		return novoUsuario;
	}

	public void setNovoUsuario(boolean novoUsuario) {
		this.novoUsuario = novoUsuario;
	}

	public String efetuarLogin() {
		String destino = null;
		if (login == null || login.equals("") || senha == null
				|| senha.equals("")) {
			MensagensUtil.mensagemErro("Informe Login e Senha");
		} else {
			UsuarioDAO userDAO = new UsuarioDAO();
			SenhaUtil su = new SenhaUtil();
			this.senha = su.encriptaSenha(senha);
			if (userDAO.efetuarLogin(this.login, this.senha)) {
				this.usuarioLogado = userDAO.carregarPorLogin(login);
				destino = "/privado/principal.jsf?faces-redirect=true";
			} else {
				MensagensUtil.mensagemErro("Login e senha inválidos");
			}
		}
		return destino;
	}

	public String registrarNovoUsuario() {
		UsuarioDAO userDAO = new UsuarioDAO();
		SenhaUtil su = new SenhaUtil();
		usuarioLogado.setSenha(su.encriptaSenha(usuarioLogado.getSenha()));
		userDAO.salvar(usuarioLogado);
		novoUsuario = false;
		MensagensUtil
				.mensagemInfo("Olá "
						+ usuarioLogado.getNome()
						+ ", seu cadastro foi realizado com sucesso. Efetue seu o login.");
		login = null;
		senha = null;
		return null;
	}

	public void novoUsuario() {
		novoUsuario = true;
		usuarioLogado = new Usuario();
		usuarioLogado.setAdministrador(false);
		confirmaSenha = null;
	}

	public void cancelarRegistro() {
		login = null;
		senha = null;
		novoUsuario = false;
		usuarioLogado = null;
	}

	public void enviarNovaSenha() {
		if (login == null || login == "") {
			MensagensUtil
					.mensagemAviso("Para envio de nova senha é necessário informe o login");
		} else {
			Usuario user = new Usuario();
			UsuarioDAO dao = new UsuarioDAO();
			user = dao.carregarPorLogin(login);
			if (user == null) {
				MensagensUtil.mensagemErro("Login não encontrado!");
			} else {
				SenhaUtil su = new SenhaUtil();
				String novaSenha = su.gerarSenha();
				EnviarEmail em = new EnviarEmail();
				if (em.enviarEmail(novaSenha, user.getEmail())) {
					user.setSenha(su.encriptaSenha(novaSenha));
					dao.atualizar(user);
					MensagensUtil
							.mensagemInfo("Nova senha enviada para o email "
									+ user.getEmail());
				}
			}
		}
	}
}
