package pizzashop.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import pizzashop.controller.KitchenGUIController;

import java.io.IOException;
import java.util.Optional;


public class KitchenGUI {

    public void kitchenGUIMethod() throws IOException {
        FXMLLoader loader = null;

        loader = new FXMLLoader(getClass().getResource("/fxml/kitchenGUIFXML.fxml"));
        Parent root = loader.load();
        KitchenGUIController controller = loader.getController();



        Stage stage = new Stage();
        stage.setTitle("Kitchen");
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to exit Kitchen window?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                                   stage.close();
            }
            // consume event
            else if (result.get() == ButtonType.NO){
                event.consume();
            }
            else {
                event.consume();
            }
        });


        stage.setAlwaysOnTop(false);
        stage.setScene(new Scene(root));
        final boolean[] result = {false};
        stage.setOnHidden(e-> {result[0] =controller.shutdown();
            if (result[0])
                Platform.exit();



        });

        stage.show();
        stage.toBack();
    }
}

