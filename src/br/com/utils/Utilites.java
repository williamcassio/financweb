package br.com.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;

public class Utilites {
	public Date retornaDataBase(int dia, Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.DAY_OF_MONTH, dia);
		int diaaux = cal.get(Calendar.DAY_OF_MONTH);
		int mesaux = cal.get(Calendar.MONTH);
		if (diaaux != dia) {
			mesaux = mesaux - 1;
			cal.set(Calendar.MONTH, mesaux);
			dia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, dia);
		}
		return cal.getTime();
	}

	public void downloadFile(String caminho) throws IOException {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment;filename=lancamento.xls");
		File file = new File(caminho);
		Faces.sendFile(file, true);
		FacesContext.getCurrentInstance().responseComplete();
		file.delete();

	}

}
