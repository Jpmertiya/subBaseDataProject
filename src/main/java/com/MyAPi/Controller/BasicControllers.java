package com.MyAPi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicControllers {

	@GetMapping("/")
	public String home() {
		return "index";
	}

}
