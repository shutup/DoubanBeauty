package com.shutup.doubanbeauty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shutup on 16/8/9.
 */
public class BeautyModel extends RealmObject{

    @PrimaryKey
    private String imageUrl;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public BeautyModel(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }
}
