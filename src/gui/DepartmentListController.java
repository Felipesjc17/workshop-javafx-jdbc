package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); //passando estado do Stage atual
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage); //criando DialogForm
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
		if (service == null) { //verifica se service foi instanciado / se a depend�ncia foi injetada
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //pegando lista 
		obsList = FXCollections.observableArrayList(list); // passando para obsList
		tableViewDepartment.setItems(obsList); // carregando na tabela
		
		}

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); //carregando tela
			Pane pane = loader.load(); // carregando view em pane
			
			DepartmentFormController controller = loader.getController(); //pegando controlador da tela carregada
			controller.setDepartment(obj); //injetando obj do department no controlador
			controller.updateFormData(); //carregando dados do obj vazio no formulario 
			
			Stage dialogStage = new Stage(); // instanciando novo stage para aparecer na frente do stage principal(palco)
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane)); // carregando nova scene
			dialogStage.setResizable(false); //Janela n�o pode ser redimensionada
			dialogStage.initOwner(parentStage); //Stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);//comportamento da janela, travada, n�o pode acessar janela anterior at� fech�-la
			dialogStage.showAndWait();//chamando a janela para preencher novo department
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR); 
		}
	}
	
}
