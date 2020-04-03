package pizzashop.validator;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

public class ValidatorService {
    public void validate(Payment payment, ValidationResult validationResult)
    {
        if(payment.getTableNumber()>8 || payment.getTableNumber()<1)
            validationResult.addErrorToList(new ErrorPayment("tableNumber","Numarul mesei nu este o valoare din intervalul [1,8]"));
        if(payment.getType()!= PaymentType.CARD && payment.getType()!= PaymentType.CASH)
            validationResult.addErrorToList(new ErrorPayment("type","tipul de plata nu este unul valid"));
        if(payment.getAmount()<0.0)
            validationResult.addErrorToList(new ErrorPayment("amount","valiarea achitata nu este o valoare din intervalul[0.0,infinit)"));

    }
}
