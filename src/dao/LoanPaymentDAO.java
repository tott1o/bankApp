package dao;

import model.LoanPayment;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class LoanPaymentDAO {

    // 1. INSERT LOAN PAYMENT
    public boolean addLoanPayment(LoanPayment payment) {
        String sql = "INSERT INTO loan_payments (loan_id, disbursement_amount, receipt_no, payment_date, remaining_balance) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getLoanId());
            stmt.setDouble(2, payment.getDisbursementAmount());
            stmt.setString(3, payment.getReceiptNo());

            if (payment.getPaymentDate() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(payment.getPaymentDate()));
            } else {
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setDouble(5, payment.getRemainingBalance());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    payment.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. GET PAYMENT BY ID
    public LoanPayment getLoanPaymentById(int id) {
        String sql = "SELECT * FROM loan_payments WHERE id = ?";
        LoanPayment payment = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                payment = mapResultSetToLoanPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payment;
    }

    // 3. GET ALL PAYMENTS FOR A LOAN
    public List<LoanPayment> getPaymentsByLoanId(int loanId) {
        String sql = "SELECT * FROM loan_payments WHERE loan_id = ?";
        List<LoanPayment> payments = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToLoanPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // 4. GET ALL LOAN PAYMENTS
    public List<LoanPayment> getAllLoanPayments() {
        String sql = "SELECT * FROM loan_payments";
        List<LoanPayment> payments = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapResultSetToLoanPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // 5. UPDATE PAYMENT
    public boolean updateLoanPayment(LoanPayment payment) {
        String sql = "UPDATE loan_payments SET loan_id = ?, disbursement_amount = ?, receipt_no = ?, " +
                "payment_date = ?, remaining_balance = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getLoanId());
            stmt.setDouble(2, payment.getDisbursementAmount());
            stmt.setString(3, payment.getReceiptNo());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setDouble(5, payment.getRemainingBalance());
            stmt.setInt(6, payment.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. DELETE PAYMENT
    public boolean deleteLoanPayment(int id) {
        String sql = "DELETE FROM loan_payments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Convert ResultSet to LoanPayment Model
    private LoanPayment mapResultSetToLoanPayment(ResultSet rs) throws SQLException {
        LoanPayment payment = new LoanPayment();
        payment.setId(rs.getInt("id"));
        payment.setLoanId(rs.getInt("loan_id"));
        payment.setDisbursementAmount(rs.getDouble("disbursement_amount"));
        payment.setReceiptNo(rs.getString("receipt_no"));

        Timestamp timestamp = rs.getTimestamp("payment_date");
        if (timestamp != null) {
            payment.setPaymentDate(timestamp.toLocalDateTime());
        }

        payment.setRemainingBalance(rs.getDouble("remaining_balance"));

        return payment;
    }
}
