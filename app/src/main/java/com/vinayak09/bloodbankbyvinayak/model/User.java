package com.vinayak09.bloodbankbyvinayak.model;

public class User {
    String FName,LName,UID,Email,BloodGroup,Mobile,State,District,Tehsil,Village,Step, Visible,RequestBlood;

    public User(String FName, String LName, String UID, String email, String bloodGroup, String mobile, String state, String district, String tehsil, String village, String step, String visible, String requestBlood) {
        this.FName = FName;
        this.LName = LName;
        this.UID = UID;
        Email = email;
        BloodGroup = bloodGroup;
        Mobile = mobile;
        State = state;
        District = district;
        Tehsil = tehsil;
        Village = village;
        Step = step;
        Visible = visible;
        RequestBlood = requestBlood;
    }

    public User() {
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getTehsil() {
        return Tehsil;
    }

    public void setTehsil(String tehsil) {
        Tehsil = tehsil;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getStep() {
        return Step;
    }

    public void setStep(String step) {
        Step = step;
    }

    public String getVisible() {
        return Visible;
    }

    public void setVisible(String visible) {
        Visible = visible;
    }

    public String getRequestBlood() {
        return RequestBlood;
    }

    public void setRequestBlood(String requestBlood) {
        RequestBlood = requestBlood;
    }
}
