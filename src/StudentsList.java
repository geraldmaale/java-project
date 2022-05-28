import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class StudentsList extends JFrame {
    private JTable tblStudents;
    private JPanel StudentsPanel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRegister;
    private JPanel StudentsHeader;
    private int SelectedRow;

    public StudentsList() {
        super();
        setTitle("List of users");
        setContentPane(StudentsPanel);
        setMinimumSize(new Dimension(500, 460));
        setLocationRelativeTo(getParent());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadStudents();
        loadUIButtons(true);

        setVisible(true);

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelectedRow = tblStudents.getSelectedRow();

                if (SelectedRow == -1) {
                    JOptionPane.showMessageDialog(StudentsPanel, "Please select a user to edit", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    int selectedUserId = (int) tblStudents.getValueAt(SelectedRow, 0);

                    // Pass the selected user id to the UpdateUser class
                    UpdateUser updateUser = new UpdateUser(selectedUserId);
                    dispose();
                    updateUser.setVisible(true);
                }
            }
        });
        tblStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pass the selected user id to the UpdateUser class
                RegistrationForm registrationForm = new RegistrationForm();
                dispose();
                registrationForm.setVisible(true);
        }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedRow = tblStudents.getSelectedRow();

                if (SelectedRow == -1) {
                    JOptionPane.showMessageDialog(StudentsPanel, "Please select a user to delete", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    int selectedUserId = (int) tblStudents.getValueAt(SelectedRow, 0);
                    deleteUser(selectedUserId);
                }
            }
        });
    }

    private Connection connectToDb() {
        final String DB_URL = "jdbc:postgresql://localhost:5432/studentportaldb";
        final String USERNAME = "appuser";
        final String PASSWORD = "P@ssword1";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected successfully
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadStudents() {
        Connection conn = connectToDb();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            ResultSet resultSet = stat.executeQuery("SELECT * FROM users");

            // Display data in table
            tblStudents.setModel(DbUtils.resultSetToTableModel(resultSet));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void CreateDatabase() {
        final String DB_URL = "jdbc:postgresql://localhost:5432/studentportaldb";
        final String USERNAME = "appuser";
        final String PASSWORD = "P@ssword1";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected successfully

            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE users (id SERIAL PRIMARY KEY, fullName VARCHAR(50), email VARCHAR(50), address VARCHAR(255), phoneNumber VARCHAR(10), password VARCHAR(25))";

            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Database successfully created", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Failed to create database", "Try again", JOptionPane.ERROR_MESSAGE);
            }

            statement.close();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StudentsList studentsList = new StudentsList();
        studentsList.setVisible(true);
    }

    private void deleteUser(int selectedUserId) {
        Connection conn = connectToDb();
        Statement stat = null;
        try {
            stat = conn.createStatement();
            int rowsDeleted = stat.executeUpdate("DELETE FROM users WHERE id = " + selectedUserId);
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "User successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            }
            else {
                JOptionPane.showMessageDialog(this, "Failed to delete user", "Try again", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUIButtons(boolean isVisible) {
        btnDelete.setEnabled(isVisible);
        btnEdit.setEnabled(isVisible);
    }
}
