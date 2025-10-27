package service;

import dao.LoanDAO;
import model.Loan;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final LoanDAO loanDAO;

    public LoanService(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
    }

    public boolean createLoan(Loan loan) {
        if (loan.getAmountSanctioned() <= 0) {
            System.err.println("Loan amount must be positive.");
            return false;
        }
        if (loan.getOpenDate() == null) {
            loan.setOpenDate(LocalDate.now());
        }
        // Initial balance should be equal to the sanctioned amount upon creation
        loan.setBalance(loan.getAmountSanctioned());
        return loanDAO.addLoan(loan);
    }

    public Loan getLoan(int id) {
        return loanDAO.getLoanById(id);
    }

    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }

    public boolean updateLoan(Loan loan) {
        if (loan.getId() <= 0) {
            System.err.println("Cannot update loan: ID is missing.");
            return false;
        }
        return loanDAO.updateLoan(loan);
    }

    public boolean deleteLoan(int id) {
        // Business logic: Only delete if the balance is fully paid (zero).
        Loan loan = loanDAO.getLoanById(id);
        if (loan == null) return false;
        if (loan.getBalance() > 0) {
            System.err.println("Cannot delete loan ID " + id + ". Outstanding balance remains.");
            return false;
        }
        return loanDAO.deleteLoan(id);
    }

    /**
     * Apply monthly interest to a single loan.
     * Interest is calculated as: (balance * annual_rate) / 12
     * @param loanId The ID of the loan to apply interest to
     * @return true if interest was applied successfully
     */
    public boolean applyMonthlyInterest(int loanId) {
        Loan loan = loanDAO.getLoanById(loanId);
        if (loan == null) {
            System.err.println("Loan not found for applying interest.");
            return false;
        }

        if (loan.getInterestRate() <= 0) {
            System.err.println("Interest rate is not set or zero for loan ID " + loanId);
            return false;
        }

        // Calculate monthly interest
        double monthlyRate = loan.getInterestRate() / 12.0 / 100.0;
        double interest = loan.getBalance() * monthlyRate;
        
        if (interest <= 0.0) {
            System.err.println("Calculated interest is zero for loan ID " + loanId);
            return false;
        }

        loan.setBalance(loan.getBalance() + interest);
        return loanDAO.updateLoan(loan);
    }

    /**
     * Apply monthly interest to all active loans.
     * @return true if interest was successfully applied to all active loans
     */
    public boolean applyMonthlyInterestToAllLoans() {
        int successCount = 0;
        int activeCount = 0;
        List<Loan> loans = loanDAO.getAllLoans();
        
        for (Loan loan : loans) {
            // Skip closed loans
            if (loan.getCloseDate() != null) continue;
            activeCount++;
            
            try {
                if (applyMonthlyInterest(loan.getId())) {
                    successCount++;
                }
            } catch (Exception ex) {
                // Log error and continue with next loan
                System.err.println("Error applying interest to loan ID " + loan.getId() + ": " + ex.getMessage());
                return false;
            }
        }
        
        return successCount == activeCount && activeCount > 0;
    }
}