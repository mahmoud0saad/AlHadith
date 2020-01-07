package com.mahmoud.hadith.model.entity.api.hadithwithout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HadithResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("Chapter")
	private List<HadithItem> hadithWithout;



	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public List<HadithItem> getHadithWithout() {
		return hadithWithout;
	}

	public void setHadithWithout(List<HadithItem> hadithWithout) {
		this.hadithWithout = hadithWithout;
	}

	@Override
 	public String toString(){
		return 
			"HadithResponse{" + 
			"code = '" + code + '\'' + 
			"}";
		}
}