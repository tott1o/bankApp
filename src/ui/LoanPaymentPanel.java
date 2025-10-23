package ui;

import model.Loan;
import model.LoanPayment;
import service.LoanPaymentService;
import service.LoanService;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

// Define colors locally for compilation if not using static import
class LoanPaymentPanel extends JPanel {
    private final LoanPaymentService paymentService;
    private final LoanService loanService;
    private final Color BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR;
    private final Color BG_SECONDARY = new Color(52, 73, 94);
    private final Color FORM_BG_COLOR;

    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JTextField txtLoanId, txtPaymentAmount, txtReceiptNo;
    private JLabel lblCurrentLoanBalance;
    private JButton btnRecordPayment, btnRefresh;

    public LoanPaymentPanel(LoanPaymentService paymentService, LoanService loanService, Color bgDark, Color accent, Color text, Color border) {
        this.paymentService = paymentService;
        this.loanService = loanService;
        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;
        this.FORM_BG_COLOR = bgDark.brighter();

        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Layout: Top Panel (Record Payment) and Bottom Panel (History)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createPaymentFormPanel(), createHistoryPanel());
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.25);
        splitPane.setBorder(null);
        splitPane.setBackground(BG_DARK);

        add(splitPane, BorderLayout.CENTER);
        loadPaymentData(0);
    }

    // --- UI Component Creation ---

    private JPanel createPaymentFormPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(FORM_BG_COLOR);
        main.setBorder(createModernTitledBorder("Record New Loan Payment"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(FORM_BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 20, 10, 20);

        txtLoanId = createTextField(true);
        txtPaymentAmount = createTextField(true);
        txtReceiptNo = createTextField(true);
        lblCurrentLoanBalance = createInfoLabel("N/A");

        // Row 0: Loan ID and Check Button
        gbc.gridx = 0; gbc.gridy = 0; form.add(createInputLabel("Loan ID:"), gbc);
        gbc.gridx = 1; form.add(txtLoanId, gbc);
        JButton btnCheckLoan = createStyledButton("Check Balance", this::handleCheckLoanAction, BG_SECONDARY.brighter());
        btnCheckLoan.setPreferredSize(new Dimension(120, 35));
        gbc.gridx = 2; gbc.weightx = 0; gbc.insets = new Insets(10, 10, 10, 20); form.add(btnCheckLoan, gbc);

        // Row 1: Current Balance Display
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.insets = new Insets(5, 20, 5, 20); form.add(createInputLabel("Current Balance:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; form.add(lblCurrentLoanBalance, gbc);
        gbc.gridwidth = 1; gbc.weightx = 0;

        // Row 2: Payment Amount
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1.0; gbc.insets = new Insets(5, 20, 5, 20); form.add(createInputLabel("Payment Amount:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; form.add(txtPaymentAmount, gbc);
        gbc.gridwidth = 1;

        // Row 3: Receipt No
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1.0; form.add(createInputLabel("Receipt No (Optional):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; form.add(txtReceiptNo, gbc);
        gbc.gridwidth = 1;

        // Row 4: Button
        btnRecordPayment = createStyledButton(" Record Payment", this::handleRecordPaymentAction, ACCENT_COLOR);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(FORM_BG_COLOR);
        btnPanel.add(btnRecordPayment);

        main.add(form, BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);
        return main;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(createModernTitledBorder("Payment History"));

        String[] columnNames = {"ID", "Loan ID", "Amount", "Remaining Balance", "Payment Date", "Receipt No"};
        tableModel = new DefaultTableModel(columnNames, 0);

        paymentTable = new JTable(tableModel);
        styleTable(paymentTable);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.getViewport().setBackground(BG_DARK.darker());
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        panel.add(scrollPane, BorderLayout.CENTER);

        btnRefresh = createStyledButton("⟳ Refresh All Payments", this::handleRefreshAction, BG_SECONDARY.darker());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BG_DARK);
        footer.setBorder(new EmptyBorder(10,0,0,0));
        footer.add(btnRefresh);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private Border createModernTitledBorder(String title) {
        Border line = new LineBorder(BORDER_COLOR, 1, true);
        Border empty = new EmptyBorder(15, 15, 15, 15);
        Border compound = BorderFactory.createCompoundBorder(line, empty);

        return BorderFactory.createTitledBorder(
                compound, " " + title + " ",
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), TEXT_LIGHT);
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
        tf.setPreferredSize(new Dimension(200, 35));
        return tf;
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(150, 25));
        return label;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ACCENT_COLOR.brighter());
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
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

    private void loadPaymentData(int loanId) {
        tableModel.setRowCount(0);
        List<LoanPayment> payments;

        if (loanId > 0) {
            payments = paymentService.getPaymentsByLoanId(loanId);
        } else {
            payments = paymentService.getAllLoanPayments();
        }

        for (LoanPayment payment : payments) {
            tableModel.addRow(new Object[]{
                    payment.getId(),
                    payment.getLoanId(),
                    String.format("rs.%,.2f", payment.getDisbursementAmount()),
                    String.format("rs.%,.2f", payment.getRemainingBalance()),
                    payment.getPaymentDate(),
                    payment.getReceiptNo()
            });
        }
    }

    private void handleCheckLoanAction(ActionEvent e) {
        try {
            int loanId = Integer.parseInt(txtLoanId.getText());
            Loan loan = loanService.getLoan(loanId);

            if (loan != null) {
                lblCurrentLoanBalance.setText(String.format("rs.%,.2f", loan.getBalance()));
                loadPaymentData(loanId); // Load payment history for this loan
            } else {
                lblCurrentLoanBalance.setText("LOAN NOT FOUND");
                JOptionPane.showMessageDialog(this, "Loan ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            lblCurrentLoanBalance.setText("INVALID INPUT");
            JOptionPane.showMessageDialog(this, "Invalid Loan ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRecordPaymentAction(ActionEvent e) {
        try {
            int loanId = Integer.parseInt(txtLoanId.getText());
            double amount = Double.parseDouble(txtPaymentAmount.getText());
            String receipt = txtReceiptNo.getText();

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Payment amount must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoanPayment payment = new LoanPayment();
            payment.setLoanId(loanId);
            payment.setDisbursementAmount(amount);
            payment.setReceiptNo(receipt.isEmpty() ? null : receipt);
            payment.setPaymentDate(LocalDateTime.now());

            if (paymentService.recordPayment(payment)) {
                JOptionPane.showMessageDialog(this, "Payment recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleCheckLoanAction(null); // Refresh balance and payment history
                txtPaymentAmount.setText("");
                txtReceiptNo.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed. Check loan ID or if payment exceeds balance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Loan ID or Amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRefreshAction(ActionEvent e) {
        loadPaymentData(0);
        lblCurrentLoanBalance.setText("N/A");
        txtLoanId.setText("");
    }
}