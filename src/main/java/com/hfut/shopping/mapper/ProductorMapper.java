package com.hfut.shopping.mapper;

import org.springframework.stereotype.Component;

import com.hfut.shopping.domain.Commodity;
import com.hfut.shopping.domain.Productor;
import com.hfut.shopping.domain.Shop;

@Component
public interface ProductorMapper {
	
	Productor selectProductor(Productor productor);
	
	Shop selectShop(Long id);
	
	Commodity selectCommodity(Integer id);

}
