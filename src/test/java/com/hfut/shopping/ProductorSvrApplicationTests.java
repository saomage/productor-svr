package com.hfut.shopping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hfut.shopping.domain.Productor;
import com.hfut.shopping.domain.Shop;
import com.hfut.shopping.mapper.ProductorMapper;
import com.hfut.shopping.mapper.ShopMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductorSvrApplicationTests {
	
	@Autowired
	ProductorMapper pmapper;
	
	@Autowired
	ShopMapper smapper;

	@Test
	public void contextLoads() {
		Productor productor = new Productor();
		productor.setId(53L);
		Shop shop = pmapper.selectProductor(productor).getShops().get(0);
		shop.setCountPay("return a+b;");
		smapper.update(shop);
	}

}

