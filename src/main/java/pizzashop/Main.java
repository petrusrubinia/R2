package pizzashop;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import pizzashop.controller.MainGUIController;
import pizzashop.gui.KitchenGUI;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;
import pizzashop.validator.ValidatorService;


import java.util.Optional;

public class Main extends Application {
    static Logger log = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception{

        MenuRepository repoMenu=new MenuRepository("data/menu.txt");
        PaymentRepository payRepo= new PaymentRepository("data/payments.txt");
        ValidatorService validatorService = new ValidatorService();
        PizzaService service = new PizzaService(repoMenu, payRepo,validatorService);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainFXML.fxml"));

        Parent box = loader.load();
        MainGUIController ctrl = loader.getController();
        ctrl.setService(service);
        primaryStage.setTitle("PizeriaX");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setOnCloseRequest(event -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to exit the Main window?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                //trebuie sa verificam si daca  nu mai existe mese la care se serveste!!

                log.debug("Incasari cash: "+service.getTotalAmount(PaymentType.CASH));
                log.debug("Incasari card: "+service.getTotalAmount(PaymentType.CARD));

                primaryStage.close();
            }
            // consume event
            else if (result.get() == ButtonType.NO){
                event.consume();
            }
            else {
                event.consume();

            }

        });
        primaryStage.setScene(new Scene(box));
        primaryStage.show();
        KitchenGUI kitchenGUI = new KitchenGUI();
        kitchenGUI.kitchenGUIMethod();
    }

    public static void main(String[] args) { launch(args);
    }
}
