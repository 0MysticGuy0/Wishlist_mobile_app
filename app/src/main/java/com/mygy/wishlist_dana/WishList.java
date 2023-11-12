package com.mygy.wishlist_dana;

import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class WishList implements Serializable {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
    private String name;
    private Date date;
    private transient Uri icoURI = null;
    private String stringUri;
    private ArrayList<Wish> list = new ArrayList<>();
    private ArrayList<Wish> pinnedList = new ArrayList<>();

    private ArrayList<Wish> doneList = new ArrayList<>();

    public WishList(String name, Date date) {
        this.name = name;
        this.date = date;
        try {
            this.icoURI = Uri.parse(stringUri);
        }catch (NullPointerException ex){
            icoURI = null;
        }
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }
    public String getFormattedDate(){
        return dateFormat.format(date);
    }

    public ArrayList<Wish> getList() {
        sort();
        return list;
    }
    public void sort(){
        list.sort(Comparator.comparingInt(Wish::getWeight));
    }

    public Uri getIcoUri() {
        if(icoURI == null){
            try {
                this.icoURI = Uri.parse(stringUri);
            }catch (NullPointerException ex){
                icoURI = null;
            }
        }
        return icoURI;
    }

    public void setIcoURI(Uri icoURI) {
        this.icoURI = icoURI;
    }

    public ArrayList<Wish> getPinnedList() {
        return pinnedList;
    }

    public ArrayList<Wish> getDoneList() {
        return doneList;
    }

}
