<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>freemarker</title>
<script type="text/javascript" src="/js/jsUtil.js"></script>
<script type="text/javascript" src="/js/jquery-2.1.3.min.js"></script>
</head>
<body>
<#if productor??>
<#if productor.shops??>
<#list productor.shops as shop>
		<input type="hidden" id="shop_id"  name="id" value="${shop.id}">
		店铺名称<input type="text" id="shop_name" name="name" value="${shop.name}"/>
		店铺地址<input type="text" id="shop_address" name="address" value="${shop.address}"/>
		店铺电话<input type="text" id="shop_phone" name="phone" value="${shop.phone}"/>
		店铺详情<input type="text" id="shop_introduce" name="introduce" value="${shop.introduce}"/>
		店铺图片<input type="text" id="shop_avatar" name="avatar" value="${shop.avatar}"/>
		<input type="button" id="shop_button" value="提交"/><br/>
		打折策略<input type="text" id="shop_countPay" name="countPay"  value="${shop.countPay!'return a+b+c;'}"/>
		<input type="button" id="code_button" value="打折提交"/><br/>
		<#if shop.commoditys??>
		<#list shop.commoditys as commodity>
		<input type="hidden" name="commodity_id" value="${commodity.id}">
		商品名称<input type="text" name="commodity_name" value="${commodity.name}"/>
		商品详情<input type="text" name="commodity_introduce" value="${commodity.introduce}"/>
		商品类型<input type="text" name="commodity_type" value="${commodity.type}"/>
		商品价格<input type="text" name="commodity_price" value="${commodity.price}"/>
		商品图片<input type="text" name="commodity_avatar" value="${commodity.avatar}"/>
		<input type="button" name="commodity_button" value="提交"/><br/>
		</#list>
		</#if>
</#list>
</#if>
</#if>
</body>
</html>