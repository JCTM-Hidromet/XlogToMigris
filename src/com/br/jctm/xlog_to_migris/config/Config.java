package com.br.jctm.xlog_to_migris.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Config configInstance;
	
	private String parametrosXLog;
	private String parametrosMigris;
	private String prefixoMigris;
	private String prefixoXLog;
	private String diretorioXLog;
	private String diretorioMigris;
	private String cabecalhoMigris;
	
	
	//Constructor
	private Config(){
		String propertiesFile = ".\\config\\config.properties";
		Properties props = new Properties();
		
		try {
			File fConfig				=	new File(propertiesFile);
			FileInputStream flInptStrm	=	new FileInputStream(fConfig);
			props.load(flInptStrm);
			
			parametrosXLog				=	props.getProperty("config.parametrosXLog");
			parametrosMigris			=	props.getProperty("config.parametrosMigris");
			prefixoMigris				=	props.getProperty("config.prefixoMigris");
			prefixoXLog					=	props.getProperty("config.prefixoXLog");
			diretorioXLog				=	props.getProperty("config.diretorioXLog");
			diretorioMigris				=	props.getProperty("config.diretorioMigris");
			cabecalhoMigris				=	props.getProperty("config.cabecalhoMigris");
					
		} catch (IOException e) {
			// TODO: handle exception
		}
		
	}

	//getters and setters
	public String getParametrosXLog() {
		return parametrosXLog;
	}

	public String getParametrosMigris() {
		return parametrosMigris;
	}

	public String getPrefixoMigris() {
		return prefixoMigris;
	}

	public String getPrefixoXLog() {
		return prefixoXLog;
	}

	public String getDiretorioXLog() {
		return diretorioXLog;
	}

	public String getDiretorioMigris() {
		return diretorioMigris;
	}
	
	public String getCabecalhoMigris() {
		return cabecalhoMigris;
	}

	
	//singleton get config instance
	public static Config getConfigInstance() {
		if( configInstance == null ){
			configInstance = new Config();
			return configInstance;
		}else{
			return configInstance;
		}
	}

	
}
