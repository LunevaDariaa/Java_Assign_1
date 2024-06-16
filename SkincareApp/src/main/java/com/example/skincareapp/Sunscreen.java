package com.example.skincareapp;
        public class Sunscreen {
                private String productName;
                private String brand;
                private int spfLevel;
                private String skinType;
                private double rating;

                public Sunscreen(String productName, String brand, int spfLevel, String skinType, double rating) {
                        this.productName = productName;
                        this.brand = brand;
                        this.spfLevel = spfLevel;
                        this.skinType = skinType;
                        this.rating = rating;
                }

                // Getters
                public String getProductName() { return productName; }
                public String getBrand() { return brand; }
                public int getSpfLevel() { return spfLevel; }
                public String getSkinType() { return skinType; }
                public double getRating() { return rating; }
        }