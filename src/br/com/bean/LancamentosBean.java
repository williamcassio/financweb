package br.com.bean;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import br.com.converter.ModalidadeConverter;
import br.com.dao.LancamentoDAO;
import br.com.dao.ModalidadeDAO;
import br.com.entity.Lancamento;
import br.com.entity.Modalidade;
import br.com.enums.TipoOperacao;
import br.com.utils.ContextoUtil;
import br.com.utils.GeraPlanilha;
import br.com.utils.MensagensUtil;
import br.com.utils.RelatoriosUtil;
import br.com.utils.Utilites;

@ManagedBean(name = "lancamentosBean")
@ViewScoped
public class LancamentosBean {

	private LoginBean loginBean = ContextoUtil.getContextoBean();

	private Date dtini;
	private Date dtfim;
	
	private Double totalReceitas;
	private Double totalDespesas;
	private Double totalSaldo;

	private PieChartModel pizzaReceitas;
	private PieChartModel pizzaDespesas;
	
	private PieChartModel pizzaTotal;

	public PieChartModel getPizzaTotal() {
		return pizzaTotal;
	}

	public void setPizzaTotal(PieChartModel pizzaTotal) {
		this.pizzaTotal = pizzaTotal;
	}

	public PieChartModel getPizzaReceitas() {
		return pizzaReceitas;
	}

	public void setPizzaReceitas(PieChartModel pizzaReceitas) {
		this.pizzaReceitas = pizzaReceitas;
	}

	public PieChartModel getPizzaDespesas() {
		return pizzaDespesas;
	}

	public void setPizzaDespesas(PieChartModel pizzaDespesas) {
		this.pizzaDespesas = pizzaDespesas;
	}

	public List<Lancamento> getListaReceitas() {
		return listaReceitas;
	}

	public void setListaReceitas(List<Lancamento> listaReceitas) {
		this.listaReceitas = listaReceitas;
	}

	public List<Lancamento> getListaDespesas() {
		return listaDespesas;
	}

	public void setListaDespesas(List<Lancamento> listaDespesas) {
		this.listaDespesas = listaDespesas;
	}

	private List<Lancamento> listaReceitas = new ArrayList<Lancamento>();
	private List<Lancamento> listaDespesas = new ArrayList<Lancamento>();

	private boolean mostrarGraficos = false;

	public boolean isMostrarGraficos() {
		return mostrarGraficos;
	}

	public void setMostrarGraficos(boolean mostrarGraficos) {
		this.mostrarGraficos = mostrarGraficos;
	}

	private TipoOperacao operacaoSelecionada;

	private ModalidadeConverter modalidadeConverter = new ModalidadeConverter();

	private List<Modalidade> listaModalidades;

	private Lancamento lancamentoSelecionado = new Lancamento();
	private List<Lancamento> lista;

	private boolean somenteNaoFixas;

