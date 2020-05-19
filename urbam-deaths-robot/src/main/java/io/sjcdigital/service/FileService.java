package io.sjcdigital.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.sjcdigital.model.Person;

@ApplicationScoped
public class FileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	
	@ConfigProperty(name = "file.path")
	private String path;
	
	public void saveAsJsonFile(final String year, final Map<String, List<Person>> deaths) {
		
		String directoryName = path + year + "/json/";
		createDirectoryIfDoesntExists(directoryName);
		
		deaths.forEach((k, v) -> createJsonFile(directoryName, k, v) );
	}

	private void createJsonFile(String directoryName, String month, List<Person> deathPersons) {
		
		try {
			
			Files.write(Paths.get(directoryName + month + ".json"), new ObjectMapper().writeValueAsString(deathPersons).getBytes());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDirectoryIfDoesntExists(String directoryName) {
		
		LOGGER.info("Files will be save into " + directoryName + " if you need change it, replace the 'file.path' argument on application.properties file");
		
		File directory = new File(directoryName);
		
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}

}
