package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>(); //guardando erros possíveis em uma coleção, Chave = nome / valor = tipo de erro que poderá acontecer em cada campo
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){ 
		return errors;
	}
	
	//adicionando erros na coleção
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}

}
