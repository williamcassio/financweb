package br.com.bean;

import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.dao.ModalidadeDAO;
import br.com.entity.Modalidade;
import br.com.enums.TipoOperacao;
import br.com.utils.ContextoUtil;
import br.com.utils.MensagensUtil;
import br.com.utils.RelatoriosUtil;

@ManagedBean(name = "receitasBean")
@ViewScoped
public class ReceitasBean {
	private Modalidade receitaSelecionada = new Modalidade();
	private List<Modalidade> listaReceitas;
	private LoginBean loginBean = ContextoUtil.getContextoBean();

	public Modalidade getReceitaSelecionada() {
		return receitaSelecionada;
	}

	public void setReceitaSelecionada(Modalidade receitaSelecionada) {
		this.receitaSelecionada = receitaSelecionada;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public List<Modalidade> getListaReceitas() {
		if (listaReceitas == null) {
			ModalidadeDAO dao = new ModalidadeDAO();
			listaReceitas = dao.listarPorModalidadeUsuario(
					TipoOperacao.RECEITA, loginBean.getUsuarioLogado(), true);
		}
		return listaReceitas;
	}

	public void adicionarReceita() {
		String descricao = receitaSelecionada.getDescricao();
		if (descricao == null || descricao.equals("")) {
			MensagensUtil.mensagemAviso("Campo descrição da receita requerido");
		} else {
			if (receitaSelecionada.isFixo()
					&& (receitaSelecionada.getValor() == null || receitaSelecionada
							.getDiaBase() == null)) {
				MensagensUtil
						.mensagemAviso("Quando a receita for fixa é necessário informar o valor e o dia base");
			} else {
				ModalidadeDAO dao = new ModalidadeDAO();
				receitaSelecionada.setTipoOperacao(TipoOperacao.RECEITA);
				receitaSelecionada.setUsuario(loginBean.getUsuarioLogado());
				if (receitaSelecionada.getId() == null
						|| receitaSelecionada.getId().equals(0)) {
					dao.salvar(receitaSelecionada);
				} else {
					dao.atualizar(receitaSelecionada);
				}
				MensagensUtil.mensagemInfo("Receita "
						+ receitaSelecionada.getDescricao()
						+ " salva com sucesso!");
				listaReceitas = null;
				receitaSelecionada = new Modalidade();
			}
		}
	}
	

	public void atualizarLista() {
		listaReceitas = null;
		receitaSelecionada = new Modalidade();
	}

	public void removerReceita() {
		ModalidadeDAO dao = new ModalidadeDAO();
		if (dao.excluir(receitaSelecionada)) {
			MensagensUtil.mensagemInfo("Receita excluída com sucesso!");
		} else {
			MensagensUtil
					.mensagemErro("Erro na exlusão da receita selecionada!");
		}
		listaReceitas = null;
		receitaSelecionada = new Modalidade();
	}

	public String retornarMenu() {
		return "/privado/principal?faces-redirect=true";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void imprimirBrowser() {
		HashMap parametros = new HashMap();
		parametros.put("tipo", "RECEITAS");
		if (RelatoriosUtil
				.imprimeRelatorioBrowser("modalidades", parametros, listaReceitas) == false) {
			MensagensUtil
					.mensagemErro("Erro ao gerar Relatório de Receitas");
		}
	}	
}
