package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class ReplaceController {
	@FXML
	private CheckBox isCompareNoCase;
	@FXML
	private TextField searchText;
	@FXML
	private TextField replaceText;
	@FXML
	private Button btn1;
	@FXML
	private Button btn2;
	@FXML
	private Button btn3;
	
	@FXML
	public void initialize() {
		searchText.setText(EditorModel.findContext);
		isCompareNoCase.setSelected(EditorModel.isCompareNoCase);
		btn1.setDisable(searchText.getText().isEmpty());
		btn2.setDisable(searchText.getText().isEmpty());
		btn3.setDisable(searchText.getText().isEmpty());
		searchText.selectAll();
		
		searchText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				btn1.setDisable(newValue.equals(""));
				btn2.setDisable(newValue.equals(""));
				btn3.setDisable(newValue.equals(""));
				EditorModel.findContext = newValue;
			}
		});

		isCompareNoCase.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, 
					Boolean oldValue, Boolean newValue) {
				EditorModel.isCompareNoCase = newValue;
			};
		});
	}

	public void showErrorSearch() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("记事本");
		alert.setHeaderText("找不到");
		alert.setContentText("Ooops, there was an error!");
		alert.showAndWait();
	}

	@FXML
	public void searchNext(ActionEvent event) {
		boolean noCase = isCompareNoCase.isSelected();
		String findContext = searchText.getText();
		SearchController.findNext(EditorModel.textArea, findContext, noCase, false);
	}

	@FXML
	public void replace(ActionEvent event) {
		TextArea textArea = EditorModel.textArea;
		
		boolean noCase = isCompareNoCase.isSelected();
		String findContext = searchText.getText();
		String replaceContext = replaceText.getText();

		if(textArea.getSelectedText().isEmpty()) {
			SearchController.findNext(EditorModel.textArea, findContext, noCase, false);
		}else {
			textArea.replaceText(textArea.getSelection(), replaceContext);
		}
	}

	@FXML
	public void replaceAll(ActionEvent event) {
		TextArea textArea = EditorModel.textArea;
		String text = textArea.getText();
		String findContext = searchText.getText();
		String replaceContext = replaceText.getText();	
		textArea.setText(text.replace(findContext, replaceContext));
	}

	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) btn1.getScene().getWindow();
		stage.close();
	}
}
