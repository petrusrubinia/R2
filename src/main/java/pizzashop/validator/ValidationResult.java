package pizzashop.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private List<ErrorPayment> err;


    public ValidationResult() {
        this.err = new ArrayList<>();
    }

    public boolean hasError()
    {
        return (err.size() != 0);
    }
    public void addErrorToList(ErrorPayment errorPayment)
    {
        err.add(errorPayment);
    }
    public List<ErrorPayment> getErrors()
    {
        return this.err;
    }
    public void rejectIfHasErrors()
    {
        if(hasError())
            throw new ValidationException(getErrors());

    }
}
