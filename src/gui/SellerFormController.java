package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.SellerService;

public class SellerFormController implements Initializable{

	
	private Seller entity;
	
	private SellerService service;
	
	////emitindo evento // lista de objetos interessados em receber o evento de mudança
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	public void setSeller(Seller entity) {
		 this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
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
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");//erro de injeção de dependência
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try { //alerta de erro com BD e erro nos campos do formulário
			
		entity = getFormData();//pegar info da Label e instanciar novo obj e passa para entity
		service.saveOrUpdate(entity);//salva novo obj no obj SellerService
		notifyDataChangeListeners(); //Notificando para lista que um evento ocorreu (subject)
		Utils.currentStage(event).close();//pega janela e fecha após acionar botão
		
		}
		catch (ValidationException e) { //tratando validação de erros na digitação do formulário
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

	private Seller getFormData() {//pega os dados do formulario instancia obj e passa os dados para esse obj
		Seller obj = new Seller();		
		
		ValidationException exception = new ValidationException("Validation error"); //instanciando validação
		
		obj.setId(Utils.tryParceToInt(txtId.getText()));//passando o Id da caixa para obj já convertido para inteiro ou null se for vazio tratado pela função tryParceToInt
		
		//se txt name tiver espaço no começo ou final (trim) ou Equals (" ") String vazia, lança exceção
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty"); 
			}
			
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size() > 0) { //testando se a coleção tem erro, se tiver lança o erro
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
		Constraints.setTextFieldInteger(txtId); // recebendo apenas números inteiros
		Constraints.setTextFieldMaxLength(txtName, 30); // recebe até 30 caracteres 
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
			}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate() != null) {
		dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}
	
	private void setErrorMessages(Map<String, String> errors){ 
		Set<String> fields = errors.keySet(); //pegando chave do erro
		
		if(fields.contains("name")) { // verificando se a chave é name, confirmando o erro
			labelErrorName.setText(errors.get("name")); //passando conteudo da chave name para o campo de erro da janela
		}
	}

}
