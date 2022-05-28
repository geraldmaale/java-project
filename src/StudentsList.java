import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentsList extends JFrame {
    private JTable tblStudents;
    private JPanel StudentsPanel;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnRegister;
    private JPanel StudentsHeader;
    private int SelectedRow;

    public StudentsList() {
        super();
        setTitle("List of users");
        setContentPane(StudentsPanel);
        setMinimumSize(new Dimension(500, 460));
        setLocationRelativeTo(null);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadStudents();

        setVisible(true);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelectedRow = tblStudents.getSelectedRow();

                int selectedUserId = (int) tblStudents.getValueAt(SelectedRow, 0);
                System.out.println(selectedUserId);

                // Pass the selected user id to the UpdateUser class
                UpdateUser updateUser = new UpdateUser(selectedUserId);
                updateUser.setVisible(true);
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

    public static void main(String[] args) {
        StudentsList studentsList = new StudentsList();
        studentsList.setVisible(true);
    }
}
