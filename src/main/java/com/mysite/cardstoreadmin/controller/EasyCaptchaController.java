package com.mysite.cardstoreadmin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wf.captcha.utils.CaptchaUtil;

@RestController
@RequestMapping("/easy-Captcha")
public class EasyCaptchaController {
	@RequestMapping("/generatorCode")
	public void generatorCode(HttpServletRequest request,HttpServletResponse response) {
	try {
	CaptchaUtil.out(request,response);
	}catch (IOException e) {
	e.printStackTrace();
	}
	}

	@GetMapping("/verify")
	public String verify(String verifyCode,HttpServletRequest request) {
	boolean flag =CaptchaUtil.ver(verifyCode, request);
	if (flag) {
	return "驗證碼相符";
	}
	return "驗證碼錯誤";
	}

}
