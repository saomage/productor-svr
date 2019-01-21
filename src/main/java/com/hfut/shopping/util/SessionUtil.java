package com.hfut.shopping.util;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.session.StandardSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {
	
	@Autowired
	StringRedisTemplate redis;
	
	public void changeSession(HttpServletRequest req,HttpServletResponse resp) {
		HttpSession session = req.getSession();
		MySession mySession = new MySession(session,req,resp);
		mySession.setRedis(redis);
		try {
			Field f = RequestFacade.class.getDeclaredField("request");
			f.setAccessible(true);
			Request aaa =(Request) f.get(req);
			Field ff = Request.class.getDeclaredField("session");
			ff.setAccessible(true);
			Session sss=(Session)ff.get(aaa);
			Field fff = StandardSession.class.getDeclaredField("facade");
			fff.setAccessible(true);
			fff.set(sss, mySession);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
