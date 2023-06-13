package com.mysite.cardstoreadmin.service.impl;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mysite.cardstoreadmin.controller.utils.R;
import com.mysite.cardstoreadmin.mapper.UserMapper;
import com.mysite.cardstoreadmin.param.UserCheckParam;
import com.mysite.cardstoreadmin.param.UserLoginParam;
import com.mysite.cardstoreadmin.pojo.User;
import com.mysite.cardstoreadmin.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public Boolean saveUser(User user) {

		user.setUserCreatedate(LocalDateTime.now());
		user.setUserUpdatedate(LocalDateTime.now());
		return userMapper.insert(user) > 0;
	}

	@Override
	public Boolean updateUser(User user) {
		user.setUserUpdatedate(LocalDateTime.now());
		return userMapper.updateById(user) > 0;
	}

	@Override
	public R check(UserCheckParam userCheckParam) {
		// 參數封裝
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("user_account", userCheckParam.getUserAccount());
		// 資料庫查詢
		Long total = userMapper.selectCount(query);
		if (total == 0) {
			// 沒有該帳號存在 可以註冊
			return R.success("可使用的帳號");
		}
		return R.fail("該帳號已存在");
	}

	/**
	 * 1.檢查帳號是否存在 2.密碼加密 3.新增資料 4.返回結果封裝
	 */
	@Override
	public R register(User user) {
		// 1.檢查帳號是否存在
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("user_account", user.getUserAccount());
		// 資料庫查詢
		Long total = userMapper.selectCount(query);
		if (total > 0) {
			// 帳號已重複
			return  R.fail("該帳號已存在");
		}
		user.setUserCreatedate(LocalDateTime.now());
		user.setUserUpdatedate(LocalDateTime.now());
		int rows = userMapper.insert(user);

		if (rows == 0) {
			return R.fail("註冊失敗，請稍後再試");
		}
		return  R.success("註冊成功");
	}

	@Override
	public R login(UserLoginParam userLoginParam) {
		// 1.密碼處理
		String password = userLoginParam.getUserPassword();
		// 2.資料庫查詢
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("user_account", userLoginParam.getUserAccount());
		// queryWrapper.eq("user_password", userLoginParam.getUserPassword());
		User userResult = userMapper.selectOne(query);
		// 3.結果處理
		// 4.沒有該帳號返回登入失敗結果
		if (userResult == null) {
			return  R.fail("查無該帳號，請重新輸入");
		}
		// 5.確認密碼一致,若不一致返回失敗
		if (!userResult.getUserPassword().equals(password)) {
			return R.fail("密碼錯誤，請重新輸入");
		}
		// 6.判斷帳號是否啟用
		if (0 == userResult.getUserStatus()) {
			return R.fail("帳號未啟用，請洽詢客服人員");
		}
		// 不返回password屬性
		userResult.setUserPassword(null);
		return R.success(userResult);
	}

	@Override
	public R login2(HttpServletRequest request,UserLoginParam userLoginParam) {
		// 1.密碼處理
		String password = userLoginParam.getUserPassword();
		// 2.資料庫查詢
		QueryWrapper<User> query = new QueryWrapper<>();
		query.eq("user_account", userLoginParam.getUserAccount());
		// queryWrapper.eq("user_password", userLoginParam.getUserPassword());
		User userResult = userMapper.selectOne(query);
		// 3.結果處理
		// 4.沒有該帳號返回登入失敗結果
		if (userResult == null) {
			return R.fail("查無該帳號，請重新輸入");
		}
		// 5.確認密碼一致,若不一致返回失敗
		if (!userResult.getUserPassword().equals(password)) {
			return R.fail("密碼錯誤，請重新輸入");
		}
		// 6.判斷帳號是否啟用
		if (0 == userResult.getUserStatus()) {
			return R.fail("帳號未啟用，請洽詢客服人員");
		}
		// 不返回password屬性
		userResult.setUserPassword(null);
		// 7.登入成功 將使用者id存入session
		request.getSession().setAttribute("loginUser", userResult.getUserId());
		Integer attribute = (Integer) request.getSession().getAttribute("loginUser");
		System.out.println(attribute);
		return R.success(userResult);
	}

}
