package ui;

import model.Account;
import model.Customer;
import service.AccountService;
import service.CustomerService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static ui.BankingAppUI.BG_SECONDARY; // Assuming BG_SECONDARY is available

public class AccountPanel extends JPanel {
    private final AccountService accountService;
    private final CustomerService customerService;
    private final Color BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR;

    // Components
    private JTable accountTable;
    private DefaultTableModel tableModel;
    // All fields used in the form
    private JTextField txtId, txtCustomerId, txtBalance, txtDepositAmount, txtWithdrawAmount;
    private JComboBox<String> cmbAccountType;

    // New/Refactored Buttons for the Toolbar and Sidebar
    private JButton btnAddMain, btnDeleteMain, btnUpdateForm, btnClear, btnRefresh;

    // A slightly brighter color for the sidebar contrast
    private final Color FORM_BG_COLOR;

    public AccountPanel(AccountService accountService, CustomerService customerService, Color bgDark, Color accent, Color text, Color border) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;
        this.FORM_BG_COLOR = bgDark.brighter();

        setLayout(new BorderLayout(0, 0)); // Use BorderLayout for the new structure
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Initialize and arrange the new structure
        JPanel formSidebar = createFormSidebar();
        JPanel mainContentPanel = createMainContentPanel(); // Table and controls

        add(formSidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        loadAccountData();
    }

    // --- UI Component Creation - Sidebar Form (Details & Transactions) ---

