package pizzashop.repository;

import pizzashop.model.Payment;

import java.util.List;

public interface IPaymentRepository {
    void readPayments();
    Payment getPayment(String line);
    void add(Payment payment);
    List<Payment> getAll();
    void writeAll();
}
