package com.hdlight.wallpaperapps.model;

public class Save {

    String image;

    public Save(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Save{" +
                "image='" + image + '\'' +
                '}';
    }
}
