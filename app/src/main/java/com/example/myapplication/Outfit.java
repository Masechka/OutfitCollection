package com.example.myapplication;

public class Outfit {
    public String id, image_uri, tags;

    public Outfit() {
    }

    public Outfit(String id, String image_uri, String tags) {
        this.id = id;
        this.image_uri = image_uri;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getTags() {
        return tags;
    }
}
