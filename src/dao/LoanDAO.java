package dao;

import model.Loan;
import model.Loan.LoanType;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // 1. INSERT NEW LOAN
    public boolean addLoan(Loan loan) {
        String sql = "INSERT INTO loans (customer_id, loan_type, amount_sanctioned, balance, interest_rate, open_date, close_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, loan.getCustomerId());
            stmt.setString(2, loan.getLoanType().name());
            stmt.setDouble(3, loan.getAmountSanctioned());
            stmt.setDouble(4, loan.getBalance());
            stmt.setDouble(5, loan.getInterestRate());
            stmt.setDate(6, Date.valueOf(loan.getOpenDate()));
            stmt.setDate(7, loan.getCloseDate() != null ? Date.valueOf(loan.getCloseDate()) : null);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    loan.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. GET LOAN BY ID

    public Loan getLoanById(int id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        Loan loan = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loan = mapResultSetToLoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }


    // 3. GET ALL LOANS
    public List<Loan> getAllLoans() {
        String sql = "SELECT * FROM loans";
        List<Loan> loans = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // 4. UPDATE LOAN
    public boolean updateLoan(Loan loan) {
        String sql = "UPDATE loans SET customer_id = ?, loan_type = ?, amount_sanctioned = ?, " +
                "balance = ?, interest_rate = ?, open_date = ?, close_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getCustomerId());
            stmt.setString(2, loan.getLoanType().name());
            stmt.setDouble(3, loan.getAmountSanctioned());
            stmt.setDouble(4, loan.getBalance());
            stmt.setDouble(5, loan.getInterestRate());
            stmt.setDate(6, Date.valueOf(loan.getOpenDate()));
            stmt.setDate(7, loan.getCloseDate() != null ? Date.valueOf(loan.getCloseDate()) : null);
            stmt.setInt(8, loan.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. DELETE LOAN
    public boolean deleteLoan(int id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // HELPER: MAP RESULTSET TO MODEL
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setCustomerId(rs.getInt("customer_id"));
        loan.setLoanType(LoanType.valueOf(rs.getString("loan_type")));
        loan.setAmountSanctioned(rs.getDouble("amount_sanctioned"));
        loan.setBalance(rs.getDouble("balance"));
        loan.setInterestRate(rs.getDouble("interest_rate"));

        Date openDate = rs.getDate("open_date");
        if (openDate != null) {
            loan.setOpenDate(openDate.toLocalDate());
        }

        Date closeDate = rs.getDate("close_date");
        if (closeDate != null) {
            loan.setCloseDate(closeDate.toLocalDate());
        }

        return loan;
    }
}
