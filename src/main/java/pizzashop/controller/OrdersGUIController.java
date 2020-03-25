package pizzashop.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import pizzashop.model.MenuDataModel;
import pizzashop.service.PaymentAlert;
import pizzashop.service.PizzaService;

import javax.swing.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class OrdersGUIController {

    @FXML
    private ComboBox<Integer> orderQuantity;
    @FXML
    private TableView orderTable;
    @FXML
    private TableColumn tableQuantity;
    @FXML
    protected TableColumn tableMenuItem;
    @FXML
    private TableColumn tablePrice;
    @FXML
    private Label pizzaTypeLabel;
    @FXML
    private Button addToOrder;
    @FXML
    private Label orderStatus;
    @FXML
    private Button placeOrder;
    @FXML
    private Button orderServed;
    @FXML
    private Button payOrder;
    @FXML
    private Button newOrder;
    static Logger log = Logger.getLogger(OrdersGUIController.class.getName());

    private  List<String> orderList = FXCollections.observableArrayList();
    private List<Double> orderPaymentList = FXCollections.observableArrayList();
    public static double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        OrdersGUIController.totalAmount = totalAmount;
    }

    private PizzaService service;
    private int tableNumber;

    public ObservableList<String> observableList;
    private TableView<MenuDataModel> table = new TableView<>();
    private ObservableList<MenuDataModel> menuData;
    private Calendar now = Calendar.getInstance();
    private static double totalAmount;
    private boolean isPay = false;

    public OrdersGUIController(){
        // constructor default
    }

    public void setService(PizzaService service, int tableNumber) throws IOException {
        this.service=service;
        this.tableNumber=tableNumber;
        initData();

    }

    private void initData() throws IOException {
        menuData = FXCollections.observableArrayList(service.getMenuData());
        menuData.setAll(service.getMenuData());
        orderTable.setItems(menuData);

        //Controller for Place Order Button
        placeOrder.setOnAction(event ->{
            orderList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity() +" "+ menuDataModel.getMenuItem())
                    .collect(Collectors.toList());
            observableList = FXCollections.observableList(orderList);
            KitchenGUIController.order.add("Table" + tableNumber +" "+ orderList.toString());
            orderStatus.setText("Order placed at: " +  now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));
        });

        //Controller for Order Served Button
        orderServed.setOnAction(event -> orderStatus.setText("Served at: " + now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE)));

        //Controller for Pay Order Button
        payOrder.setOnAction(event -> {
            orderPaymentList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity()*menuDataModel.getPrice())
                    .collect(Collectors.toList());
            setTotalAmount(orderPaymentList.stream().mapToDouble(e->e.doubleValue()).sum());
            orderStatus.setText("Total amount: " + getTotalAmount());
            log.debug("--------------------------");
            log.debug("Table: " + tableNumber);
            log.debug("Total: " + getTotalAmount());
            log.debug("--------------------------");
            PaymentAlert pay = new PaymentAlert(service);
            String typePayment = pay.showPaymentAlert(tableNumber, getTotalAmount());
            if(typePayment.equals("CASH") || typePayment.equals("CARD"))
                isPay=true;

        });
    }

    public void initialize(){

        //populate table view with menuData from OrderGUI
        table.setEditable(true);
        tableMenuItem.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, String>("menuItem"));
        tablePrice.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Double>("price"));
        tableQuantity.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Integer>("quantity"));

        //bind pizzaTypeLabel and quantity combo box with the selection on the table view
        orderTable.getSelectionModel().selectedItemProperty().addListener((ChangeListener<MenuDataModel>) (observable, oldValue, newValue) -> pizzaTypeLabel.textProperty().bind(newValue.menuItemProperty()));

        //Populate Combo box for Quantity
        ObservableList<Integer> quantityValues =  FXCollections.observableArrayList(0, 1, 2,3,4,5);
        orderQuantity.getItems().addAll(quantityValues);
        orderQuantity.setPromptText("Quantity");

        //Controller for Add to order Button
        addToOrder.setOnAction(event -> {

            try {
                // Item here is the table view type:
                TablePosition pos = (TablePosition) orderTable.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                MenuDataModel item = (MenuDataModel) orderTable.getItems().get(row);
                int oldValue = item.getQuantity();
                int newValue = oldValue + orderQuantity.getValue();
                item.setQuantity(newValue);
            }catch (Exception e)
            {
                log.debug("Selectati mai intai un tip de pizza si cantitatea dorita");
                Alert exitAlert = new Alert(Alert.AlertType.ERROR, "Selectati mai intai un tip de pizza si cantitatea dorita.");
                exitAlert.showAndWait();

            }
            orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuDataModel>(){

                @Override
                public void changed(ObservableValue<? extends MenuDataModel> observable, MenuDataModel oldValue, MenuDataModel newValue){
                    orderTable.getSelectionModel().selectedItemProperty().removeListener(this);
                }
            });
        });

        //Controller for Exit table Button
        newOrder.setOnAction(event -> {
            orderPaymentList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity()*menuDataModel.getPrice())
                    .collect(Collectors.toList());
            setTotalAmount(orderPaymentList.stream().mapToDouble(e->e.doubleValue()).sum()); //DoubleStream::sum()
            if(!isPay && getTotalAmount()>0)
            {
                Alert notPay = new Alert(Alert.AlertType.ERROR, "Nu s-a efectuat achitarea notei.");
                notPay.showAndWait();
            }else {
                Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Exit table?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = exitAlert.showAndWait();
                if ((result.get() == ButtonType.YES && isPay) || (result.get() ==ButtonType.YES && getTotalAmount() == 0)) {
                    Stage stage = (Stage) newOrder.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }
}