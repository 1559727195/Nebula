package com.zhongtianli.nebula.model;

/**
 * Created by robin on 2016/5/24.
 */
public class Flight {
    private String company;
    private String airlineCode;
    private String start;
    private String arrive;
    private String mode;
    private String week;

    public Flight(){}

    public Flight(String company, String airlineCode, String start, String arrive, String mode, String week) {
        this.company = company;
        this.airlineCode = airlineCode;
        this.start = start;
        this.arrive = arrive;
        this.mode = mode;
        this.week = week;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return this.getAirlineCode()+":"+this.getCompany()+","+this.getMode()+","+this.getWeek();
    }
}
