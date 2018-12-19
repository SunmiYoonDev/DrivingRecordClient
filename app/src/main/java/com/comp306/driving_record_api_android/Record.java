package com.comp306.driving_record_api_android;

public class Record {
    private String rid;
    private String licenseID;
    private String ltype;
    private String aType;
    private String aDate;
    private String aLocation;

    public Record() {
    }

    public Record(String rid, String licenseID, String ltype, String aType, String aDate, String aLocation) {
        this.rid = rid;
        this.licenseID = licenseID;
        this.ltype = ltype;
        this.aType = aType;
        this.aDate = aDate;
        this.aLocation = aLocation;
    }

    public Record(String licenseID, String ltype, String aType, String aDate, String aLocation) {
        this.licenseID = licenseID;
        this.ltype = ltype;
        this.aType = aType;
        this.aDate = aDate;
        this.aLocation = aLocation;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
    }

    public String getLtype() {
        return ltype;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaLocation() {
        return aLocation;
    }

    public void setaLocation(String aLocation) {
        this.aLocation = aLocation;
    }
}
