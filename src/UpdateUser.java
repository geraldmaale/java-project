import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateUser extends JFrame {
    private JPanel UpdateUserPane;
    private int UserId;
    private JPanel registerPanel;
    private JPasswordField txtPassword;
    private JButton btnUpdate;
    private JButton btnCancel;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;

    public UpdateUser(int userId) {
        setTitle("Update User");
        setContentPane(UpdateUserPane);
        setLocationRelativeTo(getParent());
        setMinimumSize(new Dimension());
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getUser(userId);

        setVisible(true);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StudentsList studentsList = new StudentsList();
                studentsList.setVisible(true);
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // validate the user input
                if (txtFullName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAddress.getText().isEmpty() || txtPhoneNumber.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(UpdateUserPane, "Please fill in all the fields", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Connection connection = connectToDb();
                    Statement statement = connection.createStatement();
                    String sql = "UPDATE users SET fullname = '" + txtFullName.getText() + "', email = '" + txtEmail.getText() + "', address = '" + txtAddress.getText() + "', phonenumber = '" + txtPhoneNumber.getText() + "', password = '" + txtPassword.getText() + "' WHERE id = " + UserId;
                    statement.executeUpdate(sql);
                    JOptionPane.showMessageDialog(UpdateUserPane, "User updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    StudentsList studentsList = new StudentsList();
                    studentsList.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getUser(int userId){
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            Connection conn = connectToDb();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                UserId = rs.getInt("id");
                txtFullName.setText(rs.getString("fullname"));
                txtEmail.setText(rs.getString("email"));
                txtAddress.setText(rs.getString("address"));
                txtPhoneNumber.setText(rs.getString("phonenumber"));
                txtPassword.setText(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
//        UpdateUser updateUser = new UpdateUser(UserId);
    }
}
