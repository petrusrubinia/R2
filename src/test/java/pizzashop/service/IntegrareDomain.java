package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntegrareDomain {
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
    private MenuDataModel menu1 ;
    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        validatorService = mock(ValidatorService.class);
        paymentRepository = new PaymentRepository("data/paymentsTestMock.txt");
        service = new PizzaService(menuRepository, paymentRepository, validatorService);
        payment = new Payment(2,PaymentType.CASH,30.0);
    }


    @AfterEach
    void tearDown() {
        System.out.println("------------------------------------------------------------------------------------------");
    }

    /*
        testare functie pentru repo-ul de tip mock
     */
    @Test
    public void test1() throws IOException {
        //arrange
        menu1 =new MenuDataModel("Margherita",0,8.00);

        //act
        try {
            Mockito.when(menuRepository.getMenu()).thenReturn(Arrays.asList(menu1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MenuDataModel> list = service.getMenuData();

        //assert
        assert 1 == list.size();
        assert menu1.getPrice().equals(list.get(list.size() - 1).getPrice());
        assert menu1.getQuantity().equals(list.get(list.size() - 1).getQuantity());
        assert menu1.getMenuItem().equals(list.get(list.size() - 1).getMenuItem());
        Mockito.verify(menuRepository).getMenu();
        Mockito.verify(menuRepository, times(1)).getMenu();
    }
    /*
        pentru aceasta functie nu mai exista obiecte mock
    */
    @Test
    public void test2()
    {
        //arrange
        payment = new Payment(4,PaymentType.CASH,33.0);


        //action
        paymentRepository.add(payment);
        List<Payment> list = paymentRepository.getAll();

        //assert

        assert list.get(list.size()-1).getTableNumber() == payment.getTableNumber();
        assert list.get(list.size()-1).getAmount() == payment.getAmount();

    }
}