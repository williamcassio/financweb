package br.com.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import br.com.dao.UsuarioDAO;
import br.com.entity.Usuario;
import br.com.utils.ContextoUtil;
import br.com.utils.MensagensUtil;
import br.com.utils.SenhaUtil;

@ManagedBean(name="principalBean")
@RequestScoped
public class PrincipalBean {
	private LoginBean loginBean = ContextoUtil.getContextoBean();
	private String senAtual;
	private String senNova;
	private String senConf;	

	public String getSenAtual() {
		return senAtual;
	}

	public void setSenAtual(String senAtual) {
		this.senAtual = senAtual;
	}

	public String getSenNova() {
		return senNova;
	}

	public void setSenNova(String senNova) {
		this.senNova = senNova;
	}

	public String getSenConf() {
		return senConf;
	}

	public void setSenConf(String senConf) {
		this.senConf = senConf;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	
	public String receitas(){
		return "/privado/receitas?faces-redirect=true";
	}
	
	public String despesas(){
		return "/privado/despesas?faces-redirect=true";
	}	
	
	public String lancamentos(){
		return "/privado/lancamentos?faces-redirect=true";
	}
	
	public String usuarios(){
		return "/privado/usuarios?faces-redirect=true";
	}	
	
	public String efetuarLogoff(){
		loginBean.setUsuarioLogado(null);
		loginBean.setLogin(null);
		return "/login?faces-redirect=true";
	}
	
	public void confereSenha(){
		boolean bRetorno;
		SenhaUtil su = new SenhaUtil();
		Usuario user = new Usuario();
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		bRetorno = usuarioDAO.efetuarLogin(ContextoUtil.getContextoBean().getUsuarioLogado().getLogin(), su.encriptaSenha(senAtual));
		if (bRetorno == false){
			MensagensUtil.mensagemErro("Senha atual não confere");
		} else if (!senNova.equals(senConf)) {
			MensagensUtil.mensagemErro("Senha nova não confere!");
		} else{
			user = usuarioDAO.carregarPorLogin(ContextoUtil.getContextoBean().getUsuarioLogado().getLogin());
			user.setSenha(su.encriptaSenha(senNova));
			usuarioDAO.atualizar(user);
			MensagensUtil.mensagemInfo("Senha alterada com sucesso!");
		}
	}	
	
}
