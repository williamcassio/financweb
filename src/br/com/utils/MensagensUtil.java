package br.com.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MensagensUtil {
	
	public static void mensagemInfo(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "Informação"));
	}
	
	public static void mensagemErro(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "Erro"));
	}
	
	public static void mensagemAviso(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "Aviso"));
	}
	
}
