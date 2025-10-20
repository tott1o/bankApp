import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class BankingApp extends JFrame {
    private ArrayList<Account> accounts;
    private Account currentAccount;
    private JTextArea displayArea;
    private JTextField amountField;
    private JLabel balanceLabel;
    private NumberFormat currencyFormat;

    public BankingApp() {
        accounts = new ArrayList<>();
        currencyFormat = NumberFormat.getCurrencyInstance();

        // Create default account
        currentAccount = new Account("John Doe", "1001", 1000.00);
        accounts.add(currentAccount);
        accounts.add(new Account("Jane Smith", "1002", 1500.00));

        setupUI();
    }

    private void setupUI() {
        setTitle("Banking Application");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Account Info
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));

        topPanel.add(new JLabel("Account Holder:"));
        JLabel nameLabel = new JLabel(currentAccount.getName());
        topPanel.add(nameLabel);

        topPanel.add(new JLabel("Account Number:"));
        JLabel accountNumLabel = new JLabel(currentAccount.getAccountNumber());
        topPanel.add(accountNumLabel);

        topPanel.add(new JLabel("Current Balance:"));
        balanceLabel = new JLabel(currencyFormat.format(currentAccount.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(0, 128, 0));
        topPanel.add(balanceLabel);

        // Center Panel - Transaction Area
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Transaction Details"));

        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Amount: $"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);
        centerPanel.add(inputPanel, BorderLayout.SOUTH);

        // Bottom Panel - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBackground(new Color(34, 139, 34));
        depositBtn.setForeground(Color.WHITE);
        depositBtn.addActionListener(e -> deposit());

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBackground(new Color(220, 20, 60));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.addActionListener(e -> withdraw());

        JButton balanceBtn = new JButton("Check Balance");
        balanceBtn.addActionListener(e -> checkBalance());

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBackground(new Color(30, 144, 255));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.addActionListener(e -> transfer());

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearDisplay());

        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(balanceBtn);
        buttonPanel.add(clearBtn);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial message
        displayArea.append("Welcome to Banking Application!\n");
        displayArea.append("================================\n\n");

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentAccount.deposit(amount);
            updateBalance();
            displayArea.append("DEPOSIT: " + currencyFormat.format(amount) + "\n");
            displayArea.append("New Balance: " + currencyFormat.format(currentAccount.getBalance()) + "\n\n");
            amountField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (currentAccount.withdraw(amount)) {
                updateBalance();
                displayArea.append("WITHDRAWAL: " + currencyFormat.format(amount) + "\n");
                displayArea.append("New Balance: " + currencyFormat.format(currentAccount.getBalance()) + "\n\n");
                amountField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transfer() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] accountNames = accounts.stream()
                    .filter(acc -> !acc.equals(currentAccount))
                    .map(acc -> acc.getName() + " (" + acc.getAccountNumber() + ")")
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(this, "Select account to transfer to:",
                    "Transfer", JOptionPane.QUESTION_MESSAGE, null, accountNames, accountNames[0]);

            if (selected != null) {
                Account toAccount = accounts.stream()
                        .filter(acc -> selected.contains(acc.getAccountNumber()))
                        .findFirst().orElse(null);

                if (toAccount != null && currentAccount.withdraw(amount)) {
                    toAccount.deposit(amount);
                    updateBalance();
                    displayArea.append("TRANSFER: " + currencyFormat.format(amount) +
                            " to " + toAccount.getName() + "\n");
                    displayArea.append("New Balance: " + currencyFormat.format(currentAccount.getBalance()) + "\n\n");
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkBalance() {
        displayArea.append("BALANCE INQUIRY\n");
        displayArea.append("Account: " + currentAccount.getName() + "\n");
        displayArea.append("Balance: " + currencyFormat.format(currentAccount.getBalance()) + "\n\n");
    }

    private void clearDisplay() {
        displayArea.setText("");
    }

    private void updateBalance() {
        balanceLabel.setText(currencyFormat.format(currentAccount.getBalance()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingApp());
    }
}

class Account {
    private String name;
    private String accountNumber;
    private double balance;

    public Account(String name, String accountNumber, double initialBalance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}