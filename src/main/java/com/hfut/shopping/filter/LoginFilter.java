package com.hfut.shopping.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.hfut.shopping.domain.Productor;
import com.hfut.shopping.util.JsonUtils;
import com.hfut.shopping.util.SessionUtil;

@WebFilter(urlPatterns="/productor/*" ,filterName="LoginFilter")
public class LoginFilter implements Filter{
	
	@Autowired
	StringRedisTemplate redis;
	
	@Autowired
	SessionUtil session;
	
	final AtomicBoolean flag=new AtomicBoolean(true);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpServletResponse httpResponse=(HttpServletResponse)response;
		session.changeSession(httpRequest, httpResponse);
		Optional<Cookie[]> oc = Optional.ofNullable(httpRequest.getCookies());
		Optional<Stream<Cookie>> cookies = oc.map(u->Stream.of(u));
		cookies.ifPresent(u->u.forEach(cookie->{
			if("productor".equals(cookie.getName())) {
				Productor productor = JsonUtils.string2Obj(redis.opsForValue().get(cookie.getValue()), Productor.class);
				request.setAttribute("productor", productor);
				try {
					chain.doFilter(request, response);
				}catch (IOException | ServletException e) {
					e.printStackTrace();
				}
				flag.set(false);
			}
		}));
//		httpRequest.getRequestDispatcher("/consumer/login1").forward(request, response);
		if(flag.get()) {
			httpResponse.sendRedirect("/login");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
