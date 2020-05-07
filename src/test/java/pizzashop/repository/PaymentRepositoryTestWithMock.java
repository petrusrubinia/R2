package pizzashop.repository;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentRepositoryTestWithMock {
    private PaymentRepository paymentRepository;
    private Throwable thrown;
    private Payment payment;


    @BeforeEach
    void setUp() {

        payment = mock(Payment.class);
        paymentRepository = new PaymentRepository("data/paymentsTestMock.txt");
        when(payment.getTableNumber()).thenReturn(2);
        when(payment.getType()).thenReturn(PaymentType.CARD);
        when(payment.getAmount()).thenReturn(20.0);
    }


    @AfterEach
    void tearDown() {
        System.out.println("------------------------------------------------------------------------------------------");
    }

    @Test
    public void test_size()
    {
        //arrange
        String line = "2,CARD,203.0";
        when(payment.getTableNumber()).thenReturn(2);
        when(payment.getType()).thenReturn(PaymentType.CARD);
        when(payment.getAmount()).thenReturn(20.0);
        int old_size = paymentRepository.getAll().size();

        //act
        paymentRepository.add(payment);
        int new_size = paymentRepository.getAll().size();

        //assert
        assert old_size+1 == new_size;
        Mockito.verify(payment, times(1)).getType();
    }

    @Test
    public void test_add()
    {
        int old_size = paymentRepository.getAll().size();
        //act
        paymentRepository.add(payment);
        int new_size = paymentRepository.getAll().size();

        //assert
        assert old_size+1 == new_size;
        Mockito.verify(payment, times(1)).getAmount();
        Mockito.verify(payment,times(1)).getTableNumber();
    }

}