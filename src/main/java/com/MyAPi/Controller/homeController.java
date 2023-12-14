package com.MyAPi.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MyAPi.Entity.Customer;
import com.MyAPi.Entity.LoginCred;
import com.MyAPi.Service.ApiService;

@Controller
@RequestMapping("/myapi/v1")
public class homeController {

	@Autowired
	private ApiService apiService;

	private String token;
	
	private String updateUuid;

	@PostMapping("/login")
	public String login(@ModelAttribute LoginCred login) {
		if (login.getLoginId().equals("test@sunbasedata.com") && login.getPassword().equals("Test@123")) {
			token = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";
			return "redirect:/myapi/v1/home";
		} else {
			return "redirect:/login";
		}

	}

	// all done
	@GetMapping("/home")
	public String home(Model model) {
		// fetching all customers

		List<Customer> list = apiService.sendRequestToOtherApi(token, "cmd", "get_customer_list", HttpMethod.GET);

		for (Customer customer : list) {
			System.out.println(customer);
		}
		model.addAttribute("customers", list);
		return "home";
	}

	// WORKING PROPERLY
	// all done
	@PostMapping("/saveCutomer")
	public String saveCustomer(@ModelAttribute Customer customer, Model model) {
		System.out.println(customer.toString() + " yeh hai apna customer");
		if (customer.getFirst_name() != null && customer.getLast_name() != null) {
			String sendRequestToOtherApi = apiService.saveCustomerToApi(token, "cmd", "create", customer).trim();
			// model.addAttribute("msg", sendRequestToOtherApi);
			ResponseEntity.status(HttpStatus.CREATED);

		}
		return "redirect:/myapi/v1/home";
	}

	@GetMapping("/updateUser")
	public String updateuser(@RequestParam("uuid") String uuid,Model model) {
		model.addAttribute("uuid",uuid);
		updateUuid=uuid;
		System.out.println("my uuid is"+uuid);
		return "update";
	}
	
	// working properly
	@PostMapping("/update")
	public String updateCustomer(@ModelAttribute Customer customer) {
		System.out.println("inside update and uuid is now"+customer.getUuid());
		customer.setUuid(updateUuid);
		System.out.println(customer.toString());
		if ( customer.getFirst_name() != null && customer.getLast_name() != null && customer.getUuid() != null) {
			String sendRequestToOtherApi = apiService.updateCustomerToApi(token, "cmd", "update", customer.getUuid(), customer)
					.trim();
			System.out.println(sendRequestToOtherApi);
			// model.addAttribute("msg", sendRequestToOtherApi);
		}
		return "redirect:/myapi/v1/home";
	}

	@PostMapping("/delete")
	public String deleteCustomer(@RequestParam("uuid") String uuid) {
		String deleteCustomerToApi = apiService.deleteCustomerToApi(token, uuid).trim();

		System.out.println(deleteCustomerToApi);
		return "redirect:/myapi/v1/home";
	}

	// getters and setters
	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
