package com.app.lsquared.model.vimeo;

import com.google.gson.annotations.SerializedName;

public class Rating{

	@SerializedName("id")
	private int id;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}