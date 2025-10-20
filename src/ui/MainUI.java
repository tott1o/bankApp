package ui;

import model.*;
import service.BankService;
import java.util.Scanner;

public class MainUI {
    private static BankService bankService = new BankService();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Open Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. View Balance");
            System.out.println("6. Apply Loan");
            System.out.println("7. Repay Loan");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> openAccount();
                case 3 -> deposit();
                case 4 -> withdraw();
                case 5 -> viewBalance();
                case 6 -> applyLoan();
                case 7 -> repayLoan();
                case 8 -> { System.out.println("Exiting..."); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void registerCustomer() {
        Customer c = new Customer();
        System.out.print("Full name: "); c.setFullName(sc.nextLine());
        System.out.print("Address: "); c.setAddress(sc.nextLine());
        System.out.print("Contact No: "); c.setContactNo(sc.nextLine());
        System.out.print("Email: "); c.setEmail(sc.nextLine());
        System.out.print("PAN: "); c.setPanNumber(sc.nextLine());
        c.setCreatedAt(java.time.LocalDateTime.now());

        if (bankService.registerCustomer(c))
            System.out.println("Customer registered successfully!");
        else
            System.out.println("Failed to register customer.");
    }

    private static void openAccount() {
        Account a = new Account();
        System.out.print("Customer ID: "); a.setCustomerId(sc.nextInt());
        sc.nextLine();
        System.out.print("Account Type (SAVINGS/CURRENT): ");
        a.setAccountType(Account.AccountType.valueOf(sc.nextLine().toUpperCase()));
        a.setBalance(0.0);

        if (bankService.openAccount(a))
            System.out.println("Account opened successfully!");
        else
            System.out.println("Failed to open account.");
    }

    private static void deposit() {
        System.out.print("Account ID: "); int id = sc.nextInt();
        System.out.print("Amount: "); double amt = sc.nextDouble();
        sc.nextLine();
        System.out.print("Narration: "); String n = sc.nextLine();

        if (bankService.deposit(id, amt, n))
            System.out.println("Deposit successful!");
        else
            System.out.println("Deposit failed!");
    }

    private static void withdraw() {
        System.out.print("Account ID: "); int id = sc.nextInt();
        System.out.print("Amount: "); double amt = sc.nextDouble();
        sc.nextLine();
        System.out.print("Narration: "); String n = sc.nextLine();

        if (bankService.withdraw(id, amt, n))
            System.out.println("Withdrawal successful!");
        else
            System.out.println("Withdrawal failed!");
    }

    private static void viewBalance() {
        System.out.print("Account ID: "); int id = sc.nextInt();
        System.out.println("Balance: " + bankService.getBalance(id));
    }

    private static void applyLoan() {
        Loan loan = new Loan();
        System.out.print("Customer ID: "); loan.setCustomerId(sc.nextInt());
        sc.nextLine();
        System.out.print("Loan Type (PERSONAL_LOAN/HOME_LOAN/...): ");
        loan.setLoanType(Loan.LoanType.valueOf(sc.nextLine().toUpperCase()));
        System.out.print("Amount Sanctioned: "); loan.setAmountSanctioned(sc.nextDouble());
        sc.nextLine();

        if (bankService.applyLoan(loan))
            System.out.println("Loan applied successfully!");
        else
            System.out.println("Loan application failed.");
    }

    private static void repayLoan() {
        System.out.print("Loan ID: "); int id = sc.nextInt();
        System.out.print("Payment Amount: "); double amt = sc.nextDouble();
        sc.nextLine();
        System.out.print("Receipt No: "); String receipt = sc.nextLine();

        if (bankService.repayLoan(id, amt, receipt))
            System.out.println("Loan payment recorded successfully!");
        else
            System.out.println("Loan repayment failed.");
    }
}
