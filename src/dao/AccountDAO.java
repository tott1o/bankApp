package dao;

import model.Account;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // CREATE ACCOUNT
    public boolean addAccount(Account account) {
        String sql = "INSERT INTO accounts (customer_id, account_type, open_date, close_date, balance, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account.getCustomerId());
            stmt.setString(2, account.getAccountType().name());
            stmt.setDate(3, Date.valueOf(account.getOpenDate()));
            if (account.getCloseDate() != null) {
                stmt.setDate(4, Date.valueOf(account.getCloseDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setDouble(5, account.getBalance());
            stmt.setDouble(6, account.getInterestRate());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ACCOUNT BY ID
    public Account getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
                account.setOpenDate(rs.getDate("open_date").toLocalDate());
                Date closeDate = rs.getDate("close_date");
                if (closeDate != null) account.setCloseDate(closeDate.toLocalDate());
                account.setBalance(rs.getDouble("balance"));
                account.setInterestRate(rs.getDouble("interest_rate"));
                return account;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL ACCOUNTS
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(Account.AccountType.valueOf(rs.getString("account_type")));
                account.setOpenDate(rs.getDate("open_date").toLocalDate());
                Date closeDate = rs.getDate("close_date");
                if (closeDate != null) account.setCloseDate(closeDate.toLocalDate());
                account.setBalance(rs.getDouble("balance"));
                account.setInterestRate(rs.getDouble("interest_rate"));
                accounts.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // UPDATE ACCOUNT
    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts SET customer_id=?, account_type=?, open_date=?, close_date=?, balance=?, interest_rate=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account.getCustomerId());
            stmt.setString(2, account.getAccountType().name());
            stmt.setDate(3, Date.valueOf(account.getOpenDate()));
            if (account.getCloseDate() != null) {
                stmt.setDate(4, Date.valueOf(account.getCloseDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setDouble(5, account.getBalance());
            stmt.setDouble(6, account.getInterestRate());
            stmt.setInt(7, account.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE ACCOUNT
    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
