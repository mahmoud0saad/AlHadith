package com.mahmoud.hadith.model.entity.api.chapter;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ChapterResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("Chapter")
	private List<ChapterItem> chapter;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setChapter(List<ChapterItem> chapter){
		this.chapter = chapter;
	}

	public List<ChapterItem> getChapter(){
		return chapter;
	}

	@Override
 	public String toString(){
		return 
			"ChapterResponse{" + 
			"code = '" + code + '\'' + 
			",chapter = '" + chapter + '\'' + 
			"}";
		}
}