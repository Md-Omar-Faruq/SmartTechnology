package com.example.smarttechnology.ModelUserRecyclerView;

public class ModelUser {

    String fullName, userName, email, uid, birthDate, gender, cover,image;

    public ModelUser() {

    }

    public ModelUser(String fullName, String userName, String email, String uid, String birthDate, String gender, String cover, String image) {
            this.fullName = fullName;
            this.userName = userName;
            this.email = email;
            this.uid = uid;
            this.birthDate = birthDate;
            this.gender = gender;
            this.cover = cover;
            this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
