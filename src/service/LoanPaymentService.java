package service;

import dao.LoanPaymentDAO;
import dao.LoanDAO; // Required to update the main loan balance
import model.LoanPayment;
import model.Loan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LoanPaymentService {
    private final LoanPaymentDAO loanPaymentDAO;
    private final LoanDAO loanDAO; // Declared as final field

    // CORRECTED Constructor: Accepts both DAOs
    public LoanPaymentService(LoanPaymentDAO loanPaymentDAO, LoanDAO loanDAO) {
        this.loanPaymentDAO = loanPaymentDAO;
        this.loanDAO = loanDAO; // Initialization of the LoanDAO field
    }

    public List<LoanPayment> getPaymentsByLoanId(int loanId) {
        return loanPaymentDAO.getPaymentsByLoanId(loanId);
    }

    public List<LoanPayment> getAllLoanPayments() {
        return loanPaymentDAO.getAllLoanPayments();
    }

    /**
     * Records a loan payment and updates the main Loan balance.
     */
    public boolean recordPayment(LoanPayment payment) {
        if (payment.getDisbursementAmount() <= 0) return false;

        Loan loan = loanDAO.getLoanById(payment.getLoanId());
        if (loan == null) {
            System.err.println("Loan not found for payment.");
            return false;
        }

        double paymentAmount = payment.getDisbursementAmount();
        double newBalance = loan.getBalance() - paymentAmount;

        // Business Logic: Prevent overpayment (negative balance)
        if (newBalance < -0.001) { // Use a small tolerance for floating point comparison
            System.err.println("Payment exceeds remaining loan balance.");
            return false;
        }

        // Correct the balance to zero if it's slightly negative due to floating point math
        if (newBalance < 0) {
            newBalance = 0;
        }

        // 1. Update Loan Balance
        loan.setBalance(newBalance);
        if (newBalance == 0) {
            loan.setCloseDate(LocalDate.now());
        }

        if (loanDAO.updateLoan(loan)) {
            // 2. Record Payment Transaction
            payment.setRemainingBalance(newBalance);
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(LocalDateTime.now());
            }
            return loanPaymentDAO.addLoanPayment(payment);
        }
        return false;
    }
}