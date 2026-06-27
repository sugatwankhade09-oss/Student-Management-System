import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentManagementGUI extends JFrame {

    // DAO Object
    private StudentDAO dao = new StudentDAO();

    // Labels
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblAge;
    private JLabel lblCourse;


    // Text Fields
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtCourse;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnClear;

    // Table
    private JTable table;
    private DefaultTableModel model;

    // Selected Student ID
    private int selectedId = -1;

    public StudentManagementGUI() {

        setTitle("Student Management System");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📚 Student Management System", SwingConstants.CENTER);
title.setFont(new Font("Arial", Font.BOLD, 26));
title.setOpaque(true);
title.setBackground(new Color(33, 150, 243));
title.setForeground(Color.WHITE);
title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

JPanel headerPanel = new JPanel(new BorderLayout());

headerPanel.add(title, BorderLayout.NORTH);

createGUI();   // Don't call add(topPanel...) here

add(headerPanel, BorderLayout.NORTH);

        createGUI();

        loadStudents();

        setVisible(true);
    }

    private void createGUI() {

        // ===== FORM PANEL =====

        JPanel formPanel = new JPanel(new GridLayout(4,2,10,10));

        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        lblId = new JLabel("ID");
        txtId = new JTextField();

        lblName = new JLabel("Name");
        txtName = new JTextField();

        lblAge = new JLabel("Age");
        txtAge = new JTextField();

        lblCourse = new JLabel("Course");
        txtCourse = new JTextField();

        formPanel.add(lblId);
        formPanel.add(txtId);

        formPanel.add(lblName);
        formPanel.add(txtName);

        formPanel.add(lblAge);
        formPanel.add(txtAge);

        formPanel.add(lblCourse);
        formPanel.add(txtCourse);

        // ===== BUTTON PANEL =====

        JPanel buttonPanel = new JPanel();

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnSearch = new JButton("Search");
        btnClear = new JButton("Clear");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnClear);
        
        btnAdd.addActionListener(e -> addStudent());

btnClear.addActionListener(e -> clearFields());

btnUpdate.addActionListener(e -> updateStudent());

btnDelete.addActionListener(e -> deleteStudent());

btnSearch.addActionListener(e -> searchStudent());



        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

      add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Course");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        tableClicked();
    }
});
}

            // ================= LOAD STUDENTS =================

    private void loadStudents() {

        model.setRowCount(0);

        List<Student> students = dao.getAllStudents();

        for (Student s : students) {

            model.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getAge(),
                    s.getCourse()
            });

        }
    }

    // ================= ADD STUDENT =================

    private void addStudent() {

        try {

            String name = txtName.getText();
            int age = Integer.parseInt(txtAge.getText());
            String course = txtCourse.getText();

            Student student = new Student(name, age, course);

            dao.addStudent(student);

            JOptionPane.showMessageDialog(this, "Student Added Successfully!");

            clearFields();

            loadStudents();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Please enter valid data!");

        }

    }

    // ================= CLEAR FIELDS =================

    private void clearFields() {

        txtName.setText("");
        txtAge.setText("");
        txtCourse.setText("");
        txtId.setText("");

        txtName.requestFocus();
    }

            // ================= UPDATE STUDENT =================

    private void updateStudent() {

        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }

        try {

            Student student = new Student(
                    selectedId,
                    txtName.getText(),
                    Integer.parseInt(txtAge.getText()),
                    txtCourse.getText());

            dao.updateStudent(student);

            JOptionPane.showMessageDialog(this, "Student Updated Successfully!");

            loadStudents();
            clearFields();
            selectedId = -1;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Data!");
        }
    }

    // ================= DELETE STUDENT =================

    private void deleteStudent() {

        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }

        dao.deleteStudent(selectedId);

        JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");

        loadStudents();
        clearFields();
        selectedId = -1;
    }
    // ================= SEARCH STUDENT =================

private void searchStudent() {

    try {

        int id = Integer.parseInt(txtId.getText());

        Student student = dao.searchStudent(id);

        if (student != null) {

            txtName.setText(student.getName());
            txtAge.setText(String.valueOf(student.getAge()));
            txtCourse.setText(student.getCourse());

            selectedId = student.getId();

        } else {

            JOptionPane.showMessageDialog(this, "Student not found!");

        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(this, "Enter a valid Student ID!");

    }

}

    // ================= TABLE CLICK =================

    private void tableClicked() {

        int row = table.getSelectedRow();

        if (row >= 0) {

            selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());

            txtId.setText(String.valueOf(selectedId));

            txtName.setText(model.getValueAt(row, 1).toString());
            txtAge.setText(model.getValueAt(row, 2).toString());
            txtCourse.setText(model.getValueAt(row, 3).toString());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new StudentManagementGUI());

    }

}
