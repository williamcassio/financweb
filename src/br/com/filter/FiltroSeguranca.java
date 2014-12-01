package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bean.LoginBean;

@WebFilter(urlPatterns="/privado/*")
public class FiltroSeguranca implements Filter{
	
	private String contextPath;
	private LoginBean loginBean;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession sessao = httpRequest.getSession();
		contextPath = httpRequest.getContextPath();
		loginBean = (LoginBean) sessao.getAttribute("loginBean");
		if (loginBean == null){
			httpResponse.sendRedirect(contextPath + "/login.jsf");
		} else if (loginBean.getUsuarioLogado() == null){
			httpResponse.sendRedirect(contextPath + "/login.jsf");
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
