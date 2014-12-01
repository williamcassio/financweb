package br.com.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.entity.Lancamento;

public class GeraPlanilha {
	
	public void montarPlanilha(String localArquivo, String usuario, String periodo, List<Lancamento> lista){
		int linha = 0;
		int coluna = 0;
		Workbook wb;
		Sheet sheet;
		CellStyle estilo;
		Cell cell;
		Row row;
		wb = criarPlanilha();
		sheet = criarAba("Lançamentos", wb);
		row = sheet.createRow(linha);
		cell = inserirCelulaTexto(linha, 0, "Usuário", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = alinhamentoADireita(estilo, wb);
		cell.setCellStyle(estilo);
		cell = inserirCelulaTexto(linha, 1, usuario, sheet, row);
		estilo = fonteNegrito(null, wb);
		cell.setCellStyle(estilo);		
		linha = 1;
		row = sheet.createRow(linha);
		cell = inserirCelulaTexto(linha, 0, "Período", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = alinhamentoADireita(estilo, wb);
		cell.setCellStyle(estilo);
		cell = inserirCelulaTexto(linha, 1, periodo, sheet, row);
		estilo = fonteNegrito(null, wb);
		cell.setCellStyle(estilo);		
		
		linha = 2;
		// Inserindo Colunas são elas: Tipo, Modalidade, Data, Valor, Obs
		row = sheet.createRow(linha);
		cell = inserirCelulaTexto(linha, coluna, "Receita/Despesa", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = estiloBorda(estilo, wb);
		estilo = alinhamentoCentralizadoVertical(estilo, wb);
		cell.setCellStyle(estilo);
		coluna = 1;
		cell = inserirCelulaTexto(linha, coluna, "Modalidade", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = estiloBorda(estilo, wb);
		estilo = alinhamentoCentralizadoVertical(estilo, wb);
		cell.setCellStyle(estilo);		
		coluna = 2;
		cell = inserirCelulaTexto(linha, coluna, "Data", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = estiloBorda(estilo, wb);
		estilo = alinhamentoCentralizadoVertical(estilo, wb);
		cell.setCellStyle(estilo);
		coluna = 3;
		cell = inserirCelulaTexto(linha, coluna, "Valor", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = estiloBorda(estilo, wb);
		estilo = alinhamentoCentralizadoVertical(estilo, wb);
		cell.setCellStyle(estilo);
		coluna = 4;
		cell = inserirCelulaTexto(linha, coluna, "Obervação", sheet, row);
		estilo = fonteNegrito(null, wb);
		estilo = estiloBorda(estilo, wb);
		estilo = alinhamentoCentralizadoVertical(estilo, wb);
		cell.setCellStyle(estilo);
		
		linha = linha +1;
		for(Lancamento lanc : lista){
			row = sheet.createRow(linha);
			cell = inserirCelulaTexto(linha, 0, lanc.getModalidade().getTipoOperacao().toString(), sheet, row);
			estilo = estiloBorda(null, wb);
			cell.setCellStyle(estilo);
			cell = inserirCelulaTexto(linha, 1, lanc.getModalidade().getDescricao(), sheet, row);
			estilo = estiloBorda(null, wb);
			cell.setCellStyle(estilo);			
			cell = inserirCelulaData(linha, 2, lanc.getData(), sheet, row);
			estilo = estiloBorda(null, wb);
			estilo = alinhamentoCentralizado(estilo, wb);
			cell.setCellStyle(estilo);			
			cell = inserirCelulaDouble(linha, 3, lanc.getValor(), sheet, row);
			estilo = estiloBorda(null, wb);
			estilo = formatoNumerico(estilo, wb);
			cell.setCellStyle(estilo);
			cell = inserirCelulaTexto(linha, 4, lanc.getObservacao(), sheet, row);
			estilo = estiloBorda(null, wb);
			cell.setCellStyle(estilo);			
			linha++;
		}
		
		setarTamanhoColuna(sheet, 20, 0);
		setarTamanhoColuna(sheet, 40, 1);
		setarTamanhoColuna(sheet, 12, 2);
		setarTamanhoColuna(sheet, 10, 3);
		setarTamanhoColuna(sheet, 80, 4);
		
		gravarArquivo(wb, localArquivo);
	}
	
	public String localPlanilhas(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext
				.getExternalContext().getContext();		
		String path = scontext.getRealPath("/planilhas/");
		if (!new File(path).exists()){
			(new File(path)).mkdir();
		}
		return path;
	}

	public void gravarArquivo(Workbook wb, String localArquivo){
        FileOutputStream out;
		try {
			out = new FileOutputStream(localArquivo);
			wb.write(out);
	        out.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public Workbook criarPlanilha(){
		Workbook wb = new XSSFWorkbook();
		return wb;
	}
	
	public Sheet criarAba(String nomeAba, Workbook wb){
		Sheet sheet = wb.createSheet(nomeAba);
		return sheet;
	}
	
	
	public CellStyle formatoNumerico(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setDataFormat(wb.createDataFormat().getFormat("##,##0.00"));
		return estilo;
	}
	
	public CellStyle alinhamentoCentralizadoVertical(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return estilo;
	}	
	
	public CellStyle alinhamentoCentralizado(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setAlignment(CellStyle.ALIGN_CENTER);
		return estilo;
	}
	
	public CellStyle textoQuebrado(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setWrapText(true);
		return estilo;
	}
	
	public CellStyle alinhamentoADireita(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setAlignment(CellStyle.ALIGN_RIGHT);
		return estilo;
	}	
	
	public CellStyle alinhamentoAEsquerda(CellStyle estilo, Workbook wb){
		if (estilo == null){
			  estilo = wb.createCellStyle();
			}
		estilo.setAlignment(CellStyle.ALIGN_LEFT);
		return estilo;
	}	
	
	public CellStyle fonteNegrito(CellStyle estilo, Workbook wb){
		if (estilo == null){
		  estilo = wb.createCellStyle();
		}
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		estilo.setFont(font);
		return estilo;
	}
	
	public CellStyle fonteNegritoVermelho(CellStyle estilo, Workbook wb){
		if (estilo == null){
			estilo = wb.createCellStyle();
		}
		Font font = wb.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		estilo.setFont(font);
		return estilo;
	}	
	
	public CellStyle estiloBorda(CellStyle estilo, Workbook wb){
		if (estilo == null){
			estilo = wb.createCellStyle();
		}
        estilo.setBorderRight(CellStyle.BORDER_THIN);
        estilo.setBorderLeft(CellStyle.BORDER_THIN);
        estilo.setBorderTop(CellStyle.BORDER_THIN);
        estilo.setBorderBottom(CellStyle.BORDER_THIN);
        return estilo;
	}
	
	public Cell inserirCelulaTexto(int linha, int coluna, String texto, Sheet sheet, Row row){
		Cell cell2;
        cell2 = row.createCell(coluna);
        cell2.setCellValue(texto);
        return cell2;
      
	}
	
	public Cell inserirCelulaNumerica(int linha, int coluna, Float numero, Sheet sheet, Row row){
		Cell cell2;
        cell2 = row.createCell(coluna);
        cell2.setCellValue(numero);
        return cell2;
	}
	
	public Cell inserirCelulaDouble(int linha, int coluna, Double numero, Sheet sheet, Row row){
		Cell cell2;
        cell2 = row.createCell(coluna);
        cell2.setCellValue(numero);
        return cell2;
	}	

	public Cell inserirCelulaData(int linha, int coluna, Date data, Sheet sheet, Row row){
		Cell cell2;
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = sf.format(data);
        cell2 = row.createCell(coluna);
        cell2.setCellValue(dataFormatada);
        return cell2;
	}	
	
	
	public void setarTamanhoColuna(Sheet sheet, int tamanho, int coluna){
		sheet.setColumnWidth(coluna, tamanho * 256); 
	}
}