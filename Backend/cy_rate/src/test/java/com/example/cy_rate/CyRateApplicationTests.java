package com.example.cy_rate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.cy_rate.Business.Business;
import com.example.cy_rate.Business.BusinessRepository;
import com.example.cy_rate.Review.Review;
import com.example.cy_rate.Review.ReviewRepository;
import com.example.cy_rate.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.example.cy_rate.User.User;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class CyRateApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private BusinessRepository busRepo;
	@Autowired
	private ReviewRepository revRepo;
	@Autowired
	private UserRepository userRepo;


	@LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}
	
	/**
	 * Tests createReview() for review controller
	 * checks for ok status (200)
	 * checks most recent review in repo by business, if it's ours then it worked
	 * deletes test review from db
	 */
	@Test
	public void createReviewTest()
	{
		// Review tester = new Review(5, "This is a great place to eat", "Favorite Location");
		// Used for retrieving results from db, using it post string
		Business testBusiness = busRepo.findById(3);
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("{\"rateVal\": 5, \"reviewTxt\": \"This is a great place to eat\", \"reviewHeader\": \"Favorite Location\"}").
				when().
				post("/review/3/user/7/createReview");
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
		
		// Get most recent entry to database, aka one we just made
		List<Review> rows = revRepo.findByBusiness(testBusiness);
		Review createdReview = rows.get(rows.size() - 1);

		assertEquals("This is a great place to eat", createdReview.getReviewTxt());
		// Delete Created Repo from database
		revRepo.delete(createdReview);
	}

	/**
	 * Tests for status 200 connection when hitting /review/bid/user/uid/createReview
	 * Tests correct update of data then returns it to it's original state
	 */
	@Test
	public void updateReviewTest()
	{
		Review testReview = revRepo.findById(16);
		String originalReviewTxt = testReview.getReviewTxt();
		String originalReviewHeader = testReview.getReviewHeader();
		//revRepo.save(testReview);

		Response response = RestAssured.given().
			header("Content-Type", "application/json").
			header("charset","utf-8").
			body("{\"rateVal\": 5, \"reviewTxt\": \"" +  originalReviewTxt + "(Updated)" + "\", \"reviewHeader\": \"" + originalReviewHeader + "(Updated)" + "\"}").
			when().put("/reveiw/update/16");
	
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		// compare original to new
		Review afterPostReview = revRepo.findById(16);
		// added a 1 to end of string above
		assertEquals(originalReviewTxt + "(Updated)", afterPostReview.getReviewTxt());
		// added (Updated) to end of string above
		assertEquals(originalReviewHeader + "(Updated)", afterPostReview.getReviewHeader());

		// changed db back to original
		afterPostReview.setReviewTxt(originalReviewTxt);
		afterPostReview.setReviewHeader(originalReviewHeader);
		revRepo.save(testReview);
	}

	/**
	 * creates a review and saves it to repo
	 * makes request to delete it
	 * tests that it no longer exists in database
	 * tests for status ok (200)
	 */
	@Test
	public void deleteReviewTest()
	{
		// Create and save new repo to repo
		Review testReview = new Review(5, "testing review for deleteReviewTest()", "test header");
		testReview.setBusiness(busRepo.findById(3));
		testReview.setUser(userRepo.findById(7));

		testReview = revRepo.save(testReview);
		int testReviewId = testReview.getRid();

		// make request to delete new review
		Response response = RestAssured.given().
			header("Content-Type", "application/json").
			header("charset","utf-8").
			body("").
			when().delete("review/delete/"+ testReviewId);

		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		// repo should no longer have review will return null
		assertEquals(null, revRepo.findById(testReviewId));

	}

	/**
	 * Gathers a list of reviews by a certain user
	 * then makes request to get some list
	 * compares the two
	 * also checks for status ok
	 */
	@Test
	public void getReviewsByUser()
	{
		List<Review> queriedList = revRepo.findByUser(userRepo.findById(7));
		ObjectMapper om = new ObjectMapper();

		Response response = RestAssured.given().
			header("Content-Type", "application/json").
			header("charset","utf-8").
			body("").
			when().get("reviews/user/7");
		try{
			List<Review> reviewsAfterReq = om.readValue(response.body().toString(),  new TypeReference<List<Review>>(){});
			assertEquals(queriedList, reviewsAfterReq);
		}
		catch(Exception e)
		{
		}
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);


	}
}
