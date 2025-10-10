package com.pluralsight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private id;
    private LocalDate date;
    private LocalTime time;
    private String type;
    private String vendor;
    private BigDecimal amount;
    private String description;

    public Transaction(int id, LocalDate date, LocalTime time, String type, String vendor, BigDecimal amount, String description) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.vendor = vendor;
        this.amount = amount;
        this.description = description;

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
