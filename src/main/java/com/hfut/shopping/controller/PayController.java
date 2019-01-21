package com.hfut.shopping.controller;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hfut.shopping.domain.Shop;
import com.hfut.shopping.mapper.ShopMapper;
import com.hfut.shopping.massage.ResultMsg;
import com.hfut.shopping.pay.quick.GetPayCodeQuick;

@RestController
public class PayController {
	
	@Autowired
	ShopMapper smapper;
	
	public static final String NAMESERVER = "192.168.216.128:9876;192.168.216.130:9876";

	private final DefaultMQProducer MQproducer = new DefaultMQProducer("PayCode");

	{
		MQproducer.setNamesrvAddr(NAMESERVER);
		try {
			MQproducer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("productor/pay/code/quick")
	public ResultMsg getPayCode(@RequestBody Shop shop) {
		int id=shop.getId();
		String code = shop.getCountPay();
		try {
			shop = new Shop();
			byte[] code2 = GetPayCodeQuick.getCode(code);
			shop.setId(id);
			shop.setCountPay(code);
			shop.setDiscountStrategy(code2);
			smapper.update(shop);
		} catch (Exception e) {
			ResultMsg.errorMsg.setMsg(e.getMessage());
		}
		return ResultMsg.successMsg;
	}

	@PostMapping("productor/pay/code/slow")
	public ResultMsg getPayCodeSlow(@RequestBody Shop shop){
		int id=shop.getId();
		String code = shop.getCountPay();
		try {
			Message message= new Message("PayCode", "code", String.valueOf(id), code.getBytes());
			MQproducer.send(message);
		} catch (Exception e) {
			return ResultMsg.errorMsg.setMsg(e.getMessage());
		}
		return ResultMsg.successMsg;
	}
}
