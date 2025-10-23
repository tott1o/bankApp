package ui;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static ui.BankingAppUI.BG_SECONDARY;

public class CustomerPanel extends JPanel {
    private final CustomerService customerService;
    private final Color BG_DARK, ACCENT_COLOR, TEXT_LIGHT, BORDER_COLOR;

    // Components
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtFullName, txtAddress, txtContact, txtEmail, txtPan;

    // New/Refactored Buttons
    private JButton btnAddMain, btnUpdateMain, btnDeleteMain, btnClear, btnRefresh;

    // Form Panel (now a dedicated sidebar)
    private JPanel formSidebar;

    // A slightly darker color for main background contrast in the form area
    private final Color FORM_BG_COLOR;

    public CustomerPanel(CustomerService customerService, Color bgDark, Color accent, Color text, Color border) {
        this.customerService = customerService;
        this.BG_DARK = bgDark;
        this.ACCENT_COLOR = accent;
        this.TEXT_LIGHT = text;
        this.BORDER_COLOR = border;
        this.FORM_BG_COLOR = bgDark.brighter();

        setLayout(new BorderLayout(0, 0)); // No external spacing for a full-panel look
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove external padding

        // Initialize and arrange the new structure
        formSidebar = createFormSidebar();
        JPanel mainContentPanel = createMainContentPanel(); // Table and controls

        // Use BorderLayout for the final arrangement
        add(formSidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        loadCustomerData(); // Load data initially
    }

    // --- UI Component Creation ---

    /**
     * Creates the sidebar panel for the form fields.
     */
    private JPanel createFormSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FORM_BG_COLOR); // Use FORM_BG_COLOR for the sidebar
        panel.setPreferredSize(new Dimension(350, 0)); // Fixed width sidebar
        panel.setBorder(new LineBorder(BORDER_COLOR, 1, false)); // Simple right border

        // Sidebar Title/Header
        JLabel title = new JLabel("Customer Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_LIGHT);
        title.setBorder(new EmptyBorder(15, 20, 15, 20)); // Padding for the header
        title.setAlignmentX(Component.LEFT_ALIGNMENT); // Align left
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        txtId = createTextField(false);
        txtFullName = createTextField(true);
        txtAddress = createTextField(true);
        txtContact = createTextField(true);
        txtEmail = createTextField(true);
        txtPan = createTextField(true);

        // Input Grid
        JPanel inputGrid = new JPanel(new GridBagLayout()); // Using GridBag for precise control
        inputGrid.setBackground(FORM_BG_COLOR);
        inputGrid.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0); // Padding between rows
        gbc.gridx = 0;

        inputGrid.add(createInputRow("ID (Auto):", txtId), gbc);
        inputGrid.add(createInputRow("Full Name:", txtFullName), gbc);
        inputGrid.add(createInputRow("Address:", txtAddress), gbc);
        inputGrid.add(createInputRow("Contact No:", txtContact), gbc);
        inputGrid.add(createInputRow("Email:", txtEmail), gbc);
        inputGrid.add(createInputRow("PAN Number:", txtPan), gbc);

        panel.add(inputGrid);

        // Action Buttons (specific to the form, like a 'Clear' button)
        panel.add(createFormButtonPanel());
        panel.add(Box.createVerticalGlue()); // Push content up

