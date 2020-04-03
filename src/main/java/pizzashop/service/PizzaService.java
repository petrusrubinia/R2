package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.validator.ValidationResult;
import pizzashop.validator.ValidatorService;

import java.io.IOException;
import java.util.List;

public class PizzaService implements IPizzaService{

    private MenuRepository menuRepo;
    private PaymentRepository payRepo;
    private ValidatorService validator;

    public PizzaService(MenuRepository menuRepo, PaymentRepository payRepo, ValidatorService validator){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
        this.validator = validator;
    }

    public List<MenuDataModel> getMenuData() throws IOException {return menuRepo.getMenu();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public void addPayment(int table, PaymentType type, double amount){
        ValidationResult validationResult = new ValidationResult();
        Payment payment= new Payment(table, type, amount);
        validator.validate(payment,validationResult);
        validationResult.rejectIfHasErrors();
        payRepo.add(payment);
    }

    public double getTotalAmount(PaymentType type){
        double total=0.0f;
        List<Payment> l=getPayments();
        if (l==null) return total;
        if (l.isEmpty()) return total;
        for (Payment p:l){
            if (p.getType().equals(type))
                total+=p.getAmount();
        }
        return total;
    }

}
