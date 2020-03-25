package pizzashop.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;
import pizzashop.model.PaymentType;

import java.util.Optional;

public class PaymentAlert implements PaymentOperation {
    private PizzaService service;
    static Logger log = Logger.getLogger(PaymentAlert.class.getName());
    public PaymentAlert(PizzaService service){
        this.service=service;
    }

    @Override
    public void cardPayment() {
        String str="---";
        log.debug(str);
        log.debug("Paying by card...");
        log.debug("Please insert your card!");
        log.debug(str);
    }
    @Override
    public void cashPayment() {
        String str="---";
        log.debug(str);
        log.debug("Paying cash...");
        log.debug("Please show the cash...!");
        log.debug(str);
    }
    @Override
    public void cancelPayment() {
        log.debug("--------------------------");
        log.debug("Payment choice needed...");
        log.debug("--------------------------");
    }
      public String showPaymentAlert(int tableNumber, double totalAmount ) {
        Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
        paymentAlert.setTitle("Payment for Table "+tableNumber);
        paymentAlert.setHeaderText("Total amount: " + totalAmount);
        paymentAlert.setContentText("Please choose payment option");
        ButtonType cardPayment = new ButtonType("Pay by Card");
        ButtonType cashPayment = new ButtonType("Pay Cash");
        String str="Cancel";
        ButtonType cancel = new ButtonType(str);
        paymentAlert.getButtonTypes().setAll(cardPayment, cashPayment, cancel);
        Optional<ButtonType> result = paymentAlert.showAndWait();
        if (!result.isPresent()){
            return str;
        }
        if (result.get() == cardPayment) {
            cardPayment();
            service.addPayment(tableNumber, PaymentType.CASH,totalAmount);
            return "CARD";
        } else if (result.get() == cashPayment) {
            cashPayment();
            service.addPayment(tableNumber, PaymentType.CARD,totalAmount);
            return "CASH";
        } else {
            cancelPayment();
            return str;
        }
    }
}
