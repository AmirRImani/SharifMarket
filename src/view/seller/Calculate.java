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
import lists.ListOfProducts;
import sharedClasses.Product;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Calculate implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private TableView table;

    @FXML
    Label labelTotalSell, labelTotalProfit;

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

        TableColumn<Product, String> column1 = new TableColumn<>("Name");
        column1.setMinWidth(150);
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> column2 = new TableColumn<>("Total Sell");
        column2.setMinWidth(100);
        column2.setCellValueFactory(new PropertyValueFactory<>("totalSell"));

        TableColumn<Product, String> column3 = new TableColumn<>("Total Profit");
        column3.setMinWidth(100);
        column3.setCellValueFactory(new PropertyValueFactory<>("totalProfit"));

        TableColumn<Product, String> column4 = new TableColumn<>("Sold Quantity");
        column4.setMinWidth(100);
        column4.setCellValueFactory(new PropertyValueFactory<>("soldQuantity"));

        TableColumn<Product, String> column5 = new TableColumn<>("Unit");
        column5.setMinWidth(100);
        column5.setCellValueFactory(new PropertyValueFactory<>("unit"));

        table.setItems(getCalculatedProduct());
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        table.setOpacity(0.75);
        table.setMaxHeight(400);

        anchorPane.getChildren().add(table);
    }

    private ObservableList<CalculatedProduct> getCalculatedProduct() {
        double totalSell = 0;
        double totalProfit = 0;

        ListOfProducts list = ListOfProducts.getListInstance();
        HashSet<Product> products = list.getProducts();
        HashSet<Product> deletedProducts = list.getNProducts();
        ObservableList<CalculatedProduct> calculatedProducts = FXCollections.observableArrayList();
        for (Product deletedProduct : deletedProducts) {
            calculatedProducts.add(new CalculatedProduct(deletedProduct.getName(), sell(deletedProduct),
                    profit(deletedProduct), soldQuantity(deletedProduct), deletedProduct.getUnit()));
            totalSell += sell(deletedProduct);
            totalProfit += profit(deletedProduct);
        }
        for (Product product : products) {
            calculatedProducts.add(new CalculatedProduct(product.getName(), sell(product),
                    profit(product), soldQuantity(product), product.getUnit()));
            totalSell += sell(product);
            totalProfit += profit(product);
        }

        labelTotalSell.setText(Double.toString(totalSell) + " T");
        labelTotalProfit.setText(Double.toString(totalProfit) + " T");

        return calculatedProducts;
    }

    private double soldQuantity(Product product) {
        return ListOfOrders.getListInstance().orderCounter(product.getProductID());
    }

    private int sell(Product product) {
        return ListOfOrders.getListInstance().totalSell(product);
    }

    private int profit(Product product) {
        return ListOfOrders.getListInstance().totalProfit(product);
    }

}
