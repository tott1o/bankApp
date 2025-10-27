package ui;

import model.Loan;
import model.Loan.LoanType;
import service.LoanService;
import service.CustomerService; // Needed for Customer validation
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

// Define colors locally for compilation if not using static import
class LoanPanel extends JPanel {
    private final LoanService loanService;
    private final CustomerService customerService;
    private final Color BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR;
    private final Color BG_SECONDARY = new Color(52, 73, 94);
    private final Color FORM_BG_COLOR;

    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtCustomerId, txtAmount, txtBalance, txtInterestRate;
    private JComboBox<String> cmbLoanType;
    private JButton btnApplyInterestSingle, btnApplyInterestAll;
    private JButton btnAddMain, btnDeleteMain, btnRefresh;

    public LoanPanel(LoanService loanService, CustomerService customerService, Color bgDark, Color accent, Color text, Color border) {
        this.loanService = loanService;
        this.customerService = customerService;
        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;
        this.FORM_BG_COLOR = bgDark.brighter();

        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        add(createFormSidebar(), BorderLayout.WEST);
        add(createMainContentPanel(), BorderLayout.CENTER);

        loadLoanData();
    }

    // --- UI Component Creation --- (Using helper methods from AccountPanel structure)

    private JPanel createFormSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FORM_BG_COLOR);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(new LineBorder(BORDER_COLOR, 1, false));

        JLabel title = new JLabel("Loan Application & Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ACCENT_COLOR);
        title.setBorder(new EmptyBorder(15, 20, 15, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        txtId = createTextField(false);
        txtCustomerId = createTextField(true);
        txtAmount = createTextField(true); // Sanctioned Amount
        txtBalance = createTextField(false); // Read-only current balance
        txtInterestRate = createTextField(true);
        cmbLoanType = createComboBox(LoanType.values());

        JPanel loanFields = new JPanel(new GridBagLayout());
        loanFields.setBackground(FORM_BG_COLOR);
        loanFields.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;

        loanFields.add(createInputRow("Loan ID (Auto):", txtId), gbc);
        loanFields.add(createInputRow("Customer ID:", txtCustomerId), gbc);
        loanFields.add(createInputRow("Loan Type:", cmbLoanType), gbc);
        loanFields.add(createInputRow("Sanctioned Amount:", txtAmount), gbc);
        loanFields.add(createInputRow("Interest Rate (%):", txtInterestRate), gbc);
        loanFields.add(createInputRow("Outstanding Balance:", txtBalance), gbc);

        // Add Apply Interest button for single loan
        btnApplyInterestSingle = createStyledButton("Apply Monthly Interest", this::handleApplyInterestAction, new Color(34, 139, 34));
        JPanel interestButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interestButtonPanel.setBackground(FORM_BG_COLOR);
        interestButtonPanel.add(btnApplyInterestSingle);
        gbc.insets = new Insets(10, 0, 5, 0);
        loanFields.add(interestButtonPanel, gbc);

        panel.add(loanFields);
        panel.add(Box.createVerticalGlue());
        panel.add(createFormButtonPanel());
        panel.add(Box.createVerticalStrut(10));

        return panel;
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

    private JComboBox<String> createComboBox(Object[] items) {
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

    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(FORM_BG_COLOR);
        JButton btnClear = createStyledButton("Clear Form", this::handleClearAction, ACCENT_COLOR.darker());
        panel.add(btnClear);
        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel toolbarPanel = createToolbarPanel();
        panel.add(toolbarPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Loan List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_LIGHT);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(BG_DARK);
        left.add(titleLabel, BorderLayout.WEST);

        panel.setLayout(new BorderLayout());
        panel.add(left, BorderLayout.WEST);

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(BG_DARK);

        btnAddMain = createStyledButton(" Sanction New Loan", this::handleAddAction, ACCENT_COLOR);
        btnDeleteMain = createStyledButton(" Close Paid Loan", this::handleDeleteAction, Color.RED.darker());
        btnRefresh = createStyledButton(" Refresh", this::handleRefreshAction, BG_SECONDARY);
        btnApplyInterestAll = createStyledButton(" Apply Interest (All)", this::handleApplyInterestAllAction, new Color(34, 139, 34));

        buttonGroup.add(btnRefresh);
        buttonGroup.add(btnApplyInterestAll);
        buttonGroup.add(btnDeleteMain);
        buttonGroup.add(btnAddMain);

        panel.add(buttonGroup, BorderLayout.EAST);
        return panel;
    }

    private JButton createStyledButton(String text, Consumer<ActionEvent> action, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.addActionListener(action::accept);

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
        String[] columnNames = {"ID", "Customer ID", "Type", "Sanctioned Amt", "Interest %", "Balance", "Open Date", "Close Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loanTable = new JTable(tableModel);
        styleTable(loanTable);

        loanTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && loanTable.getSelectedRow() != -1) {
                displaySelectedLoan(loanTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(loanTable);
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

        table.getTableHeader().setBackground(BG_SECONDARY.darker());
        table.getTableHeader().setForeground(ACCENT_COLOR.brighter());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    // --- Data and Action Logic ---

    private void loadLoanData() {
        tableModel.setRowCount(0);
        List<Loan> loans = loanService.getAllLoans();
        for (Loan loan : loans) {
            String closeDate = loan.getCloseDate() != null ? loan.getCloseDate().toString() : "N/A";
            tableModel.addRow(new Object[]{
                    loan.getId(),
                    loan.getCustomerId(),
                    loan.getLoanType(),
                    String.format("rs.%,.2f", loan.getAmountSanctioned()),
                    String.format("%.2f%%", loan.getInterestRate()),
                    String.format("rs.%,.2f", loan.getBalance()),
                    loan.getOpenDate(),
                    closeDate
            });
        }
    }

    private void displaySelectedLoan(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtCustomerId.setText(tableModel.getValueAt(row, 1).toString());
        cmbLoanType.setSelectedItem(tableModel.getValueAt(row, 2).toString());

        // Remove formatting for internal display
        String amountStr = tableModel.getValueAt(row, 3).toString().replace("rs.", "").replace(",", "");
        String balanceStr = tableModel.getValueAt(row, 5).toString().replace("rs.", "").replace(",", "");
        String interestStr = tableModel.getValueAt(row, 4).toString().replace("%", "");

        txtAmount.setText(amountStr);
        txtBalance.setText(balanceStr);
        txtInterestRate.setText(interestStr);
    }

    private void handleAddAction(ActionEvent e) {
        try {
            int customerId = Integer.parseInt(txtCustomerId.getText());
            double amount = Double.parseDouble(txtAmount.getText());

            if (customerService.getCustomer(customerId) == null) {
                JOptionPane.showMessageDialog(this, "Customer ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Loan newLoan = new Loan();
            newLoan.setCustomerId(customerId);
            newLoan.setLoanType(LoanType.valueOf(cmbLoanType.getSelectedItem().toString()));
            newLoan.setAmountSanctioned(amount);
            double interestRate = Double.parseDouble(txtInterestRate.getText());
            newLoan.setInterestRate(interestRate);
            newLoan.setOpenDate(LocalDate.now());

            if (loanService.createLoan(newLoan)) {
                JOptionPane.showMessageDialog(this, "Loan sanctioned successfully! ID: " + newLoan.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadLoanData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to sanction loan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Customer ID or Amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a loan to close.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to close Loan ID: " + loanId + "? (Requires $0.00 balance)", "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (loanService.deleteLoan(loanId)) {
                JOptionPane.showMessageDialog(this, "Loan closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadLoanData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to close loan. Outstanding balance must be zero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleClearAction(ActionEvent e) {
        txtId.setText("");
        txtCustomerId.setText("");
        txtAmount.setText("");
        txtBalance.setText("");
        loanTable.clearSelection();
    }

    private void handleRefreshAction(ActionEvent e) {
        loadLoanData();
    }

    private void handleApplyInterestAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a loan first", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apply monthly interest to Loan ID: " + loanId + "?",
                "Confirm Interest Application",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (loanService.applyMonthlyInterest(loanId)) {
                JOptionPane.showMessageDialog(this,
                        "Interest applied successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadLoanData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to apply interest. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleApplyInterestAllAction(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apply monthly interest to ALL active loans?",
                "Confirm Bulk Interest Application",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (loanService.applyMonthlyInterestToAllLoans()) {
                JOptionPane.showMessageDialog(this,
                        "Interest applied successfully to all loans!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadLoanData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to apply interest to all loans. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}