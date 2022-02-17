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
import sharedClasses.Order;
import sharedClasses.Product;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Basket implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private TableView table;
    private Customer customer;
    private CustomerProcess customerProcess;
    private Order orderSelected;
    private double totalPrice;

    @FXML
    Label labelName, labelInfo, labelPrice, labelMoney;

    @FXML
    TextField txtInventory;

    @FXML
    AnchorPane anchorPane;

    public void setInitial(Customer customer) {
        this.customer = customer;
        customerProcess = new CustomerProcess(customer);
        setItems();
        labelMoney.setText(Integer.toString(customer.getCash()) + " T");
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

    public void change(ActionEvent actionEvent) {
        Order orderSelected = (Order) table.getSelectionModel().getSelectedItem();
        if (orderSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(orderSelected.getProductName());

            if (txtInventory.getText().isEmpty() || Double.parseDouble(txtInventory.getText()) <= 0) {
                labelInfo.setText("Please enter inventory");

            } else {

                if(customerProcess.enough(ListOfProducts.getListInstance().getProduct(orderSelected.getProductID()), Double.parseDouble(txtInventory.getText()))) {
                    Product product = ListOfProducts.getListInstance().getProduct(orderSelected.getProductID());
                    labelInfo.setText("Your order has been\nchanged");
                    orderSelected.setInventory(Double.parseDouble(txtInventory.getText()));
                    customer.setInventory(product, Double.parseDouble(orderSelected.getInventory()));
                    setItems();
                } else
                    labelInfo.setText("Quantity isn't enough\nfor your order");
            }
        }
        txtInventory.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableView();

        TableColumn<Product, String> column1 = new TableColumn<>("Product Name");
        column1.setMinWidth(150);
        column1.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Product, String> column2 = new TableColumn<>("Order ID");
        column2.setMinWidth(150);
        column2.setCellValueFactory(new PropertyValueFactory<>("orderID"));


        TableColumn<Product, String> column3 = new TableColumn<>("Inventory");
        column3.setMinWidth(60);
        column3.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<Product, String> column4 = new TableColumn<>("Unit");
        column4.setMinWidth(40);
        column4.setCellValueFactory(new PropertyValueFactory<>("unit"));


        table.getColumns().addAll(column1, column2, column3, column4);
        table.setOpacity(0.75);
        table.setMaxHeight(335);

        anchorPane.getChildren().add(table);

        TableView.TableViewSelectionModel selectionModel = table.getSelectionModel();
        ObservableList<Order> ordersSelected = selectionModel.getSelectedItems();
        ordersSelected.addListener(new ListChangeListener<Order>() {
            @Override
            public void onChanged(Change<? extends Order> c) {
                orderSelected = ordersSelected.get(0);
                labelName.setText(orderSelected.getProductName());
            }
        });

    }

    private void setItems() {
        table.getItems().clear();
        table.setItems(getOrders());
        totalPrice();
    }

    private void totalPrice() {
        double price = 0;
        ObservableList<Order> orders = table.getItems();
        for (Order order : orders) {
            price += ListOfProducts.getListInstance().getProduct(order.getProductID()).getSellPrice()
                    * Double.parseDouble(order.getInventory());
        }
        totalPrice = price;
        labelPrice.setText(totalPrice + " T");
    }

    private void moneyUpdate() {
        labelMoney.setText(Double.toString(customer.getCash()) + " T");
    }

    private ObservableList<Order> getOrders() {
        ListOfOrders list = ListOfOrders.getListInstance();
        HashSet<Order> orders = list.getOrders(customer);
        ObservableList<Order> ordersList = FXCollections.observableArrayList();
        for (Order order : orders)
            ordersList.add(order);
        return ordersList;
    }

    public void increase(ActionEvent actionEvent) {
        if (orderSelected == null)
            labelInfo.setText("Please select an item");
        else {
            Product product = ListOfProducts.getListInstance().getProduct(orderSelected.getProductID());
            if (customerProcess.enough(product, Double.parseDouble(orderSelected.getInventory()) + 1)) {
                orderSelected.setInventory(Double.parseDouble(orderSelected.getInventory()) + 1);
                customer.setInventory(product, Double.parseDouble(orderSelected.getInventory()));
                setItems();
            }
        }
    }

    public void decrease(ActionEvent actionEvent) {
        Product product = ListOfProducts.getListInstance().getProduct(orderSelected.getProductID());
        if (orderSelected == null)
            labelInfo.setText("Please select an item");
        else {
            if (Double.parseDouble(orderSelected.getInventory()) - 1 >= 0.1) {
                orderSelected.setInventory(Double.parseDouble(orderSelected.getInventory()) - 1);
                customer.setInventory(product, Double.parseDouble(orderSelected.getInventory()));
                setItems();
            }
        }
    }

    public void buy(ActionEvent actionEvent) {
        if(totalPrice <= customer.getCash()) {
            customer.buy(totalPrice);
            ListOfOrders.getListInstance().buy(customer);
            labelInfo.setText("Bought successfully");
            moneyUpdate();
            setItems();
        } else
            labelInfo.setText("Not enough money");
    }

    public void delete(ActionEvent actionEvent) {
        customerProcess.deleteOrder(customer, orderSelected);
        setItems();
    }
}
