package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable{

	
	private Department entity;
	
	private DepartmentService service;
	
	public void setDepartment(Department entity) {
		 this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
			throw new IllegalStateException("Entity was null");//erro de injeção de dependência
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try { //alerta de erro com BD
		entity = getFormData();//pegar info da Label e instanciar novo obj e passa para entity
		service.saveOrUpdate(entity);//salva novo obj no obj DepartmentService
		Utils.currentStage(event).close();//pega janela e fecha após acionar botão
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() {//pega os dados do formulario instancia obj e passa os dados para esse obj
		Department obj = new Department();		
		obj.setId(Utils.tryParceToInt(txtId.getText()));//passando o Id da caixa para obj já convertido para inteiro ou null se for vazio tratado pela função tryParceToInt
		obj.setName(txtName.getText());
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
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
			}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
		
		
	}

}
