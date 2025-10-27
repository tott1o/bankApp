package ui;

import dao.*;
import service.*;

import javax.swing.*;
import java.awt.*;

public class BankingAppUI extends JFrame {

    // --- Dependency Injection and Services Initialization ---
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final LoanService loanService;
    private final LoanPaymentService loanPaymentService;

    // --- Color Scheme (Declared static final for easy access) ---
    private static final Color BG_DARK = new Color(44, 62, 80);
    static final Color BG_SECONDARY = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(48, 154, 154);
    private static final Color TEXT_LIGHT = new Color(236, 240, 241);
    private static final Color BORDER_COLOR = new Color(74, 98, 120);

    public BankingAppUI() {
        super("AANT-bank");

        // --- DAO & Service Initialization ---
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        LoanDAO loanDAO = new LoanDAO();
        LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();

        // Initialize Services
        this.customerService = new CustomerService(customerDAO);
        this.transactionService = new TransactionService(transactionDAO);
        this.accountService = new AccountService(accountDAO, transactionDAO);
        this.loanService = new LoanService(loanDAO);
        this.loanPaymentService = new LoanPaymentService(loanPaymentDAO, loanDAO);

        setupLookAndFeel();
        initializeUI();
    }


    private void setupLookAndFeel() {
        // ... (LookAndFeel setup code remains the same) ...
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to default
        }

        UIManager.put("control", BG_SECONDARY);
        UIManager.put("info", BG_SECONDARY);
        UIManager.put("nimbusBase", BG_DARK.darker());
        UIManager.put("nimbusBlueGrey", BG_SECONDARY.brighter());
        UIManager.put("text", TEXT_LIGHT);
        UIManager.put("Label.foreground", TEXT_LIGHT);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", BG_DARK);
        UIManager.put("TextField.background", BG_DARK.brighter());
        UIManager.put("TextField.foreground", TEXT_LIGHT);
        UIManager.put("TextArea.background", BG_DARK.brighter());
        UIManager.put("TextArea.foreground", TEXT_LIGHT);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("TabbedPane.contentAreaColor", BG_DARK);
        UIManager.put("TabbedPane.selected", BG_SECONDARY);
        UIManager.put("TabbedPane.background", BG_SECONDARY.darker());
        UIManager.put("TabbedPane.foreground", TEXT_LIGHT);
        UIManager.put("TitledBorder.titleColor", ACCENT_COLOR);
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 900);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_SECONDARY);
        tabbedPane.setForeground(TEXT_LIGHT);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.addTab(" Customer", new CustomerPanel(customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab(" Account", new AccountPanel(accountService, customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab("Transaction", new TransactionPanel(transactionService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab("Loan", new LoanPanel(loanService, customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab("Loan Payment", new LoanPaymentPanel(loanPaymentService, loanService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));

        getContentPane().add(tabbedPane);
        getContentPane().setBackground(BG_DARK);
        // setVisible(true) is now called conditionally in the main method
    }

    private JPanel createPlaceholderPanel(String text, Color bg, Color fg) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        JLabel label = new JLabel(text + " - Coming Soon!");
        label.setForeground(fg);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create a temporary frame to center the login dialog
            JFrame tempFrame = new JFrame();
            tempFrame.setUndecorated(true); // Hide the temp frame visually
            tempFrame.setSize(0, 0);
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);

            // 2. Show Login Dialog
            LoginDialog loginDialog = new LoginDialog(tempFrame, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR);
            loginDialog.setVisible(true);

            // 3. Check Authentication Result
            if (loginDialog.isAuthenticated()) {
                // Authentication successful: Launch main application
                BankingAppUI app = new BankingAppUI();
                app.setVisible(true);
            }

            // Dispose of the temporary frame regardless of outcome
            tempFrame.dispose();
        });
    }
}