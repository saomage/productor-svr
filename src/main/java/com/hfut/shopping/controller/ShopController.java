package com.hfut.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hfut.shopping.domain.Shop;
import com.hfut.shopping.mapper.ShopMapper;
import com.hfut.shopping.massage.ResultMsg;

@RestController
public class ShopController {
	@Autowired
	ShopMapper smapper;
	
	@PostMapping("productor/shop/update")
	public ResultMsg shopUpdata(@RequestBody Shop shop) {
		Integer i=0;
		try {
			i = smapper.update(shop);
		} catch (Exception e) {
			return ResultMsg.errorMsg.setMsg(e.getMessage());
		}
		return ResultMsg.successMsg.setMsg(String.valueOf(i));
	}

}
