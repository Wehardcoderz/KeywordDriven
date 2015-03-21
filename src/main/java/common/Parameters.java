package common;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Parameters {
	private static Logger log = Logger.getLogger(Parameters.class);

	public Parameters() {

		PropertiesConfiguration config;
		File file = new File(Constants.LOG4J_PATH);
		if (!file.getAbsoluteFile().exists()) {
			System.out.println("Invalid log properties file!");
			System.exit(1);
		}
		 file = new File(Constants.OBJECT_REPOSITORY_PATH);
		if (!file.getAbsoluteFile().exists()) {
			System.out.println("Invalid log properties file!");
			System.exit(1);
		}
		//BasicConfigurator.configure();
		PropertyConfigurator.configure(Constants.LOG4J_PATH);

		try {
			config = new PropertiesConfiguration(Constants.CONFIG_PATH);
			config.getSubstitutor().setEnableSubstitutionInVariables(true);
		} catch (ConfigurationException e) {
			log.error("Unable to load config. Config file possibly missing.");
			e.printStackTrace();
		}

	}
}
