package com.khoahuy.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item {

	private String id;
	private String name;
	private String price;
	private String description;
	private String imagePath;
	private List<String> otherImage;

	public static Item parseFromJson(String jsonString) {
		Item item = new Item();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			item.setId(jsonObj.optString("id", "null"));
			item.setName(jsonObj.getString("name"));
			item.setDescription(jsonObj.optString("description", ""));
			item.setPrice(jsonObj.getString("price"));
			item.setImagePath(jsonObj.getString("main_image"));
			JSONArray otherImageArray = jsonObj.getJSONArray("other_image");
			item.setOtherImage(new ArrayList<String>());
			for (int i = 0; i < otherImageArray.length(); i++) {
				item.getOtherImage().add(otherImageArray.get(i).toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	public static Item[] getHotItem() {
		Item[] array = new Item[3];
		array[0] = new Item("1", "1 set Barbecue Garden", "230.000",
				"22/6 Nam Ki Khoi Nghia Q.3",
				"http://media.foody.vn/restaurant/634642104189375000_bbq_220.jpg");
		array[1] = new Item(
				"2",
				"Trai cay to",
				"20.000",
				"22-24 Nguyen Van Thu, Q.1",
				"http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105754855.jpg");
		array[2] = new Item(
				"3",
				"Juice up",
				"45.000",
				"128 Trinh Dinh Trong, Q.Tan Phu",
				"http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105755721.jpg");
		return array;
	}

	public static Item[] getRecommendItem() {
		Item[] array = new Item[3];
		array[0] = new Item(
				"1",
				"Lau nuong",
				"200.000",
				"22/6 Nam Ki Khoi Nghia Q.3",
				"http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_2013112510575273.jpg");
		array[1] = new Item(
				"2",
				"Trai cay to",
				"20.000",
				"22-24 Nguyen Van Thu, Q.1",
				"http://media.foody.vn/album/hinh-anh-barbecue-garden-792015-634962138475223750.jpg");
		array[2] = new Item(
				"3",
				"Juice up",
				"45.000",
				"128 Trinh Dinh Trong, Q.Tan Phu",
				"http://media.foody.vn/restaurant/foody-ahoy-beer-club-coming-soon--tp-hcm-131016092619_220.jpg");
		return array;
	}

	public static Item[] getFriendItem() {

		Item[] array = new Item[3];
		array[0] = new Item(
				"1",
				"Kimchi KimChi",
				"120.000",
				"22/6 Nam Ki Khoi Nghia Q.3",
				"http://media.foody.vn/album/resize/foody-kimchi-kimchi-satra-pham-hung-1668718-635216053698532500_cropped_154154.jpg");
		array[1] = new Item(
				"2",
				"Trai cay to",
				"20.000",
				"22-24 Nguyen Van Thu, Q.1",
				"http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105754855.jpg");
		array[2] = new Item(
				"3",
				"Juice up",
				"45.000",
				"128 Trinh Dinh Trong, Q.Tan Phu",
				"http://thegioinfc.com:8080/images/food/buffet-toi-tai-golden-central-hotel/buffet_toi_tai_golden_central_hotel_20131125105755721.jpg");
		return array;
	}

	public static Item[] getCouponItem() {

		Item[] array = new Item[3];
		array[0] = new Item(
				"1",
				"Kimchi KimChi",
				"120.000",
				"22/6 Nam Ki Khoi Nghia Q.3",
				"http://media.foody.vn/album/hinh-anh-barbecue-garden-792015-634962138825223750.jpg");
		array[1] = new Item("2", "Trai cay to", "20.000",
				"22-24 Nguyen Van Thu, Q.1",
				"http://media.foody.vn/coupon/tpc-pt_300.jpg");
		array[2] = new Item(
				"3",
				"Juice up",
				"45.000",
				"128 Trinh Dinh Trong, Q.Tan Phu",
				"http://media.foody.vn/restaurant/mobile/foody-mobile-casa-del-caffe-office-lunch-coffee-bakery-tp-hcm-131209093852_300.jpg");
		return array;
	}

	public Item() {
		super();
	}

	public Item(String id, String name, String price, String description,
			String imagePath) {
		super();
		this.id = id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getOtherImage() {
		return otherImage;
	}

	public void setOtherImage(List<String> otherImage) {
		this.otherImage = otherImage;
	}

}
