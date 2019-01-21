package com.hfut.shopping.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hfut.shopping.domain.Productor;
import com.hfut.shopping.massage.ResultMsg;

@FeignClient(name="user-svr")
public interface UserFeign {

	@PostMapping("productor/login")
	public Productor proLogin(@RequestBody Productor productor);
	
	@PostMapping("productor/register")
	public ResultMsg proRegister(@RequestBody Productor productor);
}
