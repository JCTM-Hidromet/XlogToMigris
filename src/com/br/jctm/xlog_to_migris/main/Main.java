package com.br.jctm.xlog_to_migris.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.br.jctm.xlog_to_migris.config.Config;


public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		String pfxXlog			=	Config.getConfigInstance().getPrefixoXLog();
		String caminhoXlog		=	Config.getConfigInstance().getDiretorioXLog();
		String linhaParamXlog	=	Config.getConfigInstance().getParametrosXLog();
		String linhaParamMigris	= 	Config.getConfigInstance().getParametrosMigris();
		String cabecalho 		=	Config.getConfigInstance().getCabecalhoMigris();
		String[] xlogParams		=	extrairParametros(linhaParamXlog);
		String[] migParams		=	extrairParametros(linhaParamMigris);
		File[] diretorioXlog	=	selecionarArquivos(caminhoXlog, pfxXlog);
		
		for (File arquivoXlog : diretorioXlog) {
			FileReader fldr				=	new FileReader(arquivoXlog);
			BufferedReader bfdr 		=	new BufferedReader(fldr);
			ArrayList<String> valores	=	new ArrayList<String>();
			String linha				=	bfdr.readLine();
						
			while (linha != null){
				String data				=	extrairData(linha);
				ArrayList<String> dados	= 	extrairValores(xlogParams, linha);
				
				if (dados.size() > 0){
					valores.add(data);
					valores.addAll(dados);
					gravaMigris(migParams, valores, cabecalho);
				}
				
				linha	=	bfdr.readLine();
			}
			
			bfdr.close();
			fldr.close();
			
		}
			
	}

	
	private static File[] selecionarArquivos(String caminhoDiretorio, String prefixoXlog){
		File dirXlog	=	new File(caminhoDiretorio);
		
		if(!dirXlog.isDirectory()){
			return null;
			
		}else{
			File[] ArquivosXlog	=	dirXlog.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return (pathname.getName().startsWith(prefixoXlog) && pathname.getName().endsWith(".txt"));
				}
				
			});
			
			return ArquivosXlog;
			
		}
	
	}
	
	
	private static String extrairData(String linha) throws ParseException {
		Pattern padraoData		=	Pattern.compile("\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{2}\\,\\d{4}");
		Matcher dataOcorrencia	=	padraoData.matcher(linha);
		String dataSaida		=	null;
		
		if(dataOcorrencia.find()){
			String data				=	dataOcorrencia.group();
			SimpleDateFormat sdf	=	new SimpleDateFormat("HH,mm,ss,dd,MM,yyyy");
			Date dataHora			=	sdf.parse(data);
			sdf						=	new SimpleDateFormat("dd/MM/yyyy HH:mm");
			dataSaida				=	sdf.format(dataHora);
		}
		
		return dataSaida;
		
	}
	
		
	private static ArrayList<String> extrairValores(String[]parametros , String linha) throws ParseException {
		ArrayList<String> lstValores	=	new ArrayList<String>();
		
		for (String param : parametros) {
			Pattern padraoValor		=	Pattern.compile(param);
			Matcher valorOcorrencia = 	padraoValor.matcher(linha);
			
			if( valorOcorrencia.find()){
				int inicio		=	Integer.valueOf(valorOcorrencia.end())+1;
				int fim			=	inicio+1;
				int cont		=	1;
				String teste	=	linha.substring(inicio, fim );
				
				while( !teste.equals(",")){
					inicio++;
					fim++;
					cont++;
					teste = linha.substring(inicio, fim );
				}
				
				lstValores.add(linha.substring(valorOcorrencia.end()+1, valorOcorrencia.end()+cont));
				
			}
			
		}
		
		return lstValores;
		
	}

	
	private static void gravaMigris(String[] migrisParams, ArrayList<String>valores, String cabecalho){
		String dataParam = valores.get(0);
		
		if(!cabecalho.equals("")){
			System.out.println(cabecalho);
		}
		
		for (int i = 1 ; i < migrisParams.length ; i++){
			System.out.println(dataParam + ";" + migrisParams[i] + ";" + valores.get(i).toString() + ";;;");
		}
			
	}
	
	private static String[] extrairParametros (String linhaParametros){
		String separadorParametros = ";";
		
		return linhaParametros.split(separadorParametros);
		
	}

}
