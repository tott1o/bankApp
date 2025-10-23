package service;

import dao.CustomerDAO;
import model.Customer;
import java.util.List;

public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Adds a new customer after validation.
     */
    public boolean createCustomer(Customer customer) {
        // Basic business validation example
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            System.err.println("Customer full name cannot be empty.");
            return false;
        }
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            System.err.println("Invalid email address.");
            return false;
        }

        return customerDAO.addCustomer(customer);
    }

    /**
     * Retrieves a customer by ID.
     */
    public Customer getCustomer(int id) {
        return customerDAO.getCustomerById(id);
    }

    /**
     * Retrieves all customers.
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Updates an existing customer.
     */
    public boolean updateCustomer(Customer customer) {
        if (customer.getId() <= 0) {
            System.err.println("Cannot update customer: ID is missing.");
            return false;
        }
        return customerDAO.updateCustomer(customer);
    }

    /**
     * Deletes a customer.
     * Note: In a real banking system, this would require complex checks
     * for active accounts/loans and often involves soft deletion.
     */
    public boolean deleteCustomer(int id) {
        // Here you would check if the customer has any active accounts or loans
        // If they do, you might prevent deletion or require account/loan closure first.
        return customerDAO.deleteCustomer(id);
    }
}