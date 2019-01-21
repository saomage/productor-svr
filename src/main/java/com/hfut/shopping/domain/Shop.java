package com.hfut.shopping.domain;

import org.springframework.stereotype.Component;

import com.hfut.shopping.pay.Pay;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
@Data
@EqualsAndHashCode(callSuper=false)
public class Shop extends SimpleShop{

	private byte[] discountStrategy;
	 
	private String countPay;
	
	private ShopClassLoader classLoader;
	
	private Pay pay;

}
