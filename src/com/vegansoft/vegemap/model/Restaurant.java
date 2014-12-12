package com.vegansoft.vegemap.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

	int id;
	String name;
	String province;
	String city;
	String detail_addr;
	String tel;
	String business_hours;
	String holiday;
	String type;
	String grade;
	String homepage;
	double latitude;
	double longitude;
	String menu;
	String zip;
	String parking;
	String tel2;
	double distance;
	String rough_map_desc;
	String price;
	
    public Restaurant() {
    }
    
	public Restaurant(Parcel src) {
		id = src.readInt();
		name = src.readString();
		province = src.readString();
		city = src.readString();
		detail_addr = src.readString();
		tel = src.readString();
		business_hours = src.readString();
		holiday = src.readString();
		type = src.readString();
		grade = src.readString();
		homepage = src.readString();
		latitude = src.readDouble();
		longitude = src.readDouble();
		menu = src.readString();
		zip = src.readString();
		parking = src.readString();
		tel2 = src.readString();
		distance = src.readDouble();
		rough_map_desc = src.readString();
		price = src.readString();
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(name);
		arg0.writeString(province);
		arg0.writeString(city);
		arg0.writeString(detail_addr);
		arg0.writeString(tel);
		arg0.writeString(business_hours);
		arg0.writeString(holiday);
		arg0.writeString(type);
		arg0.writeString(grade);
		arg0.writeString(homepage);
		arg0.writeDouble(latitude);
		arg0.writeDouble(longitude);
		arg0.writeString(menu);
		arg0.writeString(zip);
		arg0.writeString(parking);
		arg0.writeString(tel2);
		arg0.writeDouble(distance);
		arg0.writeString(rough_map_desc);
		arg0.writeString(price);
	}


	@Override
	public int describeContents() {
		return 0;
	}


	 // Parcelable을 생성하는 코드. 반드시 추가해주어야 한다.
	public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {

	    public Restaurant createFromParcel(Parcel in) {

	      return new Restaurant(in);

	    }

	    public Restaurant[] newArray (int size) {

	      return new Restaurant[size];

	    }

	  };

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDetail_addr() {
		return detail_addr;
	}

	public void setDetail_addr(String detail_addr) {
		this.detail_addr = detail_addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBusiness_hours() {
		return business_hours;
	}

	public void setBusiness_hours(String business_hours) {
		this.business_hours = business_hours;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getRough_map_desc() {
		return rough_map_desc;
	}

	public void setRough_map_desc(String rough_map_desc) {
		this.rough_map_desc = rough_map_desc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}


}
