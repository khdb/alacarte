package com.khoahuy.model;

public class Spotlight {

	public static enum TYPE {
		HOT, RECOMMEND, FRIEND, COUPON
	}

	private String title;
	private TYPE type;
	private Item[] array;
	
	public static Spotlight getHotSpotlight(){
		return new Spotlight("Hot", TYPE.HOT, Item.getHotItem());
	}
	
	public static Spotlight getRecommendSpotlight(){
		return new Spotlight("Recommend", TYPE.RECOMMEND, Item.getRecommendItem());
	}
	
	public static Spotlight getFriendSpotlight(){
		return new Spotlight("Where your friend go?", TYPE.FRIEND, Item.getFriendItem());
	}
	
	public static Spotlight getCouponSpotlight(){
		return new Spotlight("Promotion today", TYPE.COUPON, Item.getCouponItem());
	}
	
	public Spotlight(String title, TYPE type, Item[] array) {
		super();
		this.title = title;
		this.type = type;
		this.array = array;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TYPE getType() {
		return type;
	}
	public void setType(TYPE type) {
		this.type = type;
	}
	public Item[] getArray() {
		return array;
	}
	public void setArray(Item[] array) {
		this.array = array;
	}

}
