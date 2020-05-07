package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidationException;
import pizzashop.validator.ValidatorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestGetTotalAmount {
    private PaymentType paymentType;
    private PizzaService pizzaService;
    private PizzaService emptyPaymentService;
    private PizzaService cashPaymentService;
    private ValidatorService validatorService;
    private PaymentRepository payRepo;
    private PaymentRepository emptyPaymentRepo;
    private PaymentRepository cashPaymentRepo;
    private MenuRepository menuRepo;
    private double actualValue,oldValue;
    private double expectedValue;
    private double addValue;
    private Payment payment;

    @BeforeEach
    void setUp() {
        menuRepo = new MenuRepository("data/menu.txt");
        payRepo = new PaymentRepository("data/paymentsTest.txt");
        validatorService = new ValidatorService();
        pizzaService = new PizzaService(menuRepo, payRepo,validatorService);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getTotalAmount() {
    }

    @Test
    void F02_TC01(){
        paymentType = PaymentType.CASH;
        addValue = 24.0;

        oldValue = pizzaService.getTotalAmount(paymentType);
        expectedValue = oldValue + addValue;
        pizzaService.addPayment(new Payment(1,paymentType,addValue));
        actualValue = pizzaService.getTotalAmount(paymentType);


        assertEquals(expectedValue, actualValue, "suma totala nu se calculeaza corect");
    }

    @Test
    void F02_TC02(){
        emptyPaymentRepo = new PaymentRepository("data/emptyPaymentsTest.txt");
        emptyPaymentService = new PizzaService(menuRepo,emptyPaymentRepo,validatorService);
        paymentType = PaymentType.CASH;

        expectedValue = 0;
        actualValue = emptyPaymentService.getTotalAmount(paymentType);


        assertEquals(expectedValue,actualValue,"se ignora lista goala");
    }

    @Test
    void F02_TC03(){
        emptyPaymentRepo = new PaymentRepository("data/emptyPaymentsTest.txt", null);
        emptyPaymentService = new PizzaService(menuRepo,emptyPaymentRepo,validatorService);
        paymentType = PaymentType.CARD;

        expectedValue = 0;
        actualValue = emptyPaymentService.getTotalAmount(paymentType);


        assertEquals(expectedValue,actualValue,"se ignora lista nulla");

    }

    @Test
    void F02_TC04(){
        cashPaymentRepo = new PaymentRepository("data/justCashPayment.txt");
        cashPaymentService = new PizzaService(menuRepo,cashPaymentRepo,validatorService);
        paymentType = PaymentType.CARD;

        expectedValue = 0;
        actualValue = cashPaymentService.getTotalAmount(paymentType);


        assertEquals(expectedValue,actualValue,"nu exista plati de tipul CARD");


    }

    @Test
    void F02_TC06(){
        emptyPaymentRepo = new PaymentRepository("data/emptyPaymentsTest.txt");
        emptyPaymentService = new PizzaService(menuRepo,emptyPaymentRepo,validatorService);
        paymentType = PaymentType.NUMERAR;

        expectedValue = 0;

        try{
            actualValue = emptyPaymentService.getTotalAmount(paymentType);
        }catch (ValidationException ve)
        {
            assertEquals(ve.getErrors().get(0).getMessage(),"tipul de plata nu este unul valid");
        }


    }
}