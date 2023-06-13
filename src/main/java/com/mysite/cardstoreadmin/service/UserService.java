package com.mysite.cardstoreadmin.service;

import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mysite.cardstoreadmin.controller.utils.R;
import com.mysite.cardstoreadmin.param.UserCheckParam;
import com.mysite.cardstoreadmin.param.UserLoginParam;
import com.mysite.cardstoreadmin.pojo.User;

public interface UserService extends IService<User>{
	
	Boolean saveUser(User user); 
	
	Boolean updateUser(User user);
	
	R check(UserCheckParam userCheckParam);

	R register(User user);

	R login(UserLoginParam userLoginParam);
	
	R login2(HttpServletRequest request,UserLoginParam userLoginParam);

}
