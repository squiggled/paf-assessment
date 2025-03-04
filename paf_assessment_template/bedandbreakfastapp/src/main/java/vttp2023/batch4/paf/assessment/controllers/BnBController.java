package vttp2023.batch4.paf.assessment.controllers;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;
import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;
import vttp2023.batch4.paf.assessment.services.ListingsService;
import vttp2023.batch4.paf.exceptions.BookingFailedException;
import vttp2023.batch4.paf.assessment.Utils;

@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BnBController {

	// You may add additional dependency injections

	@Autowired
	private ListingsService listingsSvc;

	@Autowired
	private BookingsRepository bookingsRepo;
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/suburbs")
	@ResponseBody
	public ResponseEntity<String> getSuburbs() {
		List<String> suburbs = listingsSvc.getAustralianSuburbs();
		JsonArray result = Json.createArrayBuilder(suburbs).build();
		return ResponseEntity.ok(result.toString());
	}
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<String> search(@RequestParam MultiValueMap<String, String> params) {

		String suburb = params.getFirst("suburb");
		int persons = Integer.parseInt(params.getFirst("persons"));
		int duration = Integer.parseInt(params.getFirst("duration"));
		float priceRange = Float.parseFloat(params.getFirst("price_range"));

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		listingsSvc.findAccommodatations(suburb, persons, duration, priceRange)
			.stream()
			.forEach(acc -> 
				arrBuilder.add(
					Json.createObjectBuilder()
						.add("id", acc.getId())
						.add("name", acc.getName())
						.add("price", acc.getPrice())
						.add("accommodates", acc.getAccomodates())
						.build()
				)
			);

		return ResponseEntity.ok(arrBuilder.build().toString());
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/accommodation/{id}")
	@ResponseBody
	public ResponseEntity<String> getAccommodationById(@PathVariable String id) {

		Optional<Accommodation> opt = listingsSvc.findAccommodatationById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(Utils.toJson(opt.get()).toString());
	}

	// TODO: Task 6
	// @PostMapping("/accommodation")
	@PostMapping(path = "/accommodation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createBooking(@RequestBody String posting){
		Reader reader = new StringReader(posting); 

        JsonReader jsonReader = Json.createReader(reader);

        JsonObject j = jsonReader.readObject(); 
        
        String id = j.getString("id"); 
        String email = j.getString("email");
        Integer nights = j.getInt("nights");
        String name = j.getString("name");
		Bookings booking = new Bookings();
		booking.setListingId(id);
		booking.setEmail(email);
		booking.setDuration(nights);
		booking.setName(name);

		Optional<User> userOpt = bookingsRepo.doesUserExist(email);
		if (!userOpt.isPresent()){
			User user = new User(email, name);
			
			try {
				listingsSvc.createUser(user);
			} catch (BookingFailedException e){
				System.out.println("Unable to add user: " + e.getMessage());
				JsonObject msg = Json.createObjectBuilder().add("message", "unable to add user").build();
				return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(msg.toString());
			}
		}
		try {
			listingsSvc.createBooking(booking);
		} catch (BookingFailedException e) {
			System.out.println("Unable to book: " + e.getMessage());
				JsonObject msg = Json.createObjectBuilder().add("message", "unable to book").build();
				return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(msg.toString());
		}
		
		return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{}");
	}
	
}
