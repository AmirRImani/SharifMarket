package view.customers;

import customers.EnterProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Forget {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button btnReset, btnReset1;

    @FXML
    TextField txtUsername, txtEmail, txtPassword;

    @FXML
    Label labelForget, labelReset, labelUsername;

    @FXML
    ImageView imageView;



    public void reset(javafx.event.ActionEvent actionEvent) {
        EnterProcess enterProcess = new EnterProcess();
        if(txtUsername.getText().isEmpty())
            labelReset.setText("Please enter your username");
        else if(txtEmail.getText().isEmpty())
            labelReset.setText("Please enter your email address");
        else if(enterProcess.validity(txtUsername.getText(), txtEmail.getText())){
            labelReset.setText("Enter your new password");

            imageView.setImage(new Image(getClass().getResourceAsStream("/password.jpg")));

            labelUsername.setVisible(false);
            txtUsername.setVisible(false);
            txtEmail.setVisible(false);
            btnReset.setVisible(false);

            btnReset1.setVisible(true);
            txtPassword.setVisible(true);
        }
        else
            labelReset.setText("Username or email address is incorrect");
    }


    public void exit(ActionEvent actionEvent) {
       Alert aLert = new Alert(Alert.AlertType.NONE);
       aLert.setTitle("Exit");
       aLert.setHeaderText("You're about to exit the program");
       aLert.setContentText("Are you sure?");
       aLert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

       if(aLert.showAndWait().get() == ButtonType.YES) {
           System.exit(1);
       }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("entry.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void resetPass(ActionEvent actionEvent) {
        if(!txtPassword.getText().isEmpty()) {
            EnterProcess enterProcess = new EnterProcess();
            enterProcess.newPassword(txtUsername.getText(), txtPassword.getText());
            labelReset.setText("Your password has been changed successfully");

        }
    }
}
