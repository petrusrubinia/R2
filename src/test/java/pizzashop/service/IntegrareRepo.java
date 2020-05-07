package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidatorService;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntegrareRepo {
    //repo
    private MenuRepository menuRepository;
    private PaymentRepository paymentRepository;
    //validator
    private ValidatorService validatorService;
    //service
    private PizzaService service;
    //domain
    private Payment payment;
    private PaymentType type;
    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        validatorService = mock(ValidatorService.class);
        paymentRepository = new PaymentRepository("data/paymentsTestMock1.txt");
        service = new PizzaService(menuRepository,paymentRepository,validatorService);
        payment = mock(Payment.class);
        when(payment.getTableNumber()).thenReturn(2);
        when(payment.getType()).thenReturn(PaymentType.CARD);
        when(payment.getAmount()).thenReturn(20.0);
    }


    @AfterEach
    void tearDown() {
        System.out.println("------------------------------------------------------------------------------------------");
    }

    @Test
    public void test1()
    {
        //arrange
        int old_size = service.getPayments().size();

        //act
        service.addPayment(payment);

        //assert
        assert old_size+1 == service.getPayments().size();
        Mockito.verify(payment, times(1)).getAmount();
        Mockito.verify(payment,times(1)).getTableNumber();
    }

    @Test
    public void test2()
    {
        //arrange
        type = PaymentType.CASH;
        //act
        double rezult = service.getTotalAmount(type);

        //assert
        assert rezult == 0;


    }

}