	public TipoOperacao[] getOperacoes() {
		return TipoOperacao.values();
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public Date getDtini() {
		return dtini;
	}

	public void setDtini(Date dtini) {
		this.dtini = dtini;
	}

	public Date getDtfim() {
		return dtfim;
	}

	public void setDtfim(Date dtfim) {
		this.dtfim = dtfim;
	}

	public Lancamento getLancamentoSelecionado() {
		return lancamentoSelecionado;
	}

	public void setLancamentoSelecionado(Lancamento lancamentoSelecionado) {
		this.lancamentoSelecionado = lancamentoSelecionado;
	}

	public List<Lancamento> getLista() {
		if (lista == null) {
			LancamentoDAO dao = new LancamentoDAO();
			if (loginBean != null) {
				lista = dao.listarPorDataEUsuario(dtini, dtfim,
						loginBean.getUsuarioLogado());
			} else {
				lista = dao.listarGeralPorData(dtini, dtfim);
			}
		}
		calcularSaldo();
		return lista;
	}

	public LancamentosBean() {
		operacaoSelecionada = TipoOperacao.RECEITA;
		Utilites utils = new Utilites();
		Date hoje = new Date();
		dtini = utils.retornaDataBase(1, hoje);
		dtfim = utils.retornaDataBase(31, hoje);
		somenteNaoFixas = true;
		iniciarOperacao();
	}

	public List<Modalidade> getListaModalidades() {
		if (listaModalidades == null) {
			ModalidadeDAO dao = new ModalidadeDAO();
			listaModalidades = dao.listarPorModalidadeUsuario(
					operacaoSelecionada, loginBean.getUsuarioLogado(),
					!somenteNaoFixas);
		}
		return listaModalidades;
	}

	public TipoOperacao getOperacaoSelecionada() {
		return operacaoSelecionada;
	}

	public void setOperacaoSelecionada(TipoOperacao operacaoSelecionada) {
		this.operacaoSelecionada = operacaoSelecionada;
	}

	public void atualizaOperacoes() {
		listaModalidades = null;
	}

	public void carregarDadosOperacaoFixa() {
		Utilites ut = new Utilites();
		if (lancamentoSelecionado.getModalidade().isFixo()) {
			lancamentoSelecionado.setData(ut.retornaDataBase(
					lancamentoSelecionado.getModalidade().getDiaBase(),
					lancamentoSelecionado.getData()));
			lancamentoSelecionado.setValor(lancamentoSelecionado
					.getModalidade().getValor());
		}
	}

	public ModalidadeConverter getModalidadeConverter() {
		return modalidadeConverter;
	}

	public void setModalidadeConverter(ModalidadeConverter modalidadeConverter) {
		this.modalidadeConverter = modalidadeConverter;
	}

	public void salvarLancamento() {
		if (lancamentoSelecionado.getValor() == null
				|| lancamentoSelecionado.getValor() == 0l) {
			MensagensUtil.mensagemAviso("Valor da operação não informado!");
		} else {
			lancamentoSelecionado.setUsuario(loginBean.getUsuarioLogado());
			LancamentoDAO dao = new LancamentoDAO();
			if (lancamentoSelecionado.getId() == null
					|| lancamentoSelecionado.getId() == 0l) {
				if (dao.salvar(lancamentoSelecionado) != 0l) {
					MensagensUtil.mensagemInfo("Lançamento salvo com sucesso!");
				} else {
					MensagensUtil.mensagemErro("Erro ao salvar lançamento!");
				}
			} else {
				if (dao.atualizar(lancamentoSelecionado) != 0l) {
					MensagensUtil.mensagemInfo("Lançamento salvo com sucesso!");
				} else {
					MensagensUtil.mensagemErro("Erro ao salvar lançamento!");
				}
			}
			iniciarOperacao();
		}
	}

	public void excluirLancamento() {
		LancamentoDAO dao = new LancamentoDAO();
		if (dao.excluir(lancamentoSelecionado)) {
			MensagensUtil.mensagemInfo("Lançamento excluído com sucesso!");
		} else {
			MensagensUtil.mensagemErro("Erro ao excluir lançamento!");
		}
		iniciarOperacao();
	}

	public void iniciarOperacao() {
		listaModalidades = null;
		lista = null;
		lancamentoSelecionado = new Lancamento();
		lancamentoSelecionado.setData(new Date());
	}

	public String retornarMenu() {
		return "/privado/principal?faces-redirect=true";
	}

	public boolean isSomenteNaoFixas() {
		return somenteNaoFixas;
	}

	public void setSomenteNaoFixas(boolean somenteNaoFixas) {
		this.somenteNaoFixas = somenteNaoFixas;
	}

	public void lancarReceitasDespesasFixas() {
		Date data = dtini;
		ModalidadeDAO moddao = new ModalidadeDAO();
		List<Modalidade> listafixa = moddao
				.listarModalidadesFixasPorUsuario(loginBean.getUsuarioLogado());
		LancamentoDAO landao = new LancamentoDAO();
		Utilites ut = new Utilites();
		for (Modalidade modalidade : listafixa) {
			Lancamento lanc = new Lancamento();
			lanc.setModalidade(modalidade);
			lanc.setUsuario(loginBean.getUsuarioLogado());
			lanc.setValor(modalidade.getValor());
			lanc.setData(ut.retornaDataBase(modalidade.getDiaBase(), data));
			landao.salvar(lanc);
		}
		MensagensUtil.mensagemInfo("Dados inseridos com sucesso!");
		iniciarOperacao();
	}

	public void calcularSaldo() {
		totalReceitas = (double) 0l;
		totalDespesas = (double) 0l;
		totalSaldo = (double) 0l;
		for (Lancamento lanc : lista) {
			if (lanc.getModalidade().getTipoOperacao() == TipoOperacao.RECEITA) {
				totalReceitas += lanc.getValor();
			} else {
				totalDespesas += lanc.getValor();
			}
		}
		totalSaldo = totalReceitas - totalDespesas;
	}

	public void carregarLancamentos() {
		if (dtini.after(dtfim)) {
			Utilites ut = new Utilites();
			dtfim = ut.retornaDataBase(31, dtini);
		}
		iniciarOperacao();
	}

	public String gerarPlanilha() {
		String nomeArquivo = "";
		if (lista.size() > 0) {
			String periodo;
			SimpleDateFormat formatoSalvamento = new SimpleDateFormat(
					"dd-MM-yyyy");
			String nome = "Lancamento_"
					+ loginBean.getUsuarioLogado().getLogin() + "_"
					+ formatoSalvamento.format(dtini) + "_"
					+ formatoSalvamento.format(dtfim);
			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			periodo = "Entre " + sf.format(dtini) + " e " + sf.format(dtfim);
			GeraPlanilha gp = new GeraPlanilha();
			nomeArquivo = gp.localPlanilhas() + File.separator + nome + ".xlsx";
			gp.montarPlanilha(nomeArquivo, loginBean.getUsuarioLogado()
					.getNome(), periodo, lista);
		}
		return nomeArquivo;
	}

	public void download() {
		String caminho = gerarPlanilha();
		Utilites ut = new Utilites();
		try {
			ut.downloadFile(caminho);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void imprimirPDF() {
		String periodo;
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		periodo = "Período " + sf.format(dtini) + " e " + sf.format(dtfim);
		HashMap parametros = new HashMap();
		parametros.put("periodo", periodo);
		if (RelatoriosUtil
				.imprimeRelatorioPDF("lancamentos", parametros, lista) == false) {
			MensagensUtil
					.mensagemErro("Erro ao gerar Relatório de Lançamentos");
		}
	}

	public void carregarGrafico() {
		LancamentoDAO dao = new LancamentoDAO();
		Double totalRec = (double) 0l;
		Double totalDes = (double) 0l;

		listaReceitas = dao.listarPorDataEUsuarioETipo(dtini, dtfim,
				loginBean.getUsuarioLogado(), TipoOperacao.RECEITA);
		pizzaReceitas = new PieChartModel();
		for (Lancamento lanc : listaReceitas) {
			pizzaReceitas.set(lanc.getModalidade().getDescricao(),
					lanc.getValor());
			totalRec += lanc.getValor();
		}
		pizzaReceitas.setTitle("Receitas");
		pizzaReceitas.setLegendPosition("w");
		pizzaReceitas.setShowDataLabels(true);

		listaDespesas = dao.listarPorDataEUsuarioETipo(dtini, dtfim,
				loginBean.getUsuarioLogado(), TipoOperacao.DESPESA);
		pizzaDespesas = new PieChartModel();
		for (Lancamento lanc : listaDespesas) {
			pizzaDespesas.set(lanc.getModalidade().getDescricao(),
					lanc.getValor());
			totalDes += lanc.getValor();
		}
		pizzaDespesas.setTitle("Despesas");
		pizzaDespesas.setLegendPosition("w");
		pizzaDespesas.setShowDataLabels(true);
		
		pizzaTotal = new PieChartModel();
		pizzaTotal.set(TipoOperacao.RECEITA.toString(), totalRec);
		pizzaTotal.set(TipoOperacao.DESPESA.toString(), totalDes);
		pizzaTotal.setTitle("Receitas X Despesas");
		pizzaTotal.setLegendPosition("w");
		pizzaTotal.setShowDataLabels(true);		
		
		
		mostrarGrafico();
	}
	
	public void mostrarGrafico(){
		mostrarGraficos =!mostrarGraficos;
	}
	
	public String getDataFormatadaIni(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(dtini);
	}
	
	public String getDataFormatadaFim(){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(dtfim);
	}

	public Double getTotalReceitas() {
		return totalReceitas;
	}

	public void setTotalReceitas(Double totalReceitas) {
		this.totalReceitas = totalReceitas;
	}

	public Double getTotalDespesas() {
		return totalDespesas;
	}

	public void setTotalDespesas(Double totalDespesas) {
		this.totalDespesas = totalDespesas;
	}

	public Double getTotalSaldo() {
		return totalSaldo;
	}

	public void setTotalSaldo(Double totalSaldo) {
		this.totalSaldo = totalSaldo;
	}	
}
