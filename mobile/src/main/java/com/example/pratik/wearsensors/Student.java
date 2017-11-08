package com.example.pratik.wearsensors;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pratik on 10/22/17.
 */

public class Student {

    private String name;
    private Date DoB;
    private int age;
    private float height;
    private float weight;
    private String userName ="";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Student() {
        name = "";
        DoB = new Date();
        age = 0;
        height = 0.0f;
        weight = 0.0f;
    }

    public Student(String Name, String DoB, String height, String weight) {
        name = Name;
        try{
            this.DoB = dateFormat.parse(DoB);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        age = 0;
        this.height = Float.parseFloat(height);
        this.weight = Float.parseFloat(weight);
        userName = name.replace(" ","_") + String.valueOf((int) Math.random() * 100);
    }

    public void setName(String n) {
        name = n;
    }

    public void setDoB(String d) {
        try {
            DoB = dateFormat.parse(d);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setAge(int a) {
        age = a;
    }

    public void setHeight(float h) {
        height = h;
    }

    public void setWeight(float w) {
        weight = w;
    }

    public String getName() {
        return name;
    }

    public Date getDoB() {
        return DoB;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public String getUserName() {
        return userName;
    }
}
