package pizzashop.validator;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<ErrorPayment> errors;
    public ValidationException(List<ErrorPayment> errorList) {
        this.errors = errorList;
    }
    public List<ErrorPayment> getErrors(){
        return errors;
    }
}
