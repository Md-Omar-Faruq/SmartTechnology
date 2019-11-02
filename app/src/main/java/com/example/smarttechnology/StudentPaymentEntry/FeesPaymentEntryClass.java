package com.example.smarttechnology.StudentPaymentEntry;

public class FeesPaymentEntryClass {

    String studentPid,studentPname,studentPmobileNo,studentPFname,studentPMname,
            studentPemail,studentPbatch,studentPcource,paymentDate,studentPcourseFees,
            discountAmmount,afterTotal,paidAmmount,dueAmmount;

    public FeesPaymentEntryClass() {
    }

    public FeesPaymentEntryClass(String studentPid, String studentPname, String studentPmobileNo, String studentPFname, String studentPMname, String studentPemail, String studentPbatch, String studentPcource, String paymentDate, String studentPcourseFees, String discountAmmount, String afterTotal, String paidAmmount, String dueAmmount) {
        this.studentPid = studentPid;
        this.studentPname = studentPname;
        this.studentPmobileNo = studentPmobileNo;
        this.studentPFname = studentPFname;
        this.studentPMname = studentPMname;
        this.studentPemail = studentPemail;
        this.studentPbatch = studentPbatch;
        this.studentPcource = studentPcource;
        this.paymentDate = paymentDate;
        this.studentPcourseFees = studentPcourseFees;
        this.discountAmmount = discountAmmount;
        this.afterTotal = afterTotal;
        this.paidAmmount = paidAmmount;
        this.dueAmmount = dueAmmount;
    }

    public String getStudentPid() {
        return studentPid;
    }

    public void setStudentPid(String studentPid) {
        this.studentPid = studentPid;
    }

    public String getStudentPname() {
        return studentPname;
    }

    public void setStudentPname(String studentPname) {
        this.studentPname = studentPname;
    }

    public String getStudentPmobileNo() {
        return studentPmobileNo;
    }

    public void setStudentPmobileNo(String studentPmobileNo) {
        this.studentPmobileNo = studentPmobileNo;
    }

    public String getStudentPFname() {
        return studentPFname;
    }

    public void setStudentPFname(String studentPFname) {
        this.studentPFname = studentPFname;
    }

    public String getStudentPMname() {
        return studentPMname;
    }

    public void setStudentPMname(String studentPMname) {
        this.studentPMname = studentPMname;
    }

    public String getStudentPemail() {
        return studentPemail;
    }

    public void setStudentPemail(String studentPemail) {
        this.studentPemail = studentPemail;
    }

    public String getStudentPbatch() {
        return studentPbatch;
    }

    public void setStudentPbatch(String studentPbatch) {
        this.studentPbatch = studentPbatch;
    }

    public String getStudentPcource() {
        return studentPcource;
    }

    public void setStudentPcource(String studentPcource) {
        this.studentPcource = studentPcource;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStudentPcourseFees() {
        return studentPcourseFees;
    }

    public void setStudentPcourseFees(String studentPcourseFees) {
        this.studentPcourseFees = studentPcourseFees;
    }

    public String getDiscountAmmount() {
        return discountAmmount;
    }

    public void setDiscountAmmount(String discountAmmount) {
        this.discountAmmount = discountAmmount;
    }

    public String getAfterTotal() {
        return afterTotal;
    }

    public void setAfterTotal(String afterTotal) {
        this.afterTotal = afterTotal;
    }

    public String getPaidAmmount() {
        return paidAmmount;
    }

    public void setPaidAmmount(String paidAmmount) {
        this.paidAmmount = paidAmmount;
    }

    public String getDueAmmount() {
        return dueAmmount;
    }

    public void setDueAmmount(String dueAmmount) {
        this.dueAmmount = dueAmmount;
    }
}
