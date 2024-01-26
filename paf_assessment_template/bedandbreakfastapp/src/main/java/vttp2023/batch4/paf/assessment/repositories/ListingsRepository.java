package vttp2023.batch4.paf.assessment.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * db.listings.find(
		{"address.country": {$regex: "australia", $options: "i"}},
		{"address.suburb": 1, '_id': 0});
	 */
	public List<String> getSuburbs(String country) {
		Query q = new Query();
		q.addCriteria(Criteria.where("address.country").regex(country, "i"))
			.fields().include("address.suburb").exclude("_id");
		List<Document> address = template.find(q, Document.class, "listings");
		List<String> suburbs = new ArrayList<>();
		for (Document addDoc : address){
			Document subDoc = addDoc.get("address", Document.class);
			String oneSub = subDoc.get("suburb", String.class);
			// System.out.println("onesub" + oneSub);
			if ((!oneSub.equals(""))){
				suburbs.add(oneSub);
			}	
		}
		return suburbs;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 db.listings.find(
		{
			{"address.suburb": {$regex: "lily", $options: "i"}, 
		{price: {$lte:: 50}, 
		{accommodates: {$gte:: 4}, 
		{min_nights: $lte: 5}
		},
{"name": 1, 'accommodates': 1, "price": 1});
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		Query q = new Query();
		q.addCriteria(Criteria.where("address.suburb").regex(suburb, "i")
				.and("price").lte(priceRange)
				.and("accommodates").gte(persons)
				.and("min_nights").lte(duration))
				.with(Sort.by(Direction.DESC, "price"));
		q.fields().include("_id", "name", "accommodates", "price");
		List<Document> listingsRes = template.find(q, Document.class, "listings");
		List<AccommodationSummary> listings = new ArrayList<>();
		for (Document listingDoc : listingsRes){
			AccommodationSummary accom = new AccommodationSummary();
			accom.setId(listingDoc.getString("_id"));
			accom.setName(listingDoc.getString("name"));
			accom.setAccomodates(listingDoc.getInteger("accommodates"));
			accom.setPrice(listingDoc.get("price", Number.class).floatValue());
			listings.add(accom);
		}
		return listings;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
