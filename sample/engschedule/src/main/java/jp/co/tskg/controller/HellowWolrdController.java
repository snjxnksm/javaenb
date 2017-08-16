package jp.co.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HellowWolrdController {

	// トップページの表示
	@RequestMapping("/")
	String home() {
		return "index.html";
	}

}
