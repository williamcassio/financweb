package br.com.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.bean.LoginBean;

public class ContextoUtil {
	
	public static LoginBean getContextoBean(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		HttpSession session = (HttpSession) external.getSession(true);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
		return loginBean;
	}
}
