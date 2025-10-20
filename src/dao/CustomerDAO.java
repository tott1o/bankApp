package dao;

import model.Customer;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE CUSTOMER
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (full_name, address, contact_no, email, pan_number, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getContactNo());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPanNumber());
            stmt.setTimestamp(6, Timestamp.valueOf(customer.getCreatedAt()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ CUSTOMER BY ID
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFullName(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setContactNo(rs.getString("contact_no"));
                customer.setEmail(rs.getString("email"));
                customer.setPanNumber(rs.getString("pan_number"));
                customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL CUSTOMERS
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFullName(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setContactNo(rs.getString("contact_no"));
                customer.setEmail(rs.getString("email"));
                customer.setPanNumber(rs.getString("pan_number"));
                customer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // UPDATE CUSTOMER
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET full_name = ?, address = ?, contact_no = ?, email = ?, pan_number = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getContactNo());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPanNumber());
            stmt.setInt(6, customer.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE CUSTOMER
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
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
