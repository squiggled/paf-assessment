package vttp2023.batch4.paf.assessment.services;

import java.io.Reader;
import java.io.StringReader;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class ForexService {

	String URL_API = "https://api.frankfurter.app/latest";
	RestTemplate template = new RestTemplate();

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		String response = template.getForObject(URL_API, String.class);
		if (response==null){
			return -1000;
		}
			Reader reader = new StringReader(response);
			JsonReader jsonReader = Json.createReader(reader);
			JsonObject obj = jsonReader.readObject();
			JsonObject ratesObj = obj.getJsonObject("rates");
		
			double fromRate = ratesObj.getJsonNumber("AUD").doubleValue();
			double toRate = ratesObj.getJsonNumber("SGD").doubleValue();
	
			return (float) ((amount / fromRate) * toRate);

	}
}
