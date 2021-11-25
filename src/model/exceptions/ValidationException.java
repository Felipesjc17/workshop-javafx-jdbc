package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>(); //guardando erros poss�veis em uma cole��o, Chave = nome / valor = tipo de erro que poder� acontecer em cada campo
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){ 
		return errors;
	}
	
	//adicionando erros na cole��o
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}

}
