package com.pluralsight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String vendor;
    private double amount;
    private String description;

    public Transaction( LocalDate date, LocalTime time, String vendor, double amount, String description) {
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.amount = amount;
        this.description = description;
    }

    public String toString() {
        return String.format("%s %s | %-15s | %-20s | $%.2f", date, time, vendor, description, amount);
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

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
