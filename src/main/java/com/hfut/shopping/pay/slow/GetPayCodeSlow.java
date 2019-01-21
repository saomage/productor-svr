package com.hfut.shopping.pay.slow;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.hfut.shopping.domain.Shop;
import com.hfut.shopping.mapper.ShopMapper;

public class GetPayCodeSlow implements ApplicationRunner {

	@Autowired
	SlowImpl slow;
	
	@Autowired
	ShopMapper smapper;

	public static final String NAMESERVER = "192.168.216.128:9876;192.168.216.130:9876";

	private static final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("emailConsumer");
	static {
		consumer.setNamesrvAddr(NAMESERVER);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		try {
			consumer.subscribe("PayCode", "code");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		consumer.setMessageModel(MessageModel.BROADCASTING);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				MessageExt messageExt = msgs.get(0);
				try {
					String id = messageExt.getKeys();
					String code = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
					byte[] code2 = slow.getByte(code);
					Shop shop = new Shop();
					shop.setId(Integer.valueOf(id));
					shop.setCountPay(code);
					shop.setDiscountStrategy(code2);
					smapper.update(shop);
				} catch (Exception e) {
					e.printStackTrace();
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}

		});
		consumer.start();
	}
}
