package com.project.api.clients.security.utilities;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Utility.class);
	
	public static LocalDateTime generateCurrentDate() {
		try {
			LOGGER.info("######### - Retorna la fecha y hora actuales en formato LocalDateTime - #########");
			 return LocalDateTime.now();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			LOGGER.error("######### - Error - #########");
			LOGGER.error(ex.getMessage());
			return null;
		}
	}
	
	public static String generatedAlias(String name) {
		LOGGER.info("######### - Se hace un split al parámetro BussinessID - #########");
		String[] split = name.split(" ");
		LOGGER.info("######### - Se retorna el Alias creado - #########");
		return split[0].charAt(0) + split[1];
	}

}
