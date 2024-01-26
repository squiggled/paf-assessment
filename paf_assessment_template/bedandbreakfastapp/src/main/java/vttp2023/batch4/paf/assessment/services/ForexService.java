package vttp2023.batch4.paf.assessment.services;

import java.io.Reader;
import java.io.StringReader;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	String URL_API = "https://api.frankfurter.app/latest";
	RestTemplate template = new RestTemplate();

	// TODO: Task 5 
	public float convert(String fromA, String toS, float amount) {
		String response = template.getForObject(URL_API, String.class);
		Reader reader = new StringReader(response);
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject obj = jsonReader.readObject();
		JsonObject ratesObj = obj.getValue(JsonObject.class);
		JsonObject pokemon : jsonArray.getValuesAs(JsonObject.class))
		Double aud = Double.parseDouble(ratesObj.asJsonObject().get("AUD"));
		Float sgd = ratesObj.get
		return ((aud/sgd) * amount);
		/*
		 
  "amount": 1.0,
  "base": "EUR",
  "date": "2024-01-25",
  "rates": {
    "AUD": 1.6537,
    "BGN": 1.9558,
    "BRL": 5.3664,
    "CAD": 1.4714,
    "CHF": 0.942,
		 */
	}
}
