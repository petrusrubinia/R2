package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.IMenuRepository;
import pizzashop.repository.IPaymentRepository;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidatorService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class PizzaServiceTestWithMock {
    //repo
    private MenuRepository menuRepository;
    private PaymentRepository paymentRepository;
    //validator
    private ValidatorService validatorService;
    //service
    private PizzaService service;

    //domain
    private MenuDataModel model;
    private Payment payment;

    @BeforeEach
    void setUp() {
        //init repo
        menuRepository = mock(MenuRepository.class);
        paymentRepository = mock(PaymentRepository.class);
        //init validator
        validatorService = mock(ValidatorService.class);
        //init domain
        model = mock(MenuDataModel.class);
        payment = mock(Payment.class);

        //service
        service = new PizzaService(menuRepository,paymentRepository,validatorService);
    }

    @AfterEach
    void tearDown() {
        System.out.println("------------------------------------------------------------------------------------------");
    }
    @Test
    public void test_getMenuData() throws IOException {
        //arrange
        when(model.getQuantity()).thenReturn(4);
        when(model.getPrice()).thenReturn(20.0);
        when(model.getMenuItem()).thenReturn("Margherita");

        //act
        try {
            Mockito.when(menuRepository.getMenu()).thenReturn(Arrays.asList(model));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //assert
        assert 1 == service.getMenuData().size();
        Mockito.verify(menuRepository).getMenu();
        Mockito.verify(menuRepository, times(1)).getMenu();
    }

    @Test
    public void test_addPayment(){
        //arrange
        when(payment.getTableNumber()).thenReturn(2);
        when(payment.getType()).thenReturn(PaymentType.CARD);
        when(payment.getAmount()).thenReturn(20.0);

        //act
        Mockito.when(paymentRepository.getAll()).thenReturn(Arrays.asList(payment));
        service.addPayment(payment);
        Mockito.verify(paymentRepository, never()).getAll();

       //assert
        assert 1 == service.getPayments().size();
        Mockito.verify(paymentRepository, times(1)).add(payment);
        Mockito.verify(paymentRepository, times(1)).getAll();



    }
}