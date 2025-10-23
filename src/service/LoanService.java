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
}