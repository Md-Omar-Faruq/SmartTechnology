package com.example.smarttechnology.Teacher;

public class TeacherAdd {

    String teacherImageUri;
    String teacherId, teacherName, teacherMobileNo, teacherFname, teacherMname, teacherEmail, teacherSubject, teacherCountry,
    teacherBrithDate, teacherGender, teacherAddDate, teacherPresentAddr, teacherPermanentAddr;

    public TeacherAdd() {
    }

    public TeacherAdd(String teacherImageUri, String teacherId, String teacherName, String teacherMobileNo, String teacherFname,
                      String teacherMname, String teacherEmail, String teacherSubject, String teacherCountry, String teacherBrithDate,
                      String teacherGender, String teacherAddDate, String teacherPresentAddr, String teacherPermanentAddr) {

        this.teacherImageUri = teacherImageUri;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherMobileNo = teacherMobileNo;
        this.teacherFname = teacherFname;
        this.teacherMname = teacherMname;
        this.teacherEmail = teacherEmail;
        this.teacherSubject = teacherSubject;
        this.teacherCountry = teacherCountry;
        this.teacherBrithDate = teacherBrithDate;
        this.teacherGender = teacherGender;
        this.teacherAddDate = teacherAddDate;
        this.teacherPresentAddr = teacherPresentAddr;
        this.teacherPermanentAddr = teacherPermanentAddr;

    }

    public String getTeacherImageUri() {
        return teacherImageUri;
    }

    public void setTeacherImageUri(String teacherImageUri) {
        this.teacherImageUri = teacherImageUri;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherMobileNo() {
        return teacherMobileNo;
    }

    public void setTeacherMobileNo(String teacherMobileNo) {
        this.teacherMobileNo = teacherMobileNo;
    }

    public String getTeacherFname() {
        return teacherFname;
    }

    public void setTeacherFname(String teacherFname) {
        this.teacherFname = teacherFname;
    }

    public String getTeacherMname() {
        return teacherMname;
    }

    public void setTeacherMname(String teacherMname) {
        this.teacherMname = teacherMname;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherSubject() {
        return teacherSubject;
    }

    public void setTeacherSubject(String teacherSubject) {
        this.teacherSubject = teacherSubject;
    }

    public String getTeacherCountry() {
        return teacherCountry;
    }

    public void setTeacherCountry(String teacherCountry) {
        this.teacherCountry = teacherCountry;
    }

    public String getTeacherBrithDate() {
        return teacherBrithDate;
    }

    public void setTeacherBrithDate(String teacherBrithDate) {
        this.teacherBrithDate = teacherBrithDate;
    }

    public String getTeacherGender() {
        return teacherGender;
    }

    public void setTeacherGender(String teacherGender) {
        this.teacherGender = teacherGender;
    }

    public String getTeacherAddDate() {
        return teacherAddDate;
    }

    public void setTeacherAddDate(String teacherAddDate) {
        this.teacherAddDate = teacherAddDate;
    }

    public String getTeacherPresentAddr() {
        return teacherPresentAddr;
    }

    public void setTeacherPresentAddr(String teacherPresentAddr) {
        this.teacherPresentAddr = teacherPresentAddr;
    }

    public String getTeacherPermanentAddr() {
        return teacherPermanentAddr;
    }

    public void setTeacherPermanentAddr(String teacherPermanentAddr) {
        this.teacherPermanentAddr = teacherPermanentAddr;
    }
}
