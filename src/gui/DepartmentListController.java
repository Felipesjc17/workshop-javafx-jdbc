package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.service.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service = new DepartmentService();
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
			
	}
	//inicializando comportamento das colunas
	private void initializeNodes() {
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//fazendo a tableView acompanhar tamanho da janela		
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Window super classe de Stage
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		}
	
	public void updateTableView() {
		if (service == null) { //verifica se service foi instânciado / se a dependência foi injetada
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //pegando lista 
		obsList = FXCollections.observableArrayList(list); // passando para obsList
		tableViewDepartment.setItems(obsList); // carregando na tabela
	}

}
