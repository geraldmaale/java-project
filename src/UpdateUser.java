import javax.swing.*;
import java.awt.*;

public class UpdateUser extends JFrame {
    private JLabel txtId;
    private int UserId;
    private JPanel UpdateUserPane;

    public UpdateUser(int userId) {
        setTitle("Update User");
        setContentPane(UpdateUserPane);
        setLocationRelativeTo(this);
        setMinimumSize(new Dimension(500, 460));
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        UserId = userId;
        txtId.setText(String.valueOf(UserId));

        setVisible(true);
    }

    public static void main(String[] args) {
//        UpdateUser updateUser = new UpdateUser(UserId);
    }
}
