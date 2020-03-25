package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.IOException;
import java.util.List;

public interface IPizzaService {
    List<MenuDataModel> getMenuData() throws IOException;
    List<Payment> getPayments();
    void addPayment(int table, PaymentType type, double amount);
    double getTotalAmount(PaymentType type);
}
