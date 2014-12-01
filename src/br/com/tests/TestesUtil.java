package br.com.tests;


import java.util.List;

import br.com.dao.ModalidadeDAO;
import br.com.dao.UsuarioDAO;
import br.com.entity.Modalidade;
import br.com.entity.Usuario;
import br.com.enums.TipoOperacao;

public class TestesUtil {

	public static void main(String[] args) {
		/*
		String novaSenha;
		SenhaUtil su = new SenhaUtil();
		novaSenha = su.gerarSenha();
		EnviarEmail em = new EnviarEmail();
		if(em.enviarEmail(novaSenha, "william.gomes@uninorte.com.br")){
			System.out.println("Email enviado com sucesso");
		} else{
			System.out.println("Erro ao enviar email");
		}
		*/
		/*
		Utilites ut = new Utilites();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2014);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		Date data = c.getTime();
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		data = ut.retornaDataBase(31, data);
		System.out.println(sf.format(data));
		*/
		
		List<Modalidade> lista;
		ModalidadeDAO dao = new ModalidadeDAO();
		Usuario user;
		UsuarioDAO userdao = new UsuarioDAO();
		user = userdao.carregarPorLogin("william");
		lista = dao.listarPorModalidadeUsuario(TipoOperacao.RECEITA, user, false);
		for(Modalidade mod : lista){
			System.out.println(mod.getDescricao() + ", " + mod.isFixo());
		}
	}

}
