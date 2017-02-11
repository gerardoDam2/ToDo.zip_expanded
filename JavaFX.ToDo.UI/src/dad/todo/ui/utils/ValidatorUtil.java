package dad.todo.ui.utils;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;

import javafx.scene.control.Control;

public class ValidatorUtil {

	public static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	
	
	
	public static ValidationResult checkEmail(Control control, String content) {
        boolean condition = !content.matches(EMAIL_PATTERN);
        return ValidationResult.fromMessageIf(control, "formato de email incorrecto", Severity.ERROR, condition);            
    }
	
}
