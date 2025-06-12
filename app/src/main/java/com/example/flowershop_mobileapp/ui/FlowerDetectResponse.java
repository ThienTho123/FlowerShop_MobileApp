package com.example.flowershop_mobileapp.ui;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FlowerDetectResponse {
    @SerializedName("image")
    private String image;

    @SerializedName("objects")
    private List<DetectedObject> objects;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<DetectedObject> getObjects() {
        return objects;
    }

    public void setObjects(List<DetectedObject> objects) {
        this.objects = objects;
    }

    public static class DetectedObject {
        @SerializedName("detect")
        private FlowerDetectInfo detect;

        @SerializedName("numberFound")
        private int numberFound;

        @SerializedName("flowerDTOList")
        private List<FlowerProduct> flowerDTOList;

        public FlowerDetectInfo getDetect() {
            return detect;
        }

        public void setDetect(FlowerDetectInfo detect) {
            this.detect = detect;
        }

        public int getNumberFound() {
            return numberFound;
        }

        public void setNumberFound(int numberFound) {
            this.numberFound = numberFound;
        }

        public List<FlowerProduct> getFlowerDTOList() {
            return flowerDTOList;
        }

        public void setFlowerDTOList(List<FlowerProduct> flowerDTOList) {
            this.flowerDTOList = flowerDTOList;
        }
    }

    public static class FlowerDetectInfo {
        @SerializedName("vietnamname")
        private String vietnamname;

        @SerializedName("imageurl")
        private String imageurl;

        @SerializedName("flowerdetect")
        private String flowerdetect;

        @SerializedName("origin")
        private String origin;

        @SerializedName("timebloom")
        private String timebloom;

        @SerializedName("characteristic")
        private String characteristic;

        @SerializedName("flowerlanguage")
        private String flowerlanguage;

        @SerializedName("bonus")
        private String bonus;

        @SerializedName("uses")
        private String uses;

        // Getters and Setters
        public String getVietnamname() {
            return vietnamname;
        }

        public void setVietnamname(String vietnamname) {
            this.vietnamname = vietnamname;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getFlowerdetect() {
            return flowerdetect;
        }

        public void setFlowerdetect(String flowerdetect) {
            this.flowerdetect = flowerdetect;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getTimebloom() {
            return timebloom;
        }

        public void setTimebloom(String timebloom) {
            this.timebloom = timebloom;
        }

        public String getCharacteristic() {
            return characteristic;
        }

        public void setCharacteristic(String characteristic) {
            this.characteristic = characteristic;
        }

        public String getFlowerlanguage() {
            return flowerlanguage;
        }

        public void setFlowerlanguage(String flowerlanguage) {
            this.flowerlanguage = flowerlanguage;
        }

        public String getBonus() {
            return bonus;
        }

        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

        public String getUses() {
            return uses;
        }

        public void setUses(String uses) {
            this.uses = uses;
        }
    }

    public static class FlowerProduct {
        @SerializedName("flowerID")
        private int flowerID;

        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        @SerializedName("price")
        private double price;

        @SerializedName("priceEvent")
        private Double priceEvent;

        @SerializedName("category")
        private Category category;

        @SerializedName("purpose")
        private Purpose purpose;

        // Getters and Setters
        public int getFlowerID() {
            return flowerID;
        }

        public void setFlowerID(int flowerID) {
            this.flowerID = flowerID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Double getPriceEvent() {
            return priceEvent;
        }

        public void setPriceEvent(Double priceEvent) {
            this.priceEvent = priceEvent;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public Purpose getPurpose() {
            return purpose;
        }

        public void setPurpose(Purpose purpose) {
            this.purpose = purpose;
        }

        public static class Category {
            @SerializedName("categoryName")
            private String categoryName;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }
        }

        public static class Purpose {
            @SerializedName("purposeName")
            private String purposeName;

            public String getPurposeName() {
                return purposeName;
            }

            public void setPurposeName(String purposeName) {
                this.purposeName = purposeName;
            }
        }
    }
}