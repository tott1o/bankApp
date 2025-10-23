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

    // --- Color Scheme (Consistent with the requested dark/flat UI) ---
// Recommended Refined Deep Ocean Palette:
    private static final Color BG_DARK = new Color(33, 47, 60);    // #212F3C
    static final Color BG_SECONDARY = new Color(44, 62, 80); // #2C3E50
    private static final Color ACCENT_COLOR = new Color(23, 165, 137); // #17A589 (Emerald Green)
    private static final Color TEXT_LIGHT = new Color(234, 236, 238); // #EAECEE
    private static final Color BORDER_COLOR = new Color(73, 95, 116); // #495F74

    // FIX: Revert to the no-argument constructor for easy startup from main()
    public BankingAppUI() {
        super("Modern Dark Theme Banking Management System");

        // --- DAO Initialization ---
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        LoanDAO loanDAO = new LoanDAO();
        LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();

        // FIX: Initialize ALL Services here, once.
        this.customerService = new CustomerService(customerDAO);
        this.transactionService = new TransactionService(transactionDAO);
        // AccountService requires both AccountDAO and TransactionDAO
        this.accountService = new AccountService(accountDAO, transactionDAO);
        this.loanService = new LoanService(loanDAO);
        // LoanPaymentService requires LoanPaymentDAO and LoanDAO
        this.loanPaymentService = new LoanPaymentService(loanPaymentDAO, loanDAO);

        setupLookAndFeel();
        initializeUI();
    }


    private void setupLookAndFeel() {
        // ... (LookAndFeel setup code remains the same) ...
        try {
            // Use Nimbus as a base for custom theming
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to default
        }

        // Custom UIManager settings for dark theme
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

        // FIX: Correct Placeholder calls to pass the foreground color (TEXT_LIGHT)
        tabbedPane.addTab(" Customer", new CustomerPanel(customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab(" Account", new AccountPanel(accountService, customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab(" Transaction", new TransactionPanel(transactionService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab(" Loan", new LoanPanel(loanService, customerService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));
        tabbedPane.addTab(" Loan Payment", new LoanPaymentPanel(loanPaymentService, loanService, BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR));

        getContentPane().add(tabbedPane);
        getContentPane().setBackground(BG_DARK);
        setVisible(true);
    }

    // FIX: Correct Placeholder method signature to accept foreground color
    private JPanel createPlaceholderPanel(String text, Color bg, Color fg) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        JLabel label = new JLabel(text + " - Coming Soon!");
        label.setForeground(fg); // Use passed fg color
        label.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        // FIX: BankingAppUI::new now correctly refers to the no-argument constructor.
        SwingUtilities.invokeLater(BankingAppUI::new);
    }
}