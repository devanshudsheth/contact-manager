
/*****************************************************


 BY DEVANSHU D. SHETH
 dds160030
 CS6326


 This is the MyObject class that is defined for the list of objects which hold the data

 It can set/get values for first name, last name, phone, email and bdate
 Also has a toString method for the MyObject



 ******************************************************/

package com.example.devan.asg5_dds160030;


import java.lang.String;


public class MyObject
{

    //Empty Constructor
    public MyObject()
    {

    }

    //Empty Constructor
    public MyObject(String FirstName,String LastName, String Phone, String Email, String BDate)
    {

    }

    //getter for bdate
    public String getBDate() {
        return BDate;
    }

    //getter for email
    public String getEmail() {
        return Email;
    }

    //getter for FirstName
    public String getFirstName() {
        return FirstName;
    }

    //getter for LastName
    public String getLastName() {
        return LastName;
    }

    //getter for Phone
    public String getPhone() {
        return Phone;
    }

    // Override the ToString method for MyObject
    // Print all data as a single TAB spaced Line in data record
    // Each MyObject corresponds to one unique line
    @Override
    public String toString()
    {
        String newLine = System.getProperty("line.separator");
        return this.FirstName + "\t" + this.LastName + "\t" + this.Phone + "\t" + this.Email + "\t" + this.BDate + newLine ;
    }

    //first name
    public String FirstName;

    //last name
    public String LastName;

    //phone
    public String Phone;

    //email
    public String Email;

    //bdate
    public String BDate;


}
