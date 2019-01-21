package com.hfut.shopping.util;

import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.Data;

@SuppressWarnings("deprecation")
public class MySession extends StandardSessionFacade implements HttpSession{
	
	private final HttpSession session;
	
	private StringRedisTemplate redis;
	
	private String uuID;
	
	public MySession(HttpSession session,HttpServletRequest req,HttpServletResponse resp) {
		super(session);
		this.session = session;
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("mySession")) {
					uuID=cookie.getValue();
				}
			}
		}
		if(uuID==null) {
			uuID=UUID.randomUUID().toString();
			CookieUtil.set(resp, "mySession", uuID, 7200);
		}
	}
	
	public void setRedis(StringRedisTemplate redis) {
		this.redis=redis;
	}

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getAttribute(String name) {
		String json = redis.opsForValue().get(uuID+name);
		if(json==null||json=="") {
			return session.getAttribute(name);
		}
		ClassUtil util = JsonUtils.string2Obj(json, ClassUtil.class);
		return JsonUtils.string2Obj(util.json, util.type);
	}

	@Override
	public Object getValue(String name) {
		return session.getValue(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

	@Override
	public String[] getValueNames() {
		return session.getValueNames();
	}

	@Override
	public void setAttribute(String name, Object value) {
		ClassUtil classUtil = new ClassUtil();
		classUtil.type=value.getClass();
		classUtil.json=JsonUtils.obj2String(value);
		redis.opsForValue().set(uuID+name, JsonUtils.obj2String(classUtil), 24, TimeUnit.HOURS);
	}

	@Override
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	@Override
	public void removeValue(String name) {
		session.removeValue(name);
	}

	@Override
	public void invalidate() {
		session.invalidate();
	}

	@Override
	public boolean isNew() {
		return session.isNew();
	}
	
}

@Data
class  ClassUtil{
	String json;
	Class<?> type;
}