    private JPanel createFormSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FORM_BG_COLOR);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(new LineBorder(BORDER_COLOR, 1, false));

        // Sidebar Title/Header
        JLabel title = new JLabel("Account Details & Operations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ACCENT_COLOR);
        title.setBorder(new EmptyBorder(15, 20, 15, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        txtId = createTextField(false);
        txtCustomerId = createTextField(true);
        txtBalance = createTextField(false); // Balance is read-only
        txtDepositAmount = createTextField(true);
        txtWithdrawAmount = createTextField(true);
        cmbAccountType = createComboBox(Account.AccountType.values());

        // --- Account Fields Group ---
        JPanel accountFields = new JPanel(new GridBagLayout());
        accountFields.setBackground(FORM_BG_COLOR);
        accountFields.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;

        accountFields.add(createInputRow("ID (Auto):", txtId), gbc);
        accountFields.add(createInputRow("Customer ID:", txtCustomerId), gbc);
        accountFields.add(createInputRow("Account Type:", cmbAccountType), gbc);
        accountFields.add(createInputRow("Current Balance:", txtBalance), gbc);

        // --- Update Button for form fields (Account Type only) ---
        btnUpdateForm = createStyledButton("📝 Apply Changes", this::handleUpdateAction, BG_SECONDARY.brighter());
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updatePanel.setBackground(FORM_BG_COLOR);
        updatePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        updatePanel.add(btnUpdateForm);

        // --- Quick Transaction Group ---
        JPanel transactionGroup = new JPanel();
        transactionGroup.setLayout(new BoxLayout(transactionGroup, BoxLayout.Y_AXIS));
        transactionGroup.setBackground(FORM_BG_COLOR);
        transactionGroup.setBorder(createModernTitledBorder("Quick Transactions"));

        // Deposit Row
        JPanel depositRow = createTransactionRow("Deposit:", txtDepositAmount, "💸 Deposit", this::handleDepositAction, ACCENT_COLOR);

        // Withdraw Row
        JPanel withdrawRow = createTransactionRow("Withdraw:", txtWithdrawAmount, "💳 Withdraw", this::handleWithdrawAction, new Color(231, 76, 60));

        transactionGroup.add(depositRow);
        transactionGroup.add(withdrawRow);
        transactionGroup.add(Box.createVerticalStrut(10));

        // Add components to main sidebar panel
        panel.add(accountFields);
        panel.add(updatePanel); // Add the update button after the main fields
        panel.add(Box.createVerticalStrut(10));
        panel.add(transactionGroup);
        panel.add(Box.createVerticalGlue());
        panel.add(createFormButtonPanel()); // Add clear button at the bottom
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JPanel createTransactionRow(String labelText, JTextField textField, String buttonText, Consumer<ActionEvent> action, Color buttonColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        row.setBackground(FORM_BG_COLOR);

        JLabel label = createInputLabel(labelText);
        label.setPreferredSize(new Dimension(80, 25)); // Adjusted width for transaction labels
        row.add(label);

        textField.setPreferredSize(new Dimension(100, 25));
        row.add(textField);

        JButton button = createStyledButton(buttonText, action, buttonColor);
        button.setForeground(TEXT_LIGHT); // Ensure text is light for color buttons
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        row.add(button);

        return row;
    }

    private Border createModernTitledBorder(String title) {
        Border line = new LineBorder(BORDER_COLOR, 1, true);
        Border empty = new EmptyBorder(0, 15, 0, 15);
        Border compound = BorderFactory.createCompoundBorder(line, empty);

        return BorderFactory.createTitledBorder(
                compound, " " + title + " ",
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), ACCENT_COLOR);
    }

    private JPanel createInputRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(FORM_BG_COLOR);
        row.add(createInputLabel(labelText), BorderLayout.WEST);

        if (component instanceof JTextField) {
            ((JTextField) component).setPreferredSize(new Dimension(200, 25));
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setPreferredSize(new Dimension(200, 25));
        }

        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(110, 25));
        return label;
    }

    private JTextField createTextField(boolean editable) {
        JTextField tf = new JTextField(15);
        tf.setEditable(editable);
        tf.setBackground(BG_DARK);
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(ACCENT_COLOR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private JComboBox<String> createComboBox(Account.AccountType[] items) {
        String[] itemNames = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            itemNames[i] = items[i].toString();
        }
        JComboBox<String> cmb = new JComboBox<>(itemNames);
        cmb.setBackground(BG_DARK);
        cmb.setForeground(TEXT_LIGHT);
        cmb.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        return cmb;
    }

    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(FORM_BG_COLOR);
        btnClear = createStyledButton("Clear Form", this::handleClearAction, ACCENT_COLOR.darker());
        btnClear.setForeground(TEXT_LIGHT); // Set text light for consistency
        panel.add(btnClear);
        return panel;
    }

    // --- UI Component Creation - Main Content (Table + Toolbar) ---

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header/Toolbar Panel (Top)
        JPanel toolbarPanel = createToolbarPanel();
        panel.add(toolbarPanel, BorderLayout.NORTH);

        // 2. Table Panel (Center)
        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Left Side: Title
        JLabel titleLabel = new JLabel("Account List (Core Banking)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_LIGHT);
        panel.add(titleLabel, BorderLayout.WEST);

        // Right Side: Action Buttons (Main actions that apply to the data set)
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(BG_DARK);

        btnAddMain = createStyledButton("➕ Open New Account", this::handleAddAction, ACCENT_COLOR);
        btnDeleteMain = createStyledButton("❌ Close Account", this::handleDeleteAction, Color.RED.darker());
        btnRefresh = createStyledButton("⟳ Refresh", this::handleRefreshAction, BG_SECONDARY);

        buttonGroup.add(btnRefresh);
        buttonGroup.add(btnDeleteMain);
        buttonGroup.add(btnAddMain);

        panel.add(buttonGroup, BorderLayout.EAST);
        return panel;
    }

    // Overloaded to accept Consumer<ActionEvent> and Color
    private JButton createStyledButton(String text, Consumer<ActionEvent> action, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.addActionListener(action::accept);

        // Hover effect
        Color hoverBg = bgColor.brighter();
        button.addChangeListener(e -> {
            if (button.getModel().isRollover()) {
                button.setBackground(hoverBg);
            } else {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private JPanel createTablePanel() {
        String[] columnNames = {"ID", "Customer ID", "Type", "Open Date", "Balance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountTable = new JTable(tableModel);
        styleTable(accountTable);

        accountTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && accountTable.getSelectedRow() != -1) {
                displaySelectedAccount(accountTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.getViewport().setBackground(BG_DARK.darker());
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setBackground(BG_DARK.darker());
        table.setForeground(TEXT_LIGHT);
        table.setSelectionBackground(ACCENT_COLOR.darker().darker());
        table.setSelectionForeground(TEXT_LIGHT);
        table.setGridColor(BORDER_COLOR.darker());
        table.setRowHeight(35);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        table.getTableHeader().setBackground(BG_SECONDARY.darker());
        table.getTableHeader().setForeground(ACCENT_COLOR.brighter());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));

        // Striped rows and cell renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_DARK.darker() : BG_DARK.darker().darker());
                }
                // Right align balance column
                setHorizontalAlignment(column == 4 ? JLabel.RIGHT : JLabel.LEFT);
                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    // --- Data and Action Logic ---

    private void loadAccountData() {
        tableModel.setRowCount(0);
        List<Account> accounts = accountService.getAllAccounts();
        for (Account account : accounts) {
            tableModel.addRow(new Object[]{
                    account.getId(),
                    account.getCustomerId(),
                    account.getAccountType(),
                    account.getOpenDate(),
                    String.format("$%,.2f", account.getBalance()) // Format balance
            });
        }
    }

    private void displaySelectedAccount(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtCustomerId.setText(tableModel.getValueAt(row, 1).toString());
        cmbAccountType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        // Remove currency symbols for editing/display purposes
        String balanceStr = tableModel.getValueAt(row, 4).toString().replace("$", "").replace(",", "");
        txtBalance.setText(balanceStr);
        txtDepositAmount.setText("");
        txtWithdrawAmount.setText("");
    }

    private void handleAddAction(ActionEvent e) {
        try {
            int customerId = Integer.parseInt(txtCustomerId.getText());
            double initialBalance = Double.parseDouble(txtBalance.getText().isEmpty() ? "0" : txtBalance.getText());

            Customer customer = customerService.getCustomer(customerId);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer ID not found. Cannot open account.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account newAccount = new Account();
            newAccount.setCustomerId(customerId);
            newAccount.setAccountType(Account.AccountType.valueOf(cmbAccountType.getSelectedItem().toString()));
            newAccount.setOpenDate(LocalDate.now());
            newAccount.setBalance(initialBalance);

            if (accountService.createAccount(newAccount)) {
                JOptionPane.showMessageDialog(this, "Account opened successfully! ID: " + newAccount.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to open account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Customer ID or Initial Balance.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an account to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int accountId = Integer.parseInt(txtId.getText());
            Account existingAccount = accountService.getAccount(accountId);

            if (existingAccount == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Only update the Account Type from the form
            existingAccount.setAccountType(Account.AccountType.valueOf(cmbAccountType.getSelectedItem().toString()));

            if (accountService.updateAccount(existingAccount)) {
                JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an account to close.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int accountId = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to close Account ID: " + accountId + "? (Balance must be $0.00)", "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (accountService.deleteAccount(accountId)) {
                JOptionPane.showMessageDialog(this, "Account closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to close account. Balance must be zero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDepositAction(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select an account first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int accountId = Integer.parseInt(txtId.getText());

            String amountText = txtDepositAmount.getText();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a deposit amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Deposit amount must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accountService.deposit(accountId, amount)) {
                JOptionPane.showMessageDialog(this, String.format("Deposit of $%,.2f successful to Account ID %d.", amount, accountId), "Success", JOptionPane.INFORMATION_MESSAGE);
                txtDepositAmount.setText("");
                loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed. Check account ID and connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Deposit Amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdrawAction(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select an account first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int accountId = Integer.parseInt(txtId.getText());

            String amountText = txtWithdrawAmount.getText();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a withdrawal amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Withdrawal amount must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accountService.withdraw(accountId, amount)) {
                JOptionPane.showMessageDialog(this, String.format("Withdrawal of $%,.2f successful from Account ID %d.", amount, accountId), "Success", JOptionPane.INFORMATION_MESSAGE);
                txtWithdrawAmount.setText("");
                loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient funds or invalid account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Withdrawal Amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleClearAction(ActionEvent e) {
        txtId.setText("");
        txtCustomerId.setText("");
        txtBalance.setText("");
        txtDepositAmount.setText("");
        txtWithdrawAmount.setText("");
        accountTable.clearSelection();
    }

    private void handleRefreshAction(ActionEvent e) {
        loadAccountData();
    }
}