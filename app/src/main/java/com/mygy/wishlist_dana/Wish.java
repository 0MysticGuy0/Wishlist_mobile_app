package com.mygy.wishlist_dana;

import android.net.Uri;

import java.io.Serializable;

public class Wish implements Serializable {
    private int weight = 1;
    private String name;
    private String link;
    private String description;
    private double price;
    private transient Uri icoUri ;
    private boolean pinned=false;
    private boolean done=false;
    private String stringUri;

    public Wish(String name, String link, double price,String description) {
        this.name = name;
        this.link = link;
        this.price = price;
        this.description = description;
        try {
            this.icoUri = Uri.parse(stringUri);
        }catch (NullPointerException ex){
            icoUri = null;
        }
    }

    public void setIcoUri(Uri uri) {
        this.icoUri = uri;
        if(uri != null)
            stringUri = uri.toString();
    }

    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
        weight =(pinned)?0:1;
    }

    public String getLink() {
        return link;
    }

    public double getPrice() {
        return price;
    }

    public Uri getIcoUri() {
        if(icoUri == null){
            try {
                this.icoUri = Uri.parse(stringUri);
            }catch (NullPointerException ex){
                icoUri = null;
            }
        }
        return icoUri;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPinned() {
        return pinned;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        weight = (done)?2:((pinned)?0:1);
    }
}
