package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinanicalTracker {
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        try { BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if ((line.isEmpty())) continue;

                try {
                    String[] token = line.split("\\|");
                    if (token.length != 5) continue;
                    LocalDate date = LocalDate.parse(token[0]);
                    LocalTime time = LocalTime.parse(token[1]);
                    String vendor = token[2];
                    double amount = Double.parseDouble(token[3]);
                    String description = token[4];
                    transactions.add(new Transaction(date, time, vendor, amount, description));

                } catch (Exception e) {
                    System.err.println("Invalid line format: " + line);
                }
            }
             System.out.println("Loaded " + transactions.size() + " transactions.");

        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {
        boolean adding = true;
        while (adding) {
            try {
                System.out.println("Enter vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.println("Enter amount: ");
                double amount = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("Enter description");
                String description = scanner.nextLine();
                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();

                transactions.add(new Transaction(date, time, vendor, amount, description));
                System.out.println("Deposit added successfully!");

                System.out.println("Add another deposit? (Y/N)");
                String response= scanner.nextLine().trim();
                if (!response.equalsIgnoreCase("Y")) {
                    adding = false;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
    private static void addPayment(Scanner scanner) {
        boolean adding = true;
        while (adding) {
            try {
                System.out.println("Enter date and time (yyyy-MM-dd  HH:mm:ss): ");
                String dateTimeInput = scanner.nextLine().trim();
                String[] dateTimeParts = dateTimeInput.split(" ");
                LocalDate  date = LocalDate.parse(dateTimeParts[0]);
                LocalTime time = LocalTime.parse(dateTimeParts[1]);

                System.out.println("Enter vendor: ");
                String vendor = scanner.nextLine().trim();
                System.out.println("Enter amount: ");
                double amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount > 0) amount = -amount;

                System.out.println("Enter description: ");
                String description = scanner.nextLine().trim();

                transactions.add(new Transaction(date, time, vendor, amount, description));
                System.out.println("Payments added successfully!");
                System.out.println("add another payments? ");
                String response = scanner.nextLine().trim();
                if (!response.equalsIgnoreCase("Y")) {
                    adding = false;
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void displayLedger() { /* TODO – print all transactions in column format */ }

    private static void displayDeposits() { /* TODO – only amount > 0               */ }

    private static void displayPayments() { /* TODO – only amount < 0               */ }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {/* TODO – month-to-date report */ }
                case "2" -> {/* TODO – previous month report */ }
                case "3" -> {/* TODO – year-to-date report   */ }
                case "4" -> {/* TODO – previous year report  */ }
                case "5" -> {/* TODO – prompt for vendor then report */ }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range
    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        /* TODO – return LocalDate or null */
        return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
}

