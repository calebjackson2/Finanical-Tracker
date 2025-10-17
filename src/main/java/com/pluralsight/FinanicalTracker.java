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
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }
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
                String response = scanner.nextLine().trim();
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
                LocalDate date = LocalDate.parse(dateTimeParts[0]);
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

    private static void displayLedger() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.\n");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n", "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("---------------------------------------------------------------------------------");
        for (int i = transactions.size() - 1; i > 0; i--) {
            Transaction t = transactions.get(i);
            System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                    t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
        }
        System.out.println("-----------------------------------------------------------------------\n");
    }

    private static void displayDeposits() {
        boolean found = false;
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n",
                "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("---------------------------------------------------------------------------------");

        for (int i = FinanicalTracker.transactions.size() - 1; i >= 0; i--) {
            Transaction t = FinanicalTracker.transactions.get(i);

            if (t.getAmount() > 0) {
                found = true;
                System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                        t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
            }
        }
        if (!found) {
            System.out.println("No deposits found.");
        }

        System.out.println("-----------------------------------------------------------------------\n");
    }

    private static void displayPayments() {
        boolean found = false;
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n",
                "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("---------------------------------------------------------------------------------");

        for (int i = FinanicalTracker.transactions.size() - 1; i >= 0; i--) {
            Transaction t = FinanicalTracker.transactions.get(i);

            if (t.getAmount() > 0) {
                found = true;
                System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                        t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
            }
        }
        if (!found) {
            System.out.println("No payments found.");
        }

        System.out.println("-----------------------------------------------------------------------\n");
    }

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
                case "1" -> monthToDateReport();
                case "2" -> previousMonthReport();
                case "3" -> yearToDateReport();
                case "4" -> previousYearReport();
                case "5" -> {
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    yearToDateReport();
                }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        boolean found = false;
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n",
                "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("--------------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            LocalDate date = t.getDate();
            if ((date.isEqual(start) || date.isAfter(start)) &&
                    (date.isEqual(end) || date.isBefore(end))) {
                found = true;
                System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                        t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
            }
        }

        if (!found) System.out.println("No transactions found in that date range.");
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void filterTransactionsByVendor(String vendor) {
        boolean found = false;

        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n",
                "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("---------------------------------------------------------------------------------");

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                found = true;
                System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                        t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
            }
        }

        if (!found) System.out.println("No transactions found for vendor: " + vendor);
        System.out.println("---------------------------------------------------------------------------------\n");
    }

    private static void customSearch(Scanner scanner) {
        System.out.print("Enter start date (YYYY-MM-DD) or leave blank: ");
        String startInput = scanner.nextLine().trim();
        LocalDate start = startInput.isEmpty() ? LocalDate.MIN : parseDate(startInput);

        System.out.print("Enter end date (YYYY-MM-DD) or leave blank: ");
        String endInput = scanner.nextLine().trim();
        LocalDate end = endInput.isEmpty() ? LocalDate.MAX : parseDate(endInput);

        System.out.print("Enter vendor name (optional): ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter description keyword (optional): ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter exact amount (optional): ");
        String amountInput = scanner.nextLine().trim();
        Double amount = amountInput.isEmpty() ? null : parseDouble(amountInput);

        boolean found = false;
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-20s %-10s %-20s%n",
                "Date", "Time", "Vendor", "Amount", "Description");
        System.out.println("---------------------------------------------------------------------------------");

        for (Transaction t : transactions) {
            boolean match = true;

            if (start != null && end != null) {
                if (t.getDate().isBefore(start) || t.getDate().isAfter(end)) match = false;
            }
            if (!vendor.isEmpty() && !t.getVendor().equalsIgnoreCase(vendor)) match = false;
            if (!description.isEmpty() && !t.getDescription().toLowerCase().contains(description.toLowerCase()))
                match = false;
            if (amount != null && t.getAmount() != amount) match = false;
            if (match) {
                found = true;
                System.out.printf("%-12s %-10s %-20s %-10.2f %-20s%n",
                        t.getDate(), t.getTime(), t.getVendor(), t.getAmount(), t.getDescription());
            }
        }

        if (!found) System.out.println("No transactions match your criteria.");
        System.out.println("---------------------------------------------------------------------------------\n");
    }

    /* ------------------------------------------------------------------
                   Utility parsers (you can reuse in many places)
                   ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }

    private static Double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            System.out.println("Invalid number format.");
            return null;
        }
    }

    private static void monthToDateReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        System.out.println("\n--- Month To Date Report (" + startOfMonth + " to " + today + ") ---");
        filterTransactionsByDate(startOfMonth, today);
    }

    private static void previousMonthReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = startOfPreviousMonth.withDayOfMonth(startOfPreviousMonth.lengthOfMonth());

        System.out.println("\n--- Previous Month Report (" + startOfPreviousMonth + " to " + endOfPreviousMonth + ") ---");
        filterTransactionsByDate(startOfPreviousMonth, endOfPreviousMonth);
    }

    private static void yearToDateReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);

        System.out.println("\n--- Year To Date Report (" + startOfYear + " to " + today + ") ---");
        filterTransactionsByDate(startOfYear, today);
    }

    private static void previousYearReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfPreviousYear = today.minusYears(1).withDayOfYear(1);
        LocalDate endOfPreviousYear = startOfPreviousYear.withDayOfYear(startOfPreviousYear.lengthOfYear());

        System.out.println("\n--- Previous Year Report (" + startOfPreviousYear + " to " + endOfPreviousYear + ") ---");
        filterTransactionsByDate(startOfPreviousYear, endOfPreviousYear);
    }
}

