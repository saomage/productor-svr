package com.hfut.shopping.mapper;

import org.springframework.stereotype.Component;

import com.hfut.shopping.domain.Shop;

@Component
public interface ShopMapper {

	int update(Shop shop);
}
