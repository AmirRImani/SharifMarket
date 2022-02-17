package view.seller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lists.ListOfOrders;
import sharedClasses.Order;
import sharedClasses.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderHistory implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private TableView table;

    @FXML
    AnchorPane anchorPane;


    public void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("mainMenuPage.fxml"));

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableView();

        TableColumn<Order, String> column1 = new TableColumn<>("Customer Name");
        column1.setMinWidth(80);
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Order, String> column2 = new TableColumn<>("Date of Order");
        column2.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("dateOfOrder"));

        TableColumn<Order, String> column3 = new TableColumn<>("Product ID");
        column3.setMinWidth(100);
        column3.setCellValueFactory(new PropertyValueFactory<>("productID"));

        TableColumn<Order, String> column4 = new TableColumn<>("Inventory");
        column4.setMinWidth(80);
        column4.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<Order, String> column5 = new TableColumn<>("Sell");
        column5.setMinWidth(80);
        column5.setCellValueFactory(new PropertyValueFactory<>("sell"));

        table.setItems(getOrderHistory());
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        table.setOpacity(0.75);
        table.setMaxHeight(400);
        table.setPrefWidth(560);

        anchorPane.getChildren().add(table);
    }

    private ObservableList<Order> getOrderHistory() {
        short cnt = 0;
        ListOfOrders list = ListOfOrders.getListInstance();
        ArrayList<Order> orders = list.getHistoryOrders();
        ObservableList<Order> ordersList = FXCollections.observableArrayList();
        for (int i = orders.size()-1; i > orders.size() - 11 && i >= 0; i--)
            ordersList.add(orders.get(i));
        return ordersList;
    }

}
