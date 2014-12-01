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

@ManagedBean(name="despesasBean")
@ViewScoped
public class DespesasBean {
	private Modalidade despesaSelecionada = new Modalidade();
	private List<Modalidade> listaDespesas;
	private LoginBean loginBean = ContextoUtil.getContextoBean();
	
	public Modalidade getDespesaSelecionada() {
		return despesaSelecionada;
	}
	public void setDespesaSelecionada(Modalidade despesaSelecionada) {
		this.despesaSelecionada = despesaSelecionada;
	}
	public LoginBean getLoginBean() {
		return loginBean;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public List<Modalidade> getListaDespesas() {
		if (listaDespesas == null){
			ModalidadeDAO dao = new ModalidadeDAO();
			listaDespesas = dao.listarPorModalidadeUsuario(TipoOperacao.DESPESA, loginBean.getUsuarioLogado(), true);
		}		
		return listaDespesas;
	}
	
	public void adicionarDespesa(){
		String descricao = despesaSelecionada.getDescricao();
		if (descricao == null || descricao.equals("")){
			MensagensUtil.mensagemAviso("Campo descrição da despesa requerido");
		} else{
			if (despesaSelecionada.isFixo() &&( despesaSelecionada.getValor() == null || despesaSelecionada.getDiaBase() == null)){
				MensagensUtil.mensagemAviso("Quando a despesa for fixa é necessário informar o valor e o dia base");
			} else{			
				ModalidadeDAO dao = new ModalidadeDAO();
				despesaSelecionada.setTipoOperacao(TipoOperacao.DESPESA);
				despesaSelecionada.setUsuario(loginBean.getUsuarioLogado());
				if (despesaSelecionada.getId() == null || despesaSelecionada.getId().equals(0)){
					dao.salvar(despesaSelecionada);
				} else{
					dao.atualizar(despesaSelecionada);
				}
				MensagensUtil.mensagemInfo("Despesa " + despesaSelecionada.getDescricao() + " salva com sucesso!");
				listaDespesas = null;
				despesaSelecionada = new Modalidade();
			}
		}
	}
	
	public void removerDespesa(){
		ModalidadeDAO dao = new ModalidadeDAO();
		if (dao.excluir(despesaSelecionada)){
			MensagensUtil.mensagemInfo("Despesa excluída com sucesso!");
		} else{
			MensagensUtil.mensagemErro("Erro na exlusão da despesa selecionada!");
		}
		listaDespesas = null;
		despesaSelecionada = new Modalidade();		
		
		
		listaDespesas.remove(despesaSelecionada);
		despesaSelecionada = new Modalidade();
	}	
		
	public String retornarMenu(){
		return "/privado/principal?faces-redirect=true";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void imprimirBrowser() {
		HashMap parametros = new HashMap();
		parametros.put("tipo", "DESPESAS");
		if (RelatoriosUtil
				.imprimeRelatorioBrowser("modalidades", parametros, listaDespesas) == false) {
			MensagensUtil
					.mensagemErro("Erro ao gerar Relatório de Despesas");
		}
	}	

}
