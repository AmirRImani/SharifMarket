package view.customers;

import customers.Customer;
import customers.CustomerProcess;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import lists.ListOfProducts;
import sharedClasses.Product;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ProductList implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private TableView table;
    private Customer customer;
    private CustomerProcess customerProcess;

    @FXML
    Button btnChangeList;

    @FXML
    Label labelName, labelInfo;

    @FXML
    TextField txtInventory;

    @FXML
    AnchorPane anchorPane;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerProcess = new CustomerProcess(customer);
    }

    public void back(ActionEvent actionEvent) throws IOException {
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

    public void buy(ActionEvent actionEvent) {
        Product productSelected = (Product) table.getSelectionModel().getSelectedItem();
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            if (txtInventory.getText().isEmpty() || Double.parseDouble(txtInventory.getText()) <= 0)
                labelInfo.setText("Please enter inventory");
            else {
                if(customerProcess.order(customer, productSelected, Double.parseDouble(txtInventory.getText()),
                        (int) (Double.parseDouble(txtInventory.getText()) * productSelected.getSellPrice()),
                        (int) (Double.parseDouble(txtInventory.getText()) * (productSelected.getSellPrice() - productSelected.getBuyPrice()))))
                    labelInfo.setText("Your order has been\nregistered");
                else
                    labelInfo.setText("Quantity isn't enough\nfor your order");
            }
        }
        txtInventory.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableView();

        TableColumn<Product, String> column1 = new TableColumn<>("Name");
        column1.setMinWidth(150);
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> column2 = new TableColumn<>("ID");
        column2.setMinWidth(150);
        column2.setCellValueFactory(new PropertyValueFactory<>("productID"));


        TableColumn<Product, String> column3 = new TableColumn<>("Price(Toman)");
        column3.setMinWidth(100);
        column3.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));

        TableColumn<Product, String> column4 = new TableColumn<>("Inventory");
        column4.setMinWidth(60);
        column4.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<Product, String> column5 = new TableColumn<>("Unit");
        column5.setMinWidth(40);
        column5.setCellValueFactory(new PropertyValueFactory<>("unit"));

        table.setItems(getAvailableProducts());
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        table.setOpacity(0.75);
        table.setMaxHeight(335);

        anchorPane.getChildren().add(table);

        TableView.TableViewSelectionModel selectionModel = table.getSelectionModel();
        ObservableList<Product> productsSelected = selectionModel.getSelectedItems();
        productsSelected.addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(Change<? extends Product> c) {
                labelName.setText(productsSelected.get(0).getName());
            }
        });

    }

    private ObservableList<Product> getAvailableProducts() {
        ListOfProducts list = ListOfProducts.getListInstance();
        HashSet<Product> products = list.getProducts();
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        for (Product product : products)
            productsList.add(product);
        return productsList;
    }

    private ObservableList<Product> getNAvailableProducts() {
        ListOfProducts list = ListOfProducts.getListInstance();
        HashSet<Product> products = list.getNProducts();
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        for (Product product : products)
            productsList.add(product);
        return productsList;
    }

    public void changeList(ActionEvent actionEvent) {
        if (btnChangeList.getText().equals("Available List")) {
           table.getItems().clear();
           table.setItems(getNAvailableProducts());
           btnChangeList.setText("Not Available List");
        } else {
            table.getItems().clear();
            table.setItems(getAvailableProducts());
            btnChangeList.setText("Available List");
        }
    }
}
