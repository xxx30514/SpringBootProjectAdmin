package com.mysite.cardstoreadmin.controller;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mysql.cj.Session;
import com.wf.captcha.utils.CaptchaUtil;

import lombok.extern.slf4j.Slf4j;
import com.mysite.cardstoreadmin.controller.utils.R;
import com.mysite.cardstoreadmin.param.UserCheckParam;
import com.mysite.cardstoreadmin.param.UserLoginParam;
import com.mysite.cardstoreadmin.pojo.User;
import com.mysite.cardstoreadmin.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	private CaptchaUtil captchaUtil;

	@PostMapping("/login")
	// 登入
	public R login(HttpServletRequest request, @RequestBody User user) {
		// 1.將頁面提交的密碼進行加密
		String password = user.getUserPassword();
		// password = DigestUtils.md5DigestAsHex(password.getBytes());
		// 2.根據提交的帳號查詢資料庫是否有該帳號
		LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
		lqw.eq(User::getUserAccount, user.getUserAccount());
		User userResult = userService.getOne(lqw);
		// 3.沒有該帳號返回登入失敗結果
		if (userResult == null) {
			return  R.fail("查無該帳號，請重新登入");
		}
		// 4.確認密碼一致,若不一致返回失敗
		if (!userResult.getUserPassword().equals(password)) {
			return R.fail("密碼錯誤，請重新登入");
		}
		// 5.判斷帳號是否啟用
		if (0 == userResult.getUserStatus()) {
			return R.fail("帳號未啟用，請洽詢客服人員");
		}
		// 6.登入成功 將使用者id存入session
		request.getSession().setAttribute("loginUser", userResult.getUserId());
		return R.success(userResult);
	}

	@PostMapping("/logout")
	// 登出
	public R logout(HttpServletRequest request) {
		// 清除session
		request.getSession().removeAttribute("loginUser");
		return R.success("登出成功");
	}

	@PostMapping
	public R save(@RequestBody User user) {
		log.info("新增使用者:{}", user.toString());
		return  R.success(userService.saveUser(user));
	}

	@PutMapping
	public R update(@RequestBody User user) {
		userService.updateUser(user);
		return  R.success("更新成功");
	}

	
	/**
	 * 檢查帳號是否已經存在
	 * 
	 * @param userCheckParam 接收檢查帳號的實體 內部有參數userAccount進行校驗
	 * @param result 校驗結果的實體
	 * @return 封裝好的json物件Result
	 */
	@PostMapping("/check")
	public R check(@RequestBody @Validated UserCheckParam userCheckParam, BindingResult result) {
		// 檢查是否符合校驗規則 符合false 不符合true
		boolean b = result.hasErrors();
		if (b) {
			return R.fail("請輸入您的帳號");
		}
		return userService.check(userCheckParam);
	}

	@PostMapping("/register")
	public R register(@RequestBody @Validated User user, BindingResult result) {
		boolean b = result.hasErrors();
		if (b) {
			return R.fail("註冊失敗，請稍後再試");
		}
		return userService.register(user);
	}

	@PostMapping("/newlogin")
	public R newlogin(@RequestBody @Validated UserLoginParam userLoginParam, BindingResult result) {
		// 檢查是否符合校驗規則 符合false 不符合true
		boolean b = result.hasErrors();
		if (b) {
			return R.fail("請輸入您的帳號");
		}
		return userService.login(userLoginParam);
	}
	//postman測試過
	@PostMapping("/newlogin2")
	public R newlogin2(@RequestBody @Validated UserLoginParam userLoginParam, BindingResult result, HttpServletRequest request) {
		// 檢查是否符合校驗規則 符合false 不符合true
		boolean b = result.hasErrors();
		if (b) {
			return R.fail("請輸入您的帳號與密碼");
		}
		System.out.println("000"+userLoginParam.getVerCode());
		System.out.println((String)request.getSession().getAttribute("captcha"));
		if (!captchaUtil.ver(userLoginParam.getVerCode(), request)) {
			return R.fail("驗證碼錯誤");
		}
		//request.setAttribute("loginUser", userLoginParam.getUserAccount());
		Object attribute = request.getAttribute("loginUser");
		System.out.println("abc"+attribute);//null	
		return userService.login2(request, userLoginParam);
	}
	
	@RequestMapping("/generatorCode")
	public void generatorCode(HttpServletRequest request, HttpServletResponse response) {
		try {
//			Font font = new Font("楷體", Font.PLAIN, 28);
			Font font = new Font("Times New Roman", Font.PLAIN, 28);
			captchaUtil.out(5,font, request, response);
//			CaptchaUtil.out(5, request, response);
			String captcha = (String) request.getSession().getAttribute("captcha");
			System.out.println("驗證碼:" + captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
