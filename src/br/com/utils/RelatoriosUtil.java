package br.com.utils;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class RelatoriosUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean imprimeRelatorioBrowser(String nomeRelatorio,
			HashMap parametros, List lista) {
		try {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
					lista);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();
			ExternalContext ec = facesContext.getExternalContext();
			ec.responseReset();

			String path = scontext.getRealPath("/WEB-INF/relatorios/");
			parametros.put("SUBREPORT_DIR", path + File.separator);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					scontext.getRealPath("/WEB-INF/relatorios")
							+ File.separator + nomeRelatorio + ".jasper",
					parametros, dataSource);
			ec.setResponseHeader("Content-Disposition", "attachment; filename="
					+ nomeRelatorio + ".pdf");

			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse res = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			res.setContentType("application/pdf");
			int codigo = (int) (Math.random() * 1000);
			res.setHeader("Content-disposition", "inline);filename=relatorio_"
					+ codigo + ".pdf");
			res.getOutputStream().write(b);
			res.getCharacterEncoding();
			facesContext.responseComplete();

			// JasperViewer.viewReport(jasperPrint, false);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean imprimeRelatorioPDF(String nomeRelatorio,
			HashMap parametros, List lista) {

		try {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
					lista);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext
					.getExternalContext().getContext();
			ExternalContext ec = facesContext.getExternalContext();
			ec.responseReset();

			String path = scontext.getRealPath("/WEB-INF/relatorios/");
			parametros.put("SUBREPORT_DIR", path + File.separator);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					scontext.getRealPath("/WEB-INF/relatorios")
							+ File.separator + nomeRelatorio + ".jasper",
					parametros, dataSource);
			ec.setResponseHeader("Content-Disposition", "attachment; filename="
					+ nomeRelatorio + ".pdf");

			OutputStream output = ec.getResponseOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			output.flush();
			output.close();

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
