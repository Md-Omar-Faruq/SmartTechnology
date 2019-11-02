package com.example.smarttechnology.Notic;

public class Notic {

    public String noticId,noticTitle,description,noticimage,noticTime;


    public Notic() {
    }

    public Notic(String noticId, String noticTitle, String description, String noticimage, String noticTime) {
        this.noticId = noticId;
        this.noticTitle = noticTitle;
        this.description = description;
        this.noticimage = noticimage;
        this.noticTime = noticTime;
    }

    public String getNoticId() {
        return noticId;
    }

    public void setNoticId(String noticId) {
        this.noticId = noticId;
    }

    public String getNoticTitle() {
        return noticTitle;
    }

    public void setNoticTitle(String noticTitle) {
        this.noticTitle = noticTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoticimage() {
        return noticimage;
    }

    public void setNoticimage(String noticimage) {
        this.noticimage = noticimage;
    }

    public String getNoticTime() {
        return noticTime;
    }

    public void setNoticTime(String noticTime) {
        this.noticTime = noticTime;
    }
}
