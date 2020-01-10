package com.mahmoud.hadith.model.entity.api.hadithwithout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


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