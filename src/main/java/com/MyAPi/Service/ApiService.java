package com.MyAPi.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.MyAPi.Entity.Customer;
import com.MyAPi.Entity.LoginCred;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class ApiService {
	private final String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	@Autowired
	private final Gson gson;

	Logger logger = LoggerFactory.getLogger(ApiService.class);

	// Authenticating
//ufff
	public String generateToken(LoginCred login) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestBody = gson.toJson(login);

		logger.info(requestBody);

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		System.out.println("inside api service" + requestEntity);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
				String.class);

		// Handle the response
		String responseBody = responseEntity.getBody();
		return responseBody;
	}

	// used to save Customer
	public String saveCustomerToApi(String token, String paramKey, String paramValue, Customer customer) {

		System.out.println("inside save customer api call");
		// Set up the request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + token);
		// Convert the Java object to JSON string using Gson

		String urlWithParams = apiUrl + "?" + paramKey + "=" + paramValue;
		String requestBody = gson.toJson(customer);

		// Create the HTTP entity with headers and body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Make the HTTP POST request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithParams, HttpMethod.POST, requestEntity,
				String.class);

		// Handle the response
		String responseBody = responseEntity.getBody();
		// You can process the responseBody or return it as needed

		return responseBody;
	}

	// Performing get operations
	public List<Customer> sendRequestToOtherApi(String bearerToken, String paramKey, String paramValue, HttpMethod httpMethod) {
		// setting header of url
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + bearerToken);
		// building dynamic url
		String urlWithParams = apiUrl + "?" + paramKey + "=" + paramValue;

		// creating HTTP entity with headers and body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Make the HTTP request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithParams, httpMethod, requestEntity,
				String.class);

		// Handle the response
		ObjectMapper mapper = new ObjectMapper();
		
		List<Customer> customers=new ArrayList<>();
		try {
			 customers = Arrays.asList(mapper.readValue(responseEntity.getBody(), Customer[].class));

		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		return customers;
	}

	// updating Customers
	public String updateCustomerToApi(String token, String paramKey, String paramValue, String uuid,
			Customer customer) {

		System.out.println("inside save customer api call");
		// Set up the request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + token);
		// Convert the Java object to JSON string using Gson
		String requestBody = gson.toJson(customer);
		String urlWithParams = apiUrl + "?" + paramKey + "=" + paramValue + "&uuid=" + uuid;

		// Create the HTTP entity with headers and body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Make the HTTP POST request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithParams, HttpMethod.POST, requestEntity,
				String.class);

		// Handle the response
		String responseBody = responseEntity.getBody();
		// You can process the responseBody or return it as needed
		System.out.println(requestBody);
		return responseBody;
	}

	public String deleteCustomerToApi(String token, String uuid) {
		String key = "cmd";
		String keyvalue = "delete";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + token);
		String urlWithParams = apiUrl + "?" + key + "=" + keyvalue + "&uuid=" + uuid;

		HttpEntity<String> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, entity, String.class);

		String responseBody = response.getBody();
		return responseBody;

	}

	public ApiService(Gson gson) {
		this.gson = gson;
	}

}
