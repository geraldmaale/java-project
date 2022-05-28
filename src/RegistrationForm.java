import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JFrame {
    public User user;

    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;
    private JPanel registerPanel;

    public RegistrationForm(){
        super();
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,460));
        setLocationRelativeTo(getParent());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }


        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String address = txtAddress.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String password = new String(txtPassword.getPassword());
        String passwordConfirm = new String(txtPasswordConfirm.getPassword());

        // validate the user input
        if (fullName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            JOptionPane.showMessageDialog(registerPanel, "Please fill in all the fields", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate the password
        if (!password.equals(passwordConfirm)) {
            JOptionPane.showMessageDialog(registerPanel, "Passwords do not match", "Invalid login attempt", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new user
        user = new User();
        user.FullName = fullName;
        user.Email = email;
        user.Address = address;
        user.PhoneNumber = phoneNumber;
        user.Password = password;

        User newUser = addUser(user);
        if (newUser != null) {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(registerPanel, "Failed to register new user", "Try again", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    // Add a new user to the database
    private User addUser(User user) {

        try {
            Connection conn = connectToDb();
            // Connected successfully

            Statement statement = conn.createStatement();
            String sql = "INSERT INTO users (fullName, email, address, phoneNumber, password) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.FullName);
            preparedStatement.setString(2, user.Email);
            preparedStatement.setString(3, user.Address);
            preparedStatement.setString(4, user.PhoneNumber);
            preparedStatement.setString(5, user.Password);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(registerPanel, "User successfully registered", "Success", JOptionPane.INFORMATION_MESSAGE);

                StudentsList studentsList = new StudentsList();
                studentsList.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(registerPanel, "Failed to register new user", "Try again", JOptionPane.ERROR_MESSAGE);
            }

            statement.close();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
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
        RegistrationForm registrationForm = new RegistrationForm();

        // Create database
         // registrationForm.CreateDatabase();

        User user = registrationForm.user;
        if (user != null) {
            System.out.println("Successful registration of : " + user.FullName);
        }
        else {
            System.out.println("Registration cancelled");
        }
    }
}
