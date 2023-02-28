package com.app.lsquared.model.vimeo;

import com.google.gson.annotations.SerializedName;

public class Request{


	@SerializedName("files")
	private Files files;



	public void setFiles(Files files){
		this.files = files;
	}

	public Files getFiles(){
		return files;
	}


}