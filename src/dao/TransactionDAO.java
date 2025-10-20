package dao;

import model.Transaction;
import model.Transaction.TransactionType;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // 1. INSERT TRANSACTION
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, date, narration) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType().name());
            stmt.setDouble(3, transaction.getAmount());

            if (transaction.getDate() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(transaction.getDate()));
            } else {
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setString(5, transaction.getNarration());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    transaction.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. GET TRANSACTION BY ID
    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        Transaction transaction = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                transaction = mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    // 3. GET ALL TRANSACTIONS
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // 4. GET ALL TRANSACTIONS FOR AN ACCOUNT
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // 5. UPDATE TRANSACTION
    public boolean updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET account_id = ?, transaction_type = ?, amount = ?, date = ?, narration = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType().name());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getNarration());
            stmt.setInt(6, transaction.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. DELETE TRANSACTION
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Map ResultSet to Transaction Model
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setAmount(rs.getDouble("amount"));

        Timestamp timestamp = rs.getTimestamp("date");
        if (timestamp != null) {
            transaction.setDate(timestamp.toLocalDateTime());
        }

        transaction.setNarration(rs.getString("narration"));

        return transaction;
    }
}
