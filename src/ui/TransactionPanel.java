package ui;

import model.Transaction;
import service.TransactionService;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

// Define colors locally (or use static imports from BankingAppUI)
class TransactionPanel extends JPanel {
    private final TransactionService transactionService;
    private final Color BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR;
    private final Color BG_SECONDARY = new Color(52, 73, 94);

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField txtAccountIdFilter;

    public TransactionPanel(TransactionService transactionService, Color bgDark, Color accent, Color text, Color border) {
        this.transactionService = transactionService;
        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;

        setLayout(new BorderLayout(0, 0));
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Layout: Toolbar (North) and Table (Center)
        add(createToolbarPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadTransactionData(0); // Load all transactions initially (0 means no filter)
    }

    // --- UI Component Creation ---

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Left Side: Title
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_LIGHT);
        panel.add(titleLabel, BorderLayout.WEST);

        // Right Side: Filter and Refresh
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(BG_DARK);

        txtAccountIdFilter = createTextField(true, 10);
        txtAccountIdFilter.setPreferredSize(new Dimension(100, 35));

        JLabel filterLabel = new JLabel("Filter by Account ID:");
        filterLabel.setForeground(TEXT_LIGHT);
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnFilter = createStyledButton(" Filter", this::handleFilterAction, BG_SECONDARY);
        JButton btnRefresh = createStyledButton(" All", this::handleRefreshAction, BG_SECONDARY);

        buttonGroup.add(filterLabel);
        buttonGroup.add(txtAccountIdFilter);
        buttonGroup.add(btnFilter);
        buttonGroup.add(btnRefresh);

        panel.add(buttonGroup, BorderLayout.EAST);
        return panel;
    }

    private JTextField createTextField(boolean editable, int columns) {
        JTextField tf = new JTextField(columns);
        tf.setEditable(editable);
        tf.setBackground(BG_DARK.darker());
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(ACCENT_COLOR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
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
        String[] columnNames = {"ID", "Account ID", "Type", "Amount", "Date", "Narration"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        styleTable(transactionTable);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
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

        // Custom Cell Renderer for Amount coloring (Green for DEPOSIT, Red for WITHDRAWAL)
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String type = table.getValueAt(row, 2).toString();

                if (column == 3) { // Amount column
                    if ("DEPOSIT".equals(type)) {
                        c.setForeground(new Color(46, 204, 113)); // Bright Green
                    } else if ("WITHDRAWAL".equals(type)) {
                        c.setForeground(new Color(231, 76, 60)); // Red
                    } else {
                        c.setForeground(TEXT_LIGHT);
                    }
                    setHorizontalAlignment(JLabel.RIGHT);
                    // Value is already a Double from loadTransactionData, so formatting is safe.
                    setValue(String.format("rs.%,.2f", (Double) value));
                } else {
                    c.setForeground(TEXT_LIGHT);
                    setHorizontalAlignment(JLabel.LEFT);
                }

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_DARK.darker() : BG_DARK.darker().darker());
                } else {
                    c.setBackground(ACCENT_COLOR.darker().darker());
                }

                setBorder(new EmptyBorder(0, 15, 0, 15));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.getTableHeader().setBackground(BG_SECONDARY.darker());
        table.getTableHeader().setForeground(ACCENT_COLOR.brighter());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
    }

    // --- Data and Action Logic ---

    private void loadTransactionData(int accountId) {
        tableModel.setRowCount(0);
        List<Transaction> transactions;

        if (accountId > 0) {
            transactions = transactionService.getTransactionsByAccountId(accountId);
        } else {
            transactions = transactionService.getAllTransactions();
        }

        for (Transaction transaction : transactions) {
            tableModel.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getAccountId(),
                    transaction.getTransactionType().name(),
                    transaction.getAmount(), // Pass as Double for the custom renderer to format
                    transaction.getDate(),
                    transaction.getNarration()
            });
        }
    }

    private void handleFilterAction(ActionEvent e) {
        try {
            String filterText = txtAccountIdFilter.getText().trim();
            if (filterText.isEmpty()) {
                loadTransactionData(0);
                return;
            }
            int accountId = Integer.parseInt(filterText);
            loadTransactionData(accountId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Account ID format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRefreshAction(ActionEvent e) {
        txtAccountIdFilter.setText("");
        loadTransactionData(0);
    }
}