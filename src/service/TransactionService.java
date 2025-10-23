package service;

import dao.TransactionDAO;
import model.Transaction;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    /**
     * Retrieves all transactions in the system.
     */
    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    /**
     * Retrieves transactions specific to a single account ID.
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    // Note: Add, Update, and Delete for transactions are typically handled internally
    // by the AccountService (deposit/withdraw) and are not exposed directly here.
}