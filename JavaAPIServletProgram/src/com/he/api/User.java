package com.he.api;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String cityId;
    private String city;
    
    public User(int id, String firstName, String lastName, String cityId, String city){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cityId = cityId;
        this.city = city;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    
    public String getCity() {
    	return city;
    }
    
    public void setCity(String city) {
    	this.city = city;
    }
    
    @Override
    public String toString(){
        return String.format("%d %s %s %s",id,firstName,lastName,cityId);
    }
}

