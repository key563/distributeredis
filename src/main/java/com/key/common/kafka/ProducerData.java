package com.key.common.kafka;

import java.io.Serializable;

public class ProducerData implements Serializable {

    private String id;
    private String name;
    private String address;
    private int age;
    private String favorate;

    public ProducerData(){

    }
    public ProducerData(String name, String address, int age, String favorate) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.favorate = favorate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFavorate() {
        return favorate;
    }

    public void setFavorate(String favorate) {
        this.favorate = favorate;
    }
}
