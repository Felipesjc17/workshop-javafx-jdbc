package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow(); //pegando o estado atual da janela
		
	}
	
	public static Integer tryParceToInt(String str ) { //Se não conseguir converter para int retorna null
		try {
					
		return Integer.parseInt(str);
		
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
}
