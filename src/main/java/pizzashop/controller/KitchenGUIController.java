package pizzashop.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.apache.log4j.Logger;

import java.util.Calendar;


public class KitchenGUIController {
    @FXML
    private ListView kitchenOrdersList;
    @FXML
    public Button cook;
    @FXML
    public Button ready;
    static Logger log = Logger.getLogger(KitchenGUIController.class.getName());

    protected  static final ObservableList<String> order = FXCollections.observableArrayList();
    private Object selectedOrder;
    private Calendar now = Calendar.getInstance();
    private String extractedTableNumberString = "";
    private int extractedTableNumberInteger;
    //thread for adding data to kitchenOrderList
    protected  Thread addOrders = new Thread(()->{
        while (true) {
            Platform.runLater(() -> kitchenOrdersList.setItems(order));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;

            }

        }
    });

    public void initialize() {
        //starting thread for adding data to kitchenOrderList
        addOrders.setDaemon(true);
        addOrders.start();
        //Controller for Cook Button
        cook.setOnAction(event -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            kitchenOrdersList.getItems().remove(selectedOrder);
            try {
                kitchenOrdersList.getItems().add(selectedOrder.toString()
                        .concat(" Cooking started at: ").toUpperCase()
                        .concat(now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE)));
            }catch (Exception e){
                log.debug("Trebuie sa selectati mai intai o comanda pentru a putea accesa acest buton!");
                Alert exitAlert = new Alert(Alert.AlertType.ERROR, "Trebuie sa selectati mai intai o comanda pentru a putea accesa acest buton.");
                exitAlert.showAndWait();
            }
        });
        //Controller for Ready Button
        ready.setOnAction(event -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            kitchenOrdersList.getItems().remove(selectedOrder);

            try {
                extractedTableNumberString = selectedOrder.toString().subSequence(5, 6).toString();
                extractedTableNumberInteger = Integer.valueOf(extractedTableNumberString);
                log.debug("--------------------------");
                log.debug("Table " + extractedTableNumberInteger +" ready at: " + now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));
                log.debug("--------------------------");
            }catch (Exception e)
            {
                log.debug("Trebuie sa selectati mai intai o comanda pentru a putea accesa acest buton!");
                Alert exitAlert = new Alert(Alert.AlertType.ERROR, "Trebuie sa selectati mai intai o comanda pentru a putea accesa acest buton.");
                exitAlert.showAndWait();
            }


        });

    }
    public boolean shutdown() {
        return false;

    }
}