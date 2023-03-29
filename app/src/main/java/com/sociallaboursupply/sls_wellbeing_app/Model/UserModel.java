package com.sociallaboursupply.sls_wellbeing_app.Model;

public class UserModel {

    public Integer id;
    public String email;
    public String password;
    public Boolean isMentor = false;
    public String firstName;
    public String lastName;
    public Integer mentorId;

    public String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getMentor() {
        return isMentor;
    }

    public void setMentor(Boolean mentor) {
        isMentor = mentor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getMentorId() {
        return mentorId;
    }

    public void setMentorId(Integer mentorId) {
        this.mentorId = mentorId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
