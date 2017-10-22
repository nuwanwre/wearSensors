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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Student() {
        name = "";
        DoB = new Date();
        age = 0;
        height = 0.0f;
        weight = 0.0f;
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
}