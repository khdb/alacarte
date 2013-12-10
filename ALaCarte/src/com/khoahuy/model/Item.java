package com.khoahuy.model;

public class Item {

	private String name;
	private String price;
	private String description;
	private String imagePath;
	
	public static Item[] getHotItem(){
		Item[] array = new Item[3];
		array[0] = new Item("Lau nuong", "200.000", "22/6 Nam Ki Khoi Nghia Q.3", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_2013112510575273.jpg");
		array[1] = new Item("Trai cay to", "20.000", "22-24 Nguyen Van Thu, Q.1", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105754855.jpg");
		array[2] = new Item("Juice up", "45.000", "128 Trinh Dinh Trong, Q.Tan Phu", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105755721.jpg");
		return array;
	}
	
	public static Item[] getRecommendItem(){
		Item[] array = new Item[3];
		array[0] = new Item("Lau nuong", "200.000", "22/6 Nam Ki Khoi Nghia Q.3", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_2013112510575273.jpg");
		array[1] = new Item("Trai cay to", "20.000", "22-24 Nguyen Van Thu, Q.1", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105754855.jpg");
		array[2] = new Item("Juice up", "45.000", "128 Trinh Dinh Trong, Q.Tan Phu", "http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105755721.jpg");
		return array;
	}
	
	public Item() {
		super();
	}
		
	public Item(String name, String price, String description, String imagePath) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	
}
