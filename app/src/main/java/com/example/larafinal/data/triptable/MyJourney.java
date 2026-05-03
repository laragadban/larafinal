package com.example.larafinal.data.triptable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MyJourney implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    private String name;
    private String type;
    private String country;

    private String town;
    private String address;
    private double lat, lang;
    private String image;
    private String description;
    private String rating;
    private String reviews;
    private String key;

    // --- 1. الـ Constructor الفارغ (ضروري جداً لـ Firebase) ---
    public MyJourney() {
    }

    // --- 2. جميع الـ Getters والـ Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // --- دوال إضافية للتوافق مع الكود القديم ---

    public String getTripName() {
        return name;
    }

    public void setTripName(String tripName) {
        this.name = tripName;
    }

    public String getTripDescription() {
        return description;
    }
}