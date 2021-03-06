package com.example.lifelocator360.DataBaseManagement;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "contact")

public class Contact {
    String allInformationO1;
    String allInformationO2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    private Integer id ;

    @ColumnInfo(name = "contact_name")
    private String name;

    @ColumnInfo(name = "contact_surname")
    private String surname;

    @ColumnInfo(name = "contact_phone")
    private String phone;

    @ColumnInfo(name = "contact_adress")
    private String address;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    public Contact(){

    }

    public Contact(String name, String surname, String phone, String address, String latitude,String longitude) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAllInformation(Contact contact){
        return getName() + getSurname() + getAddress() + getPhone();
    }

}
