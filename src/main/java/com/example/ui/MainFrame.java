package com.example.ui;

import com.example.entity.Department;
import com.example.entity.Employee;
import com.example.repository.CompanyRepository;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final CompanyRepository repository;
    private JComboBox<Department> departmentComboBox;
    private JTextField deptNameField;
    private JTextField empNameField;
    private JTextField searchDeptField;
    private JTextField searchEmpField;
    private JTextArea empListArea;
    private JLabel employeeCountLabel;
    private JPanel notificationPanel;
    private Timer notificationTimer;

    // Custom colors
    private final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private final Color LIGHT_BG_COLOR = new Color(245, 245, 250);
    private final Color DARK_TEXT_COLOR = new Color(33, 33, 33);
    private final Color LIGHT_TEXT_COLOR = new Color(250, 250, 250);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color ERROR_COLOR = new Color(244, 67, 54);

    public MainFrame() {
        repository = new CompanyRepository();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Company Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom look and feel properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", LIGHT_TEXT_COLOR);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(LIGHT_BG_COLOR);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with department and employee sections
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(LIGHT_BG_COLOR);

        // Department section
        JPanel departmentPanel = createDepartmentPanel();
        contentPanel.add(departmentPanel);

        // Employee section
        JPanel employeePanel = createEmployeePanel();
        contentPanel.add(employeePanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Notification panel (initially hidden)
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BorderLayout());
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        notificationPanel.setVisible(false);
        mainPanel.add(notificationPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Company Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(LIGHT_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createDepartmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Department panel title
        JLabel titleLabel = new JLabel("Department Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Department form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Add department section
        JPanel addDeptPanel = new JPanel(new BorderLayout(5, 0));
        addDeptPanel.setBackground(Color.WHITE);
        addDeptPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Add Department",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JLabel deptNameLabel = new JLabel("Department Name:");
        deptNameField = new JTextField(20);
        deptNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel deptInputPanel = new JPanel(new BorderLayout(5, 0));
        deptInputPanel.setBackground(Color.WHITE);
        deptInputPanel.add(deptNameLabel, BorderLayout.NORTH);
        deptInputPanel.add(deptNameField, BorderLayout.CENTER);

        JButton addDeptButton = createStyledButton("Add Department");
        addDeptButton.addActionListener(e -> addDepartment());

        addDeptPanel.add(deptInputPanel, BorderLayout.CENTER);
        addDeptPanel.add(addDeptButton, BorderLayout.EAST);

        // Search department section
        JPanel searchDeptPanel = new JPanel(new BorderLayout(5, 0));
        searchDeptPanel.setBackground(Color.WHITE);
        searchDeptPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Search Department",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JLabel searchDeptLabel = new JLabel("Search Term:");
        searchDeptField = new JTextField(20);
        searchDeptField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel searchDeptInputPanel = new JPanel(new BorderLayout(5, 0));
        searchDeptInputPanel.setBackground(Color.WHITE);
        searchDeptInputPanel.add(searchDeptLabel, BorderLayout.NORTH);
        searchDeptInputPanel.add(searchDeptField, BorderLayout.CENTER);

        JButton searchDeptButton = createStyledButton("Search");
        searchDeptButton.addActionListener(e -> searchDepartments());

        searchDeptPanel.add(searchDeptInputPanel, BorderLayout.CENTER);
        searchDeptPanel.add(searchDeptButton, BorderLayout.EAST);

        // Department selection section
        JPanel deptSelectionPanel = new JPanel(new BorderLayout(5, 5));
        deptSelectionPanel.setBackground(Color.WHITE);
        deptSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Department Selection",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JLabel selectDeptLabel = new JLabel("Select Department:");
        departmentComboBox = new JComboBox<>();
        departmentComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        departmentComboBox.addActionListener(e -> updateEmployeeCount());

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.setBackground(Color.WHITE);
        JLabel countTextLabel = new JLabel("Employee Count:");
        countTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeCountLabel = new JLabel("0");
        employeeCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        countPanel.add(countTextLabel);
        countPanel.add(employeeCountLabel);

        deptSelectionPanel.add(selectDeptLabel, BorderLayout.NORTH);
        deptSelectionPanel.add(departmentComboBox, BorderLayout.CENTER);
        deptSelectionPanel.add(countPanel, BorderLayout.SOUTH);

        // Add all sections to form panel
        formPanel.add(addDeptPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(searchDeptPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(deptSelectionPanel);

        panel.add(formPanel, BorderLayout.CENTER);

        // Initialize department combo box
        updateDepartmentComboBox();

        return panel;
    }

    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Employee panel title
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Employee form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Add employee section
        JPanel addEmpPanel = new JPanel(new BorderLayout(5, 0));
        addEmpPanel.setBackground(Color.WHITE);
        addEmpPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Add Employee",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JLabel empNameLabel = new JLabel("Employee Name:");
        empNameField = new JTextField(20);
        empNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel empInputPanel = new JPanel(new BorderLayout(5, 0));
        empInputPanel.setBackground(Color.WHITE);
        empInputPanel.add(empNameLabel, BorderLayout.NORTH);
        empInputPanel.add(empNameField, BorderLayout.CENTER);

        JButton addEmpButton = createStyledButton("Add Employee");
        addEmpButton.addActionListener(e -> addEmployee());

        addEmpPanel.add(empInputPanel, BorderLayout.CENTER);
        addEmpPanel.add(addEmpButton, BorderLayout.EAST);

        // Search employee section
        JPanel searchEmpPanel = new JPanel(new BorderLayout(5, 0));
        searchEmpPanel.setBackground(Color.WHITE);
        searchEmpPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Search Employee",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        JLabel searchEmpLabel = new JLabel("Search Term:");
        searchEmpField = new JTextField(20);
        searchEmpField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel searchEmpInputPanel = new JPanel(new BorderLayout(5, 0));
        searchEmpInputPanel.setBackground(Color.WHITE);
        searchEmpInputPanel.add(searchEmpLabel, BorderLayout.NORTH);
        searchEmpInputPanel.add(searchEmpField, BorderLayout.CENTER);

        JButton searchEmpButton = createStyledButton("Search");
        searchEmpButton.addActionListener(e -> searchEmployees());

        searchEmpPanel.add(searchEmpInputPanel, BorderLayout.CENTER);
        searchEmpPanel.add(searchEmpButton, BorderLayout.EAST);

        // Employee list section
        JPanel empListPanel = new JPanel(new BorderLayout(5, 5));
        empListPanel.setBackground(Color.WHITE);
        empListPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Employees in Department",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12)
        ));

        empListArea = new JTextArea(10, 20);
        empListArea.setEditable(false);
        empListArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        empListArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(empListArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JButton refreshEmpButton = createStyledButton("Refresh List");
        refreshEmpButton.addActionListener(e -> refreshEmployeeList());

        empListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(Color.WHITE);
        refreshPanel.add(refreshEmpButton);
        empListPanel.add(refreshPanel, BorderLayout.SOUTH);

        // Add all sections to form panel
        formPanel.add(addEmpPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(searchEmpPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(empListPanel);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(LIGHT_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void showNotification(String message, boolean isSuccess) {
        // Cancel existing timer if running
        if (notificationTimer != null && notificationTimer.isRunning()) {
            notificationTimer.stop();
        }

        // Configure notification panel
        notificationPanel.removeAll();
        notificationPanel.setBackground(isSuccess ? SUCCESS_COLOR : ERROR_COLOR);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(LIGHT_TEXT_COLOR);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificationPanel.add(messageLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("×");
        closeButton.setForeground(LIGHT_TEXT_COLOR);
        closeButton.setBackground(isSuccess ? SUCCESS_COLOR : ERROR_COLOR);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> notificationPanel.setVisible(false));
        notificationPanel.add(closeButton, BorderLayout.EAST);

        notificationPanel.setVisible(true);

        // Auto-hide after 3 seconds
        notificationTimer = new Timer(3000, e -> notificationPanel.setVisible(false));
        notificationTimer.setRepeats(false);
        notificationTimer.start();

        // Revalidate and repaint
        revalidate();
        repaint();
    }

    private void addDepartment() {
        String deptName = deptNameField.getText().trim();
        if (!deptName.isEmpty()) {
            Department department = new Department(deptName);
            repository.addDepartment(department);
            deptNameField.setText("");
            updateDepartmentComboBox();
            showNotification("Department added successfully!", true);
        } else {
            showNotification("Please enter a department name.", false);
        }
    }

    private void addEmployee() {
        String empName = empNameField.getText().trim();
        Department selectedDept = (Department) departmentComboBox.getSelectedItem();
        if (!empName.isEmpty() && selectedDept != null) {
            Employee employee = new Employee(empName, selectedDept);
            repository.addEmployee(employee);
            empNameField.setText("");
            refreshEmployeeList();
            updateEmployeeCount();
            showNotification("Employee added successfully!", true);
        } else {
            showNotification("Please enter employee name and select a department.", false);
        }
    }

    private void searchDepartments() {
        String searchText = searchDeptField.getText().trim();
        if (!searchText.isEmpty()) {
            List<Department> departments = repository.findDepartmentsByName(searchText);
            departmentComboBox.removeAllItems();
            for (Department dept : departments) {
                departmentComboBox.addItem(dept);
            }
            showNotification("Found " + departments.size() + " departments.", true);
        } else {
            updateDepartmentComboBox();
            showNotification("Please enter a search term.", false);
        }
    }

    private void searchEmployees() {
        String searchText = searchEmpField.getText().trim();
        if (!searchText.isEmpty()) {
            List<Employee> employees = repository.findEmployeesByName(searchText);
            empListArea.setText("");
            for (Employee emp : employees) {
                empListArea.append(emp.getName() + " (Dept: " + emp.getDepartment().getName() + ")\n");
            }
            showNotification("Found " + employees.size() + " employees.", true);
        } else {
            showNotification("Please enter a search term.", false);
        }
    }

    private void updateDepartmentComboBox() {
        departmentComboBox.removeAllItems();
        List<Department> departments = repository.getAllDepartments();
        for (Department dept : departments) {
            departmentComboBox.addItem(dept);
        }
        departmentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department) {
                    setText(((Department) value).getName());
                }
                return this;
            }
        });

        // Update employee count
        updateEmployeeCount();
    }

    private void refreshEmployeeList() {
        Department selectedDept = (Department) departmentComboBox.getSelectedItem();
        if (selectedDept != null) {
            List<Employee> employees = repository.getEmployeesByDepartment(selectedDept.getId());
            empListArea.setText("");
            for (Employee emp : employees) {
                empListArea.append(emp.getName() + "\n");
            }
        }
    }

    private void updateEmployeeCount() {
        Department selectedDept = (Department) departmentComboBox.getSelectedItem();
        if (selectedDept != null) {
            Long count = repository.countEmployeesInDepartment(selectedDept.getId());
            employeeCountLabel.setText(count.toString());
        } else {
            employeeCountLabel.setText("0");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}