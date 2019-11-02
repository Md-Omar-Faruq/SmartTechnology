package com.example.smarttechnology.Finance;

public class FinanceDataEntryClass {

    String trnastionDate,InorEx,ammount,narration,transtionId;

    public FinanceDataEntryClass() {

    }

    public FinanceDataEntryClass(String trnastionDate, String inorEx, String ammount, String narration, String transtionId) {
        this.trnastionDate = trnastionDate;
        InorEx = inorEx;
        this.ammount = ammount;
        this.narration = narration;
        this.transtionId = transtionId;
    }

    public String getTrnastionDate() {
        return trnastionDate;
    }

    public void setTrnastionDate(String trnastionDate) {
        this.trnastionDate = trnastionDate;
    }

    public String getInorEx() {
        return InorEx;
    }

    public void setInorEx(String inorEx) {
        InorEx = inorEx;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTranstionId() {
        return transtionId;
    }

    public void setTranstionId(String transtionId) {
        this.transtionId = transtionId;
    }
}
