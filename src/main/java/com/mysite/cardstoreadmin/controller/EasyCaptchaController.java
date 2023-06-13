package com.mysite.cardstoreadmin.controller;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;

@RestController
@RequestMapping("/easy-Captcha")
public class EasyCaptchaController {
	
//	private LineCaptcha lineCaptcha;
	
	@RequestMapping("/generatorCode")
	public void generatorCode(HttpServletRequest request, HttpServletResponse response) {
		try {
//			Font font = new Font("楷體", Font.PLAIN, 28);
			Font font = new Font("Times New Roman", Font.PLAIN, 28);
			CaptchaUtil.out(5,font, request, response);
//			CaptchaUtil.out(5, request, response);
			String captcha = (String) request.getSession().getAttribute("captcha");
			System.out.println("驗證碼:" + captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/verify")
	public String verify(String verifyCode, HttpServletRequest request) {
		boolean flag = CaptchaUtil.ver(verifyCode, request);
		if (flag) {
			return "驗證碼相符";
		}
		return "驗證碼錯誤";
	}

}
