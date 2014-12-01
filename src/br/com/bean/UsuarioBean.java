package br.com.bean;

import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import br.com.dao.UsuarioDAO;
import br.com.entity.Usuario;
import br.com.utils.ContextoUtil;
import br.com.utils.MensagensUtil;
import br.com.utils.RelatoriosUtil;
import br.com.utils.SenhaUtil;

@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean {

	private LoginBean loginBean = ContextoUtil.getContextoBean();

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	private Usuario usuarioSelecionado;
	private List<Usuario> listaUsuarios;
	private boolean exibirCampos = false;
	private String confirmaSenha;

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public boolean isExibirCampos() {
		return exibirCampos;
	}

	public void setExibirCampos(boolean exibirCampos) {
		this.exibirCampos = exibirCampos;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Usuario> getListaUsuarios() {
		if (listaUsuarios == null) {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			listaUsuarios = usuarioDAO.listar();
		}
		return listaUsuarios;
	}

	public void salvar() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Long id = usuarioSelecionado.getId();
		Long resultado;
		SenhaUtil su = new SenhaUtil();
		usuarioSelecionado.setSenha(su.encriptaSenha(usuarioSelecionado
				.getSenha()));
		if (id == null || id == 0l) {
			resultado = usuarioDAO.salvar(usuarioSelecionado);
		} else {
			resultado = usuarioDAO.atualizar(usuarioSelecionado);
		}
		if (resultado != 0l) {
			MensagensUtil.mensagemInfo("Usuário "
					+ usuarioSelecionado.getNome() + " salvo com sucesso!");
			listaUsuarios = null;
			exibirCampos = false;
		} else {
			MensagensUtil.mensagemInfo("Erro ao salvar o usuário "
					+ usuarioSelecionado.getNome());
		}
	}

	public void excluir() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (!usuarioDAO.excluir(usuarioSelecionado))
			MensagensUtil
					.mensagemErro("Erro ao exluir usuário. O mesmo possui lançamentos de receitas/despesas!");
		listaUsuarios = null;
		exibirCampos = false;
	}

	public void editar() {
		exibirCampos = true;
	}

	public void novo() {
		usuarioSelecionado = new Usuario();
		exibirCampos = true;
	}

	public void cancelar() {
		exibirCampos = false;
	}

	public String retornarMenuPrincipal() {
		return "/privado/principal?faces-redirect=true";
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void imprimirPDF() {
		HashMap parametros = new HashMap();
		if (RelatoriosUtil
				.imprimeRelatorioPDF("usuarios", parametros, listaUsuarios) == false) {
			MensagensUtil
					.mensagemErro("Erro ao gerar Relatório de Usuários");
		}
	}	

	@SuppressWarnings({ "rawtypes" })
	public void imprimirBrowser() {
		HashMap parametros = new HashMap();
		if (RelatoriosUtil
				.imprimeRelatorioBrowser("usuarios", parametros, listaUsuarios) == false) {
			MensagensUtil
					.mensagemErro("Erro ao gerar Relatório de Usuários");
		}
	}	

}
