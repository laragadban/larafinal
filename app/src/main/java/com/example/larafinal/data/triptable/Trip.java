package com.example.larafinal.data.triptable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trip
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    private String name;
    private String type;
    private String country;

    // These caused the error because they were missing getters/setters
    private String town;
    private String address;
    private long lat,lang;

    private String image;
    private String description;
    private String rating;
    private String reviews;

    // ... existing getters and setters ...

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

    // --- ADD THESE METHODS ---

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

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLang() {
        return lang;
    }

    public void setLang(long lang) {
        this.lang = lang;
    }

    // -------------------------

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
}
