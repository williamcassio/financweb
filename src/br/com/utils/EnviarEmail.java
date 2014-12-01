package br.com.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EnviarEmail {

	public boolean enviarEmail(String senha, String enderecoEmail) {
		SimpleEmail email = new SimpleEmail();
		email.setHostName("mail.adm.objetivomao.br");
		// email.setHostName("mail.myserver.com");
		//email.setDebug(true);
//		email.setHostName("smtp.gmail.com");
//		email.setSslSmtpPort("465");
//		email.setSSLOnConnect(true);
//		email.setStartTLSRequired(true);
//		email.setAuthentication("william.desenv@gmail.com", "delphi256");

		try {
			email.addTo(enderecoEmail);
			email.setFrom("sistema_gestor@uninorte.com.br", "SisFinanc"); // remetente
			email.setSubject("Nova senha"); // assunto do e-mail
			email.setMsg(senha); // conteudo
			email.send(); // envia o e-mail
			return true;
		} catch (EmailException e) {
			System.out.println(e.getMessage());
			return false;

		}

	}
}
