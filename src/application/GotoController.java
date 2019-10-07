package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

public class GotoController {
	@FXML
	private TextField rowTextField;
	
	@FXML
	public void initialize() {
		rowTextField.setText("1");
		rowTextField.selectAll();
	}
	
	@FXML
	public void gotoRow(ActionEvent event) {
		int inputRow = Integer.parseInt(rowTextField.getText());
		EditorModel.curRow = inputRow;
		if(inputRow > EditorController.model.getAllRow() || inputRow < 1) {
			EditorController.model.showErrorCaret();
			rowTextField.setText(String.valueOf(EditorController.model.getAllRow()));
			rowTextField.selectAll();
		}
		else {
			EditorController.model.setCaret();
			Stage stage = ((Stage)rowTextField.getScene().getWindow());
			stage.close();
		}
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = ((Stage)rowTextField.getScene().getWindow());
		stage.close();
	}
}
