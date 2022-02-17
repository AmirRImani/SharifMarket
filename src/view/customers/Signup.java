package view.customers;

import customers.Customer;
import customers.EnterProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Signup {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button btnExit;

    @FXML
    Button btnSignup;

    @FXML
    TextField txtUsername;

    @FXML
    TextField txtPassword;

    @FXML
    TextField txtEmail;

    @FXML
    Label labelSignup;

    public void signup(javafx.event.ActionEvent actionEvent) throws IOException {
        EnterProcess enterProcess = new EnterProcess();
        Customer customer = enterProcess.signup(txtUsername.getText(), txtPassword.getText(), txtEmail.getText());
        if(txtUsername.getText().isEmpty())
            labelSignup.setText("Please enter your Username");
        else if(txtPassword.getText().isEmpty())
            labelSignup.setText("Please enter your Password");
        else if(txtEmail.getText().isEmpty())
            labelSignup.setText("Please enter your email address");
        else if(customer != null) {
            labelSignup.setText("Hello " + txtUsername.getText() + "\nWelcome to Sharif Market");
            enterProcess.addCustomer(customer);
            toMainPage(actionEvent, customer);
        }
        else
            labelSignup.setText("This username already exists");
    }

    private void toMainPage(ActionEvent actionEvent, Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("customerMarketPage.fxml"));
        root = loader.load();

        CustomerMarket customerMarket = loader.getController();
        customerMarket.setCustomer(customer);

        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}
