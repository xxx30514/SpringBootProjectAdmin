package com.mysite.cardstoreadmin.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import cn.hutool.captcha.CaptchaUtil;
//import cn.hutool.captcha.LineCaptcha;

@RestController
@RequestMapping("/tests")
public class TestController {
	
	@GetMapping("/getCode")
	public void getCode(HttpServletResponse response) {
//		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(130, 48, 5, 5);
//		System.out.println("驗證碼"+lineCaptcha.getCode());
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		try {
			ServletOutputStream outputStream = response.getOutputStream();
//			lineCaptcha.write(outputStream);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
