package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable{

	
	private Department entity;
	
	private DepartmentService service;
	
	////emitindo evento // lista de objetos interessados em receber o evento de mudan?a
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	public void setDepartment(Department entity) {
		 this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChanceListener(DataChangeListener listener) { //metodo para se inscrever na lista que recebera os eventos
		dataChangeListeners.add(listener);
		
	}
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");//erro de inje??o de depend?ncia
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try { //alerta de erro com BD e erro nos campos do formul?rio
			
		entity = getFormData();//pegar info da Label e instanciar novo obj e passa para entity
		service.saveOrUpdate(entity);//salva novo obj no obj DepartmentService
		notifyDataChangeListeners(); //Notificando para lista que um evento ocorreu (subject)
		Utils.currentStage(event).close();//pega janela e fecha ap?s acionar bot?o
		
		}
		catch (ValidationException e) { //tratando valida??o de erros na digita??o do formul?rio
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {  // tratando erro no banco
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() { //emitindo evento para cada um da lista (subject)
		for (DataChangeListener listener : dataChangeListeners) { //varrendo lista
			listener.onDataChanged(); //notificando todos 
		}
		
	}

	private Department getFormData() {//pega os dados do formulario instancia obj e passa os dados para esse obj
		Department obj = new Department();		
		
		ValidationException exception = new ValidationException("Validation error"); //instanciando valida??o
		
		obj.setId(Utils.tryParceToInt(txtId.getText()));//passando o Id da caixa para obj j? convertido para inteiro ou null se for vazio tratado pela fun??o tryParceToInt
		
		//se txt name tiver espa?o no come?o ou final (trim) ou Equals (" ") String vazia, lan?a exce??o
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty"); 
			}
			
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size() > 0) { //testando se a cole??o tem erro, se tiver lan?a o erro
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId); // recebendo apenas n?meros inteiros
		Constraints.setTextFieldMaxLength(txtName, 30); // recebe at? 30 caracteres 
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
			}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
	
	private void setErrorMessages(Map<String, String> errors){ 
		Set<String> fields = errors.keySet(); //pegando chave do erro
		
		if(fields.contains("name")) { // verificando se a chave ? name, confirmando o erro
			labelErrorName.setText(errors.get("name")); //passando conteudo da chave name para o campo de erro da janela
		}
	}

}
