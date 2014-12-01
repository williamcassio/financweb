package br.com.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.com.dao.ModalidadeDAO;
import br.com.entity.Modalidade;

public class ModalidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String string) {
		if (string == null || string.equals("")){
			return null;
		}
		ModalidadeDAO dao = new ModalidadeDAO();
		return dao.carregarPorId(Long.parseLong(string));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj == null){
			return null;
		}
		Modalidade retorno = (Modalidade) obj;
		return retorno.getId().toString();
	}

}
