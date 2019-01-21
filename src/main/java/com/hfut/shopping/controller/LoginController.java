package com.hfut.shopping.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hfut.shopping.domain.Productor;
import com.hfut.shopping.feign.UserFeign;
import com.hfut.shopping.mapper.ProductorMapper;
import com.hfut.shopping.massage.ResultMsg;
import com.hfut.shopping.util.CookieUtil;
import com.hfut.shopping.util.JsonUtils;

@Controller
public class LoginController {

	@Autowired
	StringRedisTemplate redis;

	@Autowired
	UserFeign userFeign;
	
	@Autowired
	ProductorMapper mapper;

	@GetMapping("login")
	public String Login() {
		return "login";
	}

	@GetMapping("register")
	public String Register() {
		return "register";
	}

	@PostMapping("pro/register")
	public String registerProductor(Model model, Productor productor, HttpServletResponse resp) {
		ResultMsg msg = userFeign.proRegister(productor);
		if (ResultMsg.successMsg == msg)
			return "login";
		else {
			model.addAttribute("error", msg);
			return "projectError";
		}
	}

	@PostMapping("pro/login")
	public ModelAndView loginConsumer(Productor productor, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView();
		try {
			productor = userFeign.proLogin(productor);
			String uu = UUID.randomUUID().toString();
			redis.opsForValue().set(uu, JsonUtils.obj2String(productor), 2, TimeUnit.HOURS);
			CookieUtil.set(resp, "productor", uu, 7200);
			productor=mapper.selectProductor(productor);
			mv.addObject("productor", productor);
			mv.setViewName("detailedProductor");
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("projectError");
		}
		return mv;
	}

}
