package ui;

import auth.AdminAuth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog {

    private final Color BG_DARK;
    private final Color ACCENT_COLOR;
    private final Color TEXT_LIGHT;
    private final Color BORDER_COLOR;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private boolean authenticated = false;

    public LoginDialog(JFrame parent, Color bgDark, Color accent, Color text, Color border) {
        super(parent, "Admin Login", true); // true makes it modal

        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;

        setupUI();
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_DARK.brighter());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Header ---
        JLabel header = new JLabel("Banking System Admin Access", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setForeground(ACCENT_COLOR);
        panel.add(header, BorderLayout.NORTH);

        // --- Form ---
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(BG_DARK.brighter());

        // Username
        formPanel.add(createLabel("Username:"));
        txtUsername = createTextField();
        formPanel.add(txtUsername);

        // Password
        formPanel.add(createLabel("Password:"));
        txtPassword = createPasswordField();
        formPanel.add(txtPassword);

        panel.add(formPanel, BorderLayout.CENTER);

        // --- Button ---
        JButton btnLogin = createStyledButton("LOGIN", this::handleLogin);
        panel.add(btnLogin, BorderLayout.SOUTH);

        add(panel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField(15);
        tf.setBackground(BG_DARK.darker());
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(ACCENT_COLOR);
        tf.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return tf;
    }

    private JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField(15);
        pf.setBackground(BG_DARK.darker());
        pf.setForeground(TEXT_LIGHT);
        pf.setCaretColor(ACCENT_COLOR);
        pf.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return pf;
    }

    private JButton createStyledButton(String text, java.util.function.Consumer<ActionEvent> action) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BG_DARK.darker());
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.addActionListener(action::accept);
        return button;
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        char[] password = txtPassword.getPassword();

        if (AdminAuth.authenticate(username, password)) {
            authenticated = true;
            dispose(); // Close dialog on success
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText(""); // Clear password field
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}