package application;

import javafx.fxml.FXML;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class SearchController {
	@FXML
	private Button btn1;
	@FXML
	private CheckBox isCompareNoCase;
	@FXML
	private RadioButton upRadioBtn;
	@FXML
	private RadioButton downRadioBtn;
	@FXML
	private ToggleGroup directionToggleGroup;
	@FXML
	private TextField searchText;

	public static void showErrorSearch() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("记事本");
		alert.setHeaderText("找不到");
		alert.setContentText("Ooops, there was an error!");
		alert.showAndWait();
	}
	
	public static void findNext(TextArea textArea, String findContext, boolean isCompareNoCase, boolean up) {
		String text = textArea.getText();
		if(text.isEmpty() || findContext.isEmpty()) {
			return;
		}
		if(!isCompareNoCase) {
			text = text.toLowerCase();
			findContext = findContext.toLowerCase();
		}
		if(up) {
			text = new StringBuilder(text).reverse().toString();
			findContext = new StringBuilder(findContext).reverse().toString();
		}
		if(text.indexOf(findContext) == -1) {
			showErrorSearch();
			return;
		}
		int fromIndex = up ? text.length() - textArea.getSelection().getStart():textArea.getSelection().getEnd();
		int res = text.indexOf(findContext, fromIndex);
		
		if(res == -1) {
			showErrorSearch();
			return;
		}
		
		res = up ? text.length() - res - findContext.length():res;
		textArea.selectRange(res, res + findContext.length());
	}
	
	@FXML
	public void initialize() {
		searchText.setText(EditorModel.findContext);
		isCompareNoCase.setSelected(EditorModel.isCompareNoCase);
		directionToggleGroup.selectToggle(EditorModel.up ? upRadioBtn:downRadioBtn);
		
		upRadioBtn.setUserData("up");
		downRadioBtn.setUserData("down");
		
		btn1.setDisable(true);
		searchText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				btn1.setDisable(newValue.isEmpty());
				EditorModel.findContext = newValue;
			}
		});
		
		isCompareNoCase.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, 
					Boolean oldValue, Boolean newValue) {
				EditorModel.isCompareNoCase = newValue;
			}
		});
		
		directionToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, 
					Toggle oldValue, Toggle newValue) {
				EditorModel.up = newValue.getUserData().equals("up");
			}
		});
	}
	
	@FXML
	public void searchNext(ActionEvent event) {
		boolean noCase = isCompareNoCase.isSelected();
		boolean up = directionToggleGroup.getSelectedToggle().getUserData().equals("up");
		String findContext = searchText.getText();
		
		findNext(EditorModel.textArea, findContext, noCase, up);
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) searchText.getScene().getWindow();
		stage.close();
	}
}
