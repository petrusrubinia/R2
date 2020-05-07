package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidationException;
import pizzashop.validator.ValidatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class PizzaServiceTest {
    private PaymentType cashType;
    private PaymentType cardType;
    private List<Integer> tableList;
    private double amount;
    private PizzaService pizzaService;
    private MenuRepository menuRepo;
    private PaymentRepository payRepo;
    private ValidatorService validatorService;
    private int size;

    @BeforeEach
    void setUp() {

        menuRepo = new MenuRepository( "data/menu.txt");
        size =0;
        payRepo = new PaymentRepository("data/payments.txt");
        validatorService = new ValidatorService();
        pizzaService = new PizzaService(menuRepo, payRepo,validatorService);
        cashType = PaymentType.CASH;
        cardType = PaymentType.CARD;
        tableList = new ArrayList<Integer>(){
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
                add(7);
                add(8);
            }
        };
        amount = 0.0;

    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8})
    void TC1_EC(int table)
    {
        amount = 24.0;
        size = payRepo.getAll().size();
        pizzaService.addPayment(new Payment(table,cashType,amount));
        assertEquals(size+1 , payRepo.getAll().size(), "dimensiunea nu coincide ");
    }


    @RepeatedTest(2)
    void TC2_EC() //trebuie sa cada!
    {
        ValidationException validationException = assertThrows(ValidationException.class, () ->{
            pizzaService.addPayment(new Payment(30, cardType, amount));
        });
        System.out.println("TC2_EC: TABLE : 30" + validationException.getErrors());
    }
    @Test
    @DisplayName("errAmount")
    void TC3_EC() //trebuie sa cada!!!
    {
        ValidationException validationException = assertThrows(ValidationException.class, () ->{
            pizzaService.addPayment(new Payment(tableList.get(3),cashType,-30));
        });
        System.out.println("TC3_EC: AMOUNT -30" + validationException.getErrors());

    }

    @Test
    void TC4_EC()
    {
        size = payRepo.getAll().size();
        amount = 0.0;
        pizzaService.addPayment(new Payment(tableList.get(2),cardType,0.0));
        assertEquals(size+1 , payRepo.getAll().size(), "dimensiunea nu coincide ");

    }
    @Disabled
    void Test_DISABLED()
    {
        System.out.println("disabled test");
    }

    @Test
    void TC1_BVA()
    {
        size = payRepo.getAll().size();
        amount = 0.0;
        pizzaService.addPayment(new Payment(tableList.get(1),cashType,30.0));
        assertEquals(size+1 , payRepo.getAll().size(), "dimensiunea nu coincide ");

    }

    @Test
    void TC2_BVA()
    {
        size = payRepo.getAll().size();
        amount = 16.0;
        pizzaService.addPayment(new Payment(tableList.get(3),cardType,0.0));
        assertEquals(size+1 , payRepo.getAll().size(), "dimensiunea nu coincide ");
    }
    @Test
    void TC3_BVA() //trebuie sa cada
    {
        amount = 10.0;
        ValidationException validationException = assertThrows(ValidationException.class, () ->{
            pizzaService.addPayment(new Payment(9,cardType,amount));
        });
        System.out.println("TC3_BVA : TABLE = 9"+ validationException.getErrors());

    }
    @Test
    void TC11_BVA() //trebuie sa cada
    {

        amount = -0.1;
        ValidationException validationException = assertThrows(ValidationException.class, () ->{
            pizzaService.addPayment(new Payment(9,cardType,amount));
        });
        System.out.println("TC11_BVA : AMOUNT = -0.1"+ validationException.getErrors());

    }



    @AfterEach
    void tearDown() {
        System.out.println("------------------------------------------------------------------------------------------");
    }
}