package com.mahmoud.hadith.model.entity.api.hadithwithout;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "hadith")
public class HadithItem {

	@SerializedName("Hadith_ID")
	@PrimaryKey
	@ColumnInfo(name = "hadith_id")
	private int hadithID;

	@SerializedName("Ar_Sanad_Without_Tashkeel")
	@ColumnInfo(name = "hadith_sanad")
	private String arSanadWithoutTashkeel;

	@SerializedName("Ar_Text_Without_Tashkeel")
	@ColumnInfo(name = "hadith_text")
	private String arTextWithoutTashkeel;

	@ColumnInfo(name = "book_id")
	private int bookId;

	@ColumnInfo(name = "chapter_id")
	private int chapterID;


	@SerializedName("Ar_Sanad_1")
	@Ignore
	private String arSanad1;

	@SerializedName("Ar_Text")
	@Ignore
	private String arText;

	@SerializedName("En_Text")
	@Ignore
	private String enText;

	@SerializedName("En_Sanad")
	@Ignore
	private String enSanad;

	@SerializedName("Book_ID")
	@Ignore
	private int bookIdNew;

	@SerializedName("Chapter_ID")
	@Ignore
	private int chapterIDNew;


    public void setHadithID(int hadithID){
		this.hadithID = hadithID;
	}

	public int getHadithID(){
		return hadithID;
	}

    public String getHadithText() {
        if (arTextWithoutTashkeel != null) {
            return arTextWithoutTashkeel;
        } else if (arText != null) {
            return arText;
        } else {
            return enText;
        }

    }

    public String getSanadText() {
        if (arSanadWithoutTashkeel != null) {
            return arSanadWithoutTashkeel;
        } else if (arSanad1 != null) {
            return arSanad1;
        } else {
            return enSanad;
        }

    }

	public void setArSanadWithoutTashkeel(String arSanadWithoutTashkeel){
		this.arSanadWithoutTashkeel = arSanadWithoutTashkeel;
	}

	public String getArSanadWithoutTashkeel(){
		return arSanadWithoutTashkeel;
	}

	public void setArTextWithoutTashkeel(String arTextWithoutTashkeel){
		this.arTextWithoutTashkeel = arTextWithoutTashkeel;
	}

	public String getArTextWithoutTashkeel(){
		return arTextWithoutTashkeel;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getChapterID() {
		return chapterID;
	}

	public void setChapterID(int chapterID) {
		this.chapterID = chapterID;
	}

	public String getArSanad1() {
		return arSanad1;
	}

	public void setArSanad1(String arSanad1) {
		this.arSanad1 = arSanad1;
	}

	public String getArText() {
		return arText;
	}

	public void setArText(String arText) {
		this.arText = arText;
	}

	public String getEnText() {
		return enText;
	}

	public void setEnText(String enText) {
		this.enText = enText;
	}

	public String getEnSanad() {
		return enSanad;
	}

	public void setEnSanad(String enSanad) {
		this.enSanad = enSanad;
	}

	public int getBookIdNew() {
		return bookIdNew;
	}

	public void setBookIdNew(int bookIdNew) {
		this.bookIdNew = bookIdNew;
	}

	public int getChapterIDNew() {
		return chapterIDNew;
	}

	public void setChapterIDNew(int chapterIDNew) {
		this.chapterIDNew = chapterIDNew;
	}


    @Override
 	public String toString(){
		return 
			"HadithItem{" +
			"hadith_ID = '" + hadithID + '\'' +
					"hadith_ID = '" + hadithID + '\'' +
					"hadith_ID = '" + hadithID + '\'' +
					",ar_Sanad_Without_Tashkeel = '" + arSanadWithoutTashkeel + '\'' +
			",ar_Text_Without_Tashkeel = '" + arTextWithoutTashkeel + '\'' + 
			"}";
		}
}