package com.kitty;

public class Item {
	String market_date, txt, username, inventid, itemid;
	String price;
	String userid;
	String store_price;
	String qi;
	String colorid;
	String setid;
	String flame_id;
	String name;
	String image;
	String color_name;
	String set_name;
	String flame_name;
	String out_of_stock;
	
	public Item(String name, String price, String username){
		this.price=price;
		this.name=name;
		this.username=username; 
	}
	
	public Item(String inventid, String itemid, String price, String userid, String market_date, String txt, String username, String name, String image, String store_price, String out_of_stock, String qi, String colorid, String color_name, String set_name, String setid, String flame_name, String flame_id ){
		this.inventid=inventid;
		this.itemid=itemid;
		this.price=price;
		this.userid=userid;
		this.market_date=market_date;
		this.txt=txt;
		this.username=username;
		this.name=name;
		this.image=image;
		this.store_price=store_price;
		this.out_of_stock=out_of_stock;
		this.qi=qi;
		this.colorid=colorid;
		this.color_name=color_name;
		this.set_name=set_name;
		this.setid=setid;
		this.flame_name=flame_name;
		this.flame_id=flame_id;
	}
	
	
	
	public String getItemName(){
		return name;
	}
	
	public String getItemUsername(){
		return username;
	}
	
	public String getItemPrice(){
		return price;
	}
}
