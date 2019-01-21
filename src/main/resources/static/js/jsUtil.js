window.onload = function() {
	document.getElementById("shop_button").onclick = function() {
		var shop={};
		shop.id=document.getElementById("shop_id").value;
		shop.name=document.getElementById("shop_name").value;
		shop.address=document.getElementById("shop_address").value;
		shop.phone=document.getElementById("shop_phone").value;
		shop.introduce=document.getElementById("shop_phone").value;
		shop.avatar=document.getElementById("shop_avatar").value;
	    $.ajax({
	        url: "/productor/shop/update",
	        data: JSON.stringify(shop),
	        type: "POST",
	        contentType: "application/json;charset=utf-8",
	        success: function(response){
	        if(response.success){
	            alert(response.message);
	        }
	    }
	    });
	}
	document.getElementById("code_button").onclick = function() {
		var shop={};
		shop.id=document.getElementById("shop_id").value;
		shop.countPay =document.getElementById("shop_countPay").value;
		 $.ajax({
		        url: "/productor/pay/code/quick",
		        data:  JSON.stringify(shop),
		        type: "POST",
		        contentType: "application/json;charset=utf-8",
		        success: function(response){
		        if(response.success){
		            alert(response.message);
		        }
		    }
		    });
	}
}

