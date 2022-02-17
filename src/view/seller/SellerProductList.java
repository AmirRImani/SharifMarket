package view.seller;

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
import lists.ListOfProducts;
import sharedClasses.Product;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class SellerProductList implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private TableView table;
    private Product productSelected;
    private ListOfProducts list = ListOfProducts.getListInstance();

    @FXML
    Button btnChangeList;

    @FXML
    Label labelName, labelInfo;

    @FXML
    TextField txt, txtName, txtBuyPrice, txtSellPrice, txtInventory, txtUnit;

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

//    public void buy(ActionEvent actionEvent) {
//        Product productSelected = (Product) table.getSelectionModel().getSelectedItem();
//        if (productSelected == null)
//            labelInfo.setText("Please select an item");
//        else {
//            labelName.setText(productSelected.getName());
//
//            if (txtInventory.getText().isEmpty() || Double.parseDouble(txtInventory.getText()) <= 0)
//                labelInfo.setText("Please enter inventory");
//            else {
//                if(customerProcess.order(customer, productSelected, Double.parseDouble(txtInventory.getText())))
//                    labelInfo.setText("Your order has been\nregistered");
//                else
//                    labelInfo.setText("Quantity isn't enough\nfor your order");
//            }
//        }
//        txtInventory.setText("");
//    }

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
        column4.setMinWidth(100);
        column4.setCellValueFactory(new PropertyValueFactory<>("inventory"));

        TableColumn<Product, String> column5 = new TableColumn<>("Unit");
        column5.setMinWidth(80);
        column5.setCellValueFactory(new PropertyValueFactory<>("unit"));

        table.setItems(getAvailableProducts());
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        table.setOpacity(0.75);
        table.setMaxHeight(285);

        anchorPane.getChildren().add(table);

        TableView.TableViewSelectionModel selectionModel = table.getSelectionModel();
        ObservableList<Product> productsSelected = selectionModel.getSelectedItems();
        productsSelected.addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(Change<? extends Product> c) {
                productSelected = productsSelected.get(0);
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

    private ObservableList<Product> getAllProducts() {
        ListOfProducts list = ListOfProducts.getListInstance();
        HashSet<Product> products = list.getProducts();
        HashSet<Product> notAvailableProducts = list.getNProducts();
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        for (Product product : products)
            productsList.add(product);
        for (Product notAvailableProduct : notAvailableProducts)
            productsList.add(notAvailableProduct);
        return productsList;
    }

    public void changeList(ActionEvent actionEvent) {
        if (btnChangeList.getText().equals("Available List")) {
            table.getItems().clear();
            table.setItems(getNAvailableProducts());
            btnChangeList.setText("Not Available List");
        } else if (btnChangeList.getText().equals("Not Available List")) {
            table.getItems().clear();
            table.setItems(getAllProducts());
            btnChangeList.setText("All Products");
        } else {
            table.getItems().clear();
            table.setItems(getAvailableProducts());
            btnChangeList.setText("Available List");
        }
    }

    private void setItems() {
        table.getItems().clear();
        if (btnChangeList.getText().equals("Available List"))
            table.setItems(getAvailableProducts());
        else if (btnChangeList.getText().equals("Not Available List"))
            table.setItems(getNAvailableProducts());
        else
            table.setItems(getAllProducts());
    }

    public void delete(ActionEvent actionEvent) {
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            list.deleteProduct(productSelected);
            setItems();
        }
    }

    public void changeName(ActionEvent actionEvent) {
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            if (txt.getText().isEmpty())
                labelInfo.setText("Please enter a name");
            else {
                productSelected.editProductName(txt.getText());
                setItems();
            }
        }
        txt.setText("");
    }

    public void changeInventory(ActionEvent actionEvent) {
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            if (txt.getText().isEmpty() || Double.parseDouble(txt.getText()) <= 0)
                labelInfo.setText("Please enter a valid\ninventory");
            else {
                productSelected.editProductInventory(txt.getText());
                labelInfo.setText("Inventory has been\nchanged successfully");
                setItems();
            }
        }
        txt.setText("");
    }

    public void changeBuyPrice(ActionEvent actionEvent) {
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            if (txt.getText().isEmpty() || Integer.parseInt(txt.getText()) <= 0)
                labelInfo.setText("Please enter a valid\nbuy price");
            else {
                if(productSelected.editBuyPrice(txt.getText())) {
                    labelInfo.setText("Buy price has been\nchanged successfully");
                    setItems();
                } else
                    labelInfo.setText("Buy price is more than\nsell price");
            }
        }
        txt.setText("");
    }

    public void changeSellPrice(ActionEvent actionEvent) {
        if (productSelected == null)
            labelInfo.setText("Please select an item");
        else {
            labelName.setText(productSelected.getName());

            if (txt.getText().isEmpty() || Integer.parseInt(txt.getText()) <= 0)
                labelInfo.setText("Please enter a valid\nsell price");
            else {
                if(productSelected.editSellPrice(txt.getText())) {
                    labelInfo.setText("Sell price has been\nchanged successfully");
                    setItems();
                } else
                    labelInfo.setText("Sell price is less than\nbuy price");
            }
        }
        txt.setText("");
    }

    public void addProduct(ActionEvent actionEvent) {
        if (txtName.getText().isEmpty())
            labelInfo.setText("Please enter a name");
        else if (txtBuyPrice.getText().isEmpty())
            labelInfo.setText("Please enter a buy price");
        else if (txtSellPrice.getText().isEmpty())
            labelInfo.setText("Please enter a sell price");
        else if (txtInventory.getText().isEmpty())
            labelInfo.setText("Please enter an inventory");
        else if (txtUnit.getText().isEmpty())
            labelInfo.setText("Please enter an unit");
        else if (Double.parseDouble(txtInventory.getText()) <= 0)
            labelInfo.setText("Please enter a valid\ninventory");
        else if (Integer.parseInt(txtBuyPrice.getText()) >= Integer.parseInt(txtSellPrice.getText()))
            labelInfo.setText("Buy Price is more than\nsell price");
        else if (list.productExistence(txtName.getText()))
            labelInfo.setText("This product already exists");
        else {
            list.addProduct(new Product(txtName.getText(), Double.parseDouble(txtInventory.getText()),
                    Integer.parseInt(txtSellPrice.getText()), Integer.parseInt(txtBuyPrice.getText()),
                    txtUnit.getText()));
            labelInfo.setText("Product added successfully");
            txtName.setText("");
            txtBuyPrice.setText("");
            txtSellPrice.setText("");
            txtInventory.setText("");
            txtUnit.setText("");
            setItems();
        }
    }
}
