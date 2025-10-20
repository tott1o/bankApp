package service;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.util.List;

public class BankService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private LoanDAO loanDAO = new LoanDAO();
    private LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();

    // -----------------------------
    // CUSTOMER SERVICES
    // -----------------------------
    public boolean registerCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }

    public Customer getCustomer(int id) {
        return customerDAO.getCustomerById(id);
    }

    public List<Customer> listCustomers() {
        return customerDAO.getAllCustomers();
    }

    // -----------------------------
    // ACCOUNT SERVICES
    // -----------------------------
    public boolean openAccount(Account account) {
        account.setOpenDate(LocalDate.now());
        return accountDAO.addAccount(account);
    }

    public boolean closeAccount(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) return false;

        account.setCloseDate(LocalDate.now());
        return accountDAO.updateAccount(account);
    }

    public boolean deposit(int accountId, double amount, String narration) {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) return false;

        account.setBalance(account.getBalance() + amount);
        accountDAO.updateAccount(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setNarration(narration);
        transactionDAO.addTransaction(transaction);

        return true;
    }

    public boolean withdraw(int accountId, double amount, String narration) {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null || account.getBalance() < amount) {
            System.out.println("Insufficient balance!");
            return false;
        }

        account.setBalance(account.getBalance() - amount);
        accountDAO.updateAccount(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setNarration(narration);
        transactionDAO.addTransaction(transaction);

        return true;
    }

    public double getBalance(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        return account != null ? account.getBalance() : 0.0;
    }

    // -----------------------------
    // LOAN SERVICES
    // -----------------------------
    public boolean applyLoan(Loan loan) {
        loan.setOpenDate(LocalDate.now());
        loan.setBalance(loan.getAmountSanctioned());
        return loanDAO.addLoan(loan);
    }

    public boolean repayLoan(int loanId, double paymentAmount, String receiptNo) {
        Loan loan = loanDAO.getLoanById(loanId);
        if (loan == null || paymentAmount > loan.getBalance()) return false;

        double newBalance = loan.getBalance() - paymentAmount;
        loan.setBalance(newBalance);
        loanDAO.updateLoan(loan);

        LoanPayment payment = new LoanPayment();
        payment.setLoanId(loanId);
        payment.setDisbursementAmount(paymentAmount);
        payment.setReceiptNo(receiptNo);
        payment.setRemainingBalance(newBalance);
        loanPaymentDAO.addLoanPayment(payment);

        return true;
    }
}
