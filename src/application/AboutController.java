package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutController {
	@FXML
	private ImageView headImage;
	@FXML
	private ImageView notepadImage;
	@FXML
	private Button btn;
	
	public void initialize() {
		headImage.setImage(new Image("/images/headImage.jpg"));
		notepadImage.setImage(new Image("/images/notepad.jpg"));
	}
	
	@FXML
	public void closeStage(ActionEvent event) {
		Stage stage = (Stage) btn.getScene().getWindow();
		stage.close();
	}
}
