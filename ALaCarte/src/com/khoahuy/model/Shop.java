package com.khoahuy.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Shop {
	
	private String title;
	private String address;
	private String phone;
	private String imagePath;
	private List<Item> menu;
	
	public static Shop parseFromJson(String jsonString){
		Shop shop = new Shop();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			shop.setTitle(jsonObj.getString("title"));
			shop.setAddress(jsonObj.getString("address"));
			shop.setPhone(jsonObj.optString("phone","083125235"));
			shop.setImagePath(jsonObj.getString("image"));
			JSONArray menuArray = jsonObj.getJSONArray("menu");
			shop.setMenu(new ArrayList<Item>());
			for (int i = 0; i < menuArray.length(); i++)
			{
				JSONObject itemObject = menuArray.getJSONObject(i);
				Item item = new Item();
				item.setName(itemObject.getString("name"));
				item.setDescription(itemObject.optString("description", ""));
				item.setPrice(itemObject.getString("price"));
				item.setImagePath(itemObject.getString("image"));
				shop.getMenu().add(item);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return shop;		
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<Item> getMenu() {
		return menu;
	}
	public void setMenu(List<Item> menu) {
		this.menu = menu;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}	
	
}