        return panel;
    }

    private JPanel createFormButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(FORM_BG_COLOR);

        btnClear = createStyledButton("Clear Form", this::handleClearAction, ACCENT_COLOR.darker()); // Darker color for secondary action
        panel.add(btnClear);
        return panel;
    }

    private JPanel createInputRow(String labelText, JTextField textField) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(FORM_BG_COLOR);

        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(100, 25)); // Consistent label width

        row.add(label, BorderLayout.WEST);
        row.add(textField, BorderLayout.CENTER);
        return row;
    }

    private JTextField createTextField(boolean editable) {
        JTextField tf = new JTextField(15);
        tf.setEditable(editable);
        tf.setBackground(BG_DARK); // Use the darkest color for input fields
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(ACCENT_COLOR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    /**
     * Creates the main content area (Table + Header with Actions).
     */
    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding to the whole content area

        // 1. Header/Toolbar Panel (Top)
        JPanel toolbarPanel = createToolbarPanel();
        panel.add(toolbarPanel, BorderLayout.NORTH);

        // 2. Table Panel (Center)
        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the toolbar containing action buttons for the table.
     */
    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0)); // Spacing below the toolbar

        // Left Side: Title/List Name
        JLabel titleLabel = new JLabel("Customer List (Data Management)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_LIGHT);
        panel.add(titleLabel, BorderLayout.WEST);

        // Right Side: Action Buttons
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(BG_DARK);

        btnAddMain = createStyledButton("New Customer", this::handleAddAction, BG_DARK);
        btnUpdateMain = createStyledButton("Update Selected", this::handleUpdateAction, BG_SECONDARY); // A softer color
        btnDeleteMain = createStyledButton("Delete Selected", this::handleDeleteAction, Color.RED.darker()); // Danger color
        btnRefresh = createStyledButton("Refresh", this::handleRefreshAction, BG_SECONDARY);

        buttonGroup.add(btnRefresh);
        buttonGroup.add(btnDeleteMain);
        buttonGroup.add(btnUpdateMain);
        buttonGroup.add(btnAddMain);

        panel.add(buttonGroup, BorderLayout.EAST);
        return panel;
    }

    private JButton createStyledButton(String text, Consumer<ActionEvent> action, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_LIGHT); // Light text for all main buttons
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
        String[] columnNames = {"ID", "Full Name", "Address", "Contact No", "Email", "PAN", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        styleTable(customerTable);

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                displaySelectedCustomer(customerTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(customerTable);
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
        table.setRowHeight(35); // Even taller rows
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
                setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                setBorder(new EmptyBorder(0, 15, 0, 15)); // Increased cell padding
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(6).setPreferredWidth(160); // Created At
    }

    // --- Data and Action Logic (Simplified for the example) ---

    private void loadCustomerData() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            String formattedDateTime = customer.getCreatedAt() != null ?
                    customer.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                    "N/A";
            tableModel.addRow(new Object[]{
                    customer.getId(),
                    customer.getFullName(),
                    customer.getAddress(),
                    customer.getContactNo(),
                    customer.getEmail(),
                    customer.getPanNumber(),
                    formattedDateTime
            });
        }
    }

    private void displaySelectedCustomer(int row) {
        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtFullName.setText(tableModel.getValueAt(row, 1).toString());
        txtAddress.setText(tableModel.getValueAt(row, 2).toString());
        txtContact.setText(tableModel.getValueAt(row, 3).toString());
        txtEmail.setText(tableModel.getValueAt(row, 4).toString());
        txtPan.setText(tableModel.getValueAt(row, 5).toString());
    }

    private void handleAddAction(ActionEvent e) {
        // ... (Input validation and logic - same as before)
        try {
            Customer newCustomer = new Customer();
            newCustomer.setFullName(txtFullName.getText());
            newCustomer.setAddress(txtAddress.getText());
            newCustomer.setContactNo(txtContact.getText());
            newCustomer.setEmail(txtEmail.getText());
            newCustomer.setPanNumber(txtPan.getText());
            newCustomer.setCreatedAt(LocalDateTime.now());

            if (customerService.createCustomer(newCustomer)) {
                JOptionPane.showMessageDialog(this, "Customer added successfully! 🎉", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer. Check inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateAction(ActionEvent e) {
        // ... (Input validation and logic - same as before)
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a customer to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer customer = new Customer();
            customer.setId(Integer.parseInt(txtId.getText()));
            customer.setFullName(txtFullName.getText());
            customer.setAddress(txtAddress.getText());
            customer.setContactNo(txtContact.getText());
            customer.setEmail(txtEmail.getText());
            customer.setPanNumber(txtPan.getText());

            if (customerService.updateCustomer(customer)) {
                JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteAction(ActionEvent e) {
        // ... (Logic - same as before)
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a customer to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Customer ID: " + id + "? This cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (customerService.deleteCustomer(id)) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClearAction(null);
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer. Check for account dependencies.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleClearAction(ActionEvent e) {
        txtId.setText("");
        txtFullName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtPan.setText("");
        customerTable.clearSelection();
    }

    private void handleRefreshAction(ActionEvent e) {
        loadCustomerData();
    }
}