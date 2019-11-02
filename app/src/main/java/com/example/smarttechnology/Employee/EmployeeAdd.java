package com.example.smarttechnology.Employee;

public class EmployeeAdd {

    String employimageUri;
    String employId, employName, employMobileNo, employFname, employMname, employEmail, employBirthDate,
    employGender,employAddDate, employCountrt, employPresentAddr, employPermanentAddr;


    public EmployeeAdd() {
    }

    public EmployeeAdd(String employimageUri, String employId, String employName, String employMobileNo,
                       String employFname, String employMname, String employEmail, String employBirthDate,
                       String employGender, String employAddDate, String employCountrt, String employPresentAddr,
                       String employPermanentAddr) {

        this.employimageUri = employimageUri;
        this.employId = employId;
        this.employName = employName;
        this.employMobileNo = employMobileNo;
        this.employFname = employFname;
        this.employMname = employMname;
        this.employEmail = employEmail;
        this.employBirthDate = employBirthDate;
        this.employGender = employGender;
        this.employAddDate = employAddDate;
        this.employCountrt = employCountrt;
        this.employPresentAddr = employPresentAddr;
        this.employPermanentAddr = employPermanentAddr;

    }

    public String getEmployimageUri() {
        return employimageUri;
    }

    public void setEmployimageUri(String employimageUri) {
        this.employimageUri = employimageUri;
    }

    public String getEmployId() {
        return employId;
    }

    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public String getEmployName() {
        return employName;
    }

    public void setEmployName(String employName) {
        this.employName = employName;
    }

    public String getEmployMobileNo() {
        return employMobileNo;
    }

    public void setEmployMobileNo(String employMobileNo) {
        this.employMobileNo = employMobileNo;
    }

    public String getEmployFname() {
        return employFname;
    }

    public void setEmployFname(String employFname) {
        this.employFname = employFname;
    }

    public String getEmployMname() {
        return employMname;
    }

    public void setEmployMname(String employMname) {
        this.employMname = employMname;
    }

    public String getEmployEmail() {
        return employEmail;
    }

    public void setEmployEmail(String employEmail) {
        this.employEmail = employEmail;
    }

    public String getEmployBirthDate() {
        return employBirthDate;
    }

    public void setEmployBirthDate(String employBirthDate) {
        this.employBirthDate = employBirthDate;
    }

    public String getEmployGender() {
        return employGender;
    }

    public void setEmployGender(String employGender) {
        this.employGender = employGender;
    }

    public String getEmployAddDate() {
        return employAddDate;
    }

    public void setEmployAddDate(String employAddDate) {
        this.employAddDate = employAddDate;
    }

    public String getEmployCountrt() {
        return employCountrt;
    }

    public void setEmployCountrt(String employCountrt) {
        this.employCountrt = employCountrt;
    }

    public String getEmployPresentAddr() {
        return employPresentAddr;
    }

    public void setEmployPresentAddr(String employPresentAddr) {
        this.employPresentAddr = employPresentAddr;
    }

    public String getEmployPermanentAddr() {
        return employPermanentAddr;
    }

    public void setEmployPermanentAddr(String employPermanentAddr) {
        this.employPermanentAddr = employPermanentAddr;
    }
}
