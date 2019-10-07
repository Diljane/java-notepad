package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

public class EditorController {

	@FXML
	private TextArea textArea;
	@FXML
	private CheckMenuItem cmi;
	@FXML
	private MenuItem undoItem;
	@FXML
	private MenuItem cutItem;
	@FXML
	private MenuItem copyItem;
	@FXML
	private MenuItem pasteItem;
	@FXML
	private MenuItem deleteItem;
	@FXML
	private MenuItem searchItem;
	@FXML
	private MenuItem gotoItem;
	@FXML
	private CheckMenuItem viewItem;
	@FXML
	private HBox statusBar;
	@FXML
	private Label statusLabel;
	
	private File file;
	public static EditorModel model;
	
	@FXML
	public void initialize() {
		textArea.setText("");
		model = new EditorModel(textArea);
		textArea.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		    	undoItem.setDisable(!textArea.isUndoable());
		    	searchItem.setDisable(textArea.getText().equals(""));
		    }
		});
		
		textArea.selectedTextProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		    	cutItem.setDisable(newValue.equals(""));
		    	copyItem.setDisable(newValue.equals(""));
		    	deleteItem.setDisable(newValue.equals(""));
		    }
		});
		
		textArea.caretPositionProperty().addListener(new ChangeListener<Number>() {
			@Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
				if(oldValue != newValue) {
					statusLabel.setText("第"+model.getRow()+"行，第"+model.getColumn()+"列");
				}
		    }
		});
	}
	
	public void confirm() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("提示");
		alert.setHeaderText("文本内容已有所更改，是否确认保存？");
		alert.setContentText("Are you ok with this?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    model.onFileSave();
		} else {
		    return;
		}
	}
	
	@FXML
	public void onFileNew(ActionEvent event) {
		if(model.isChanged()) {
			confirm();
		}
		Stage stage = (Stage) textArea.getScene().getWindow();
		stage.setTitle("无标题-记事本");
		textArea.setText("");
	}
	
	@FXML
	public void onFileOpen(ActionEvent event) {
		if(model.isChanged()) {
			confirm();
		}
		FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        file = fileChooser.showOpenDialog(null);
        Stage stage = (Stage) textArea.getScene().getWindow();
        if(file != null) {
        	stage.setTitle(file.getName());
        	model.onFileOpen(file);
        }
	}
	
	@FXML
	public void onFileSave(ActionEvent event) {
		if(file == null) {
			return;
		}
		else {
			model.onFileSave();
		}
	}
	
	@FXML
	public void onFileSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		File saveFile = fileChooser.showSaveDialog(null);
		model.setCurrentFile(saveFile);
		Stage stage = (Stage) textArea.getScene().getWindow();
		if(saveFile != null) {
			stage.setTitle(saveFile.getName());
			String text = textArea.getText();
			FileServce.writeFile(text, saveFile);
		}
	}
	
	@FXML
	public void onExit(ActionEvent event) {
		if(model.isChanged()) {
			confirm();
		}
		model.onExit();
	}
	
	@FXML
    public void onAutoWrap(ActionEvent event) {
        cmi = (CheckMenuItem) event.getSource();
        textArea.setWrapText(cmi.isSelected());
        gotoItem.setDisable(cmi.isSelected());
        viewItem.setDisable(cmi.isSelected());
        if(cmi.isSelected()) {
        	statusBar.setVisible(false);
        	viewItem.setSelected(false);
        }
    }
	
    @FXML
    public void onUndo(ActionEvent event) {
        textArea.undo();
    }
	
    @FXML
    public void onCut(ActionEvent event) throws Exception {
        textArea.cut();
    }
	
    @FXML
    public void onCopy(ActionEvent event) {
        textArea.copy();
    }
	
    @FXML
    public void onPaste(ActionEvent event) {
        textArea.paste();
    }
	
    @FXML
    public void onDelete(ActionEvent event) {
        textArea.deleteNextChar();
    }
    @FXML
    public void onSearch(ActionEvent event) {
    	try {
    		Parent root=FXMLLoader.load(getClass().getResource("search.fxml"));
    		Stage newStage = new Stage();
    		Scene scene = new Scene(root);
    		newStage.setScene(scene);
            newStage.setTitle("查找");
            newStage.setResizable(false);
            newStage.getIcons().add(new Image("/images/notepad.jpg"));
    		newStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void onReplace(ActionEvent event) {
    	try { 
    		Parent root=FXMLLoader.load(getClass().getResource("replace.fxml"));
    		Stage newStage = new Stage();
    		Scene scene = new Scene(root);
    		newStage.setScene(scene);
            newStage.setTitle("替换");
            newStage.setResizable(false);
            newStage.getIcons().add(new Image("/images/notepad.jpg"));
    		newStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void onGoto(ActionEvent event) {
    	try { 
    		Parent root=FXMLLoader.load(getClass().getResource("goto.fxml"));
    		Stage newStage = new Stage();
    		Scene scene = new Scene(root);
    		newStage.setScene(scene);
            newStage.setTitle("转到指定行");
            newStage.setResizable(false);
            newStage.getIcons().add(new Image("/images/notepad.jpg"));
    		newStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void onSelectAll(ActionEvent event) {
    	textArea.selectAll();
    }
    
    @FXML
    public void showDate(ActionEvent event) {
    	model.showDate();
    }
    
    @FXML
    public void showFont(ActionEvent event) {
    	try {
    		Parent root=FXMLLoader.load(getClass().getResource("font.fxml"));
    		Stage newStage = new Stage();
    		Scene scene = new Scene(root);
    		newStage.setScene(scene);
            newStage.setTitle("字体");
            newStage.setResizable(false);
            newStage.getIcons().add(new Image("/images/notepad.jpg"));
    		newStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void showStatus(ActionEvent event) {
    	CheckMenuItem cmi = (CheckMenuItem) event.getSource();
    	statusBar.setVisible(cmi.isSelected());
    }

    @FXML
    public void onHelp(ActionEvent event) throws IOException, URISyntaxException {
    	Desktop.getDesktop().browse(new URI("https://cn.bing.com/search?q=%e8%8e%b7%e5%8f%96%e"
    			+ "6%9c%89%e5%85%b3+windows+10+%e4%b8%ad%e7%9a%84%e8%ae%b0%e4%ba%8b%e6%9c%ac%e7%9a"
    			+ "%84%e5%b8%ae%e5%8a%a9&filters=guid:%224466414-zh-hans-dia%22%20lang:%22zh-hans%"
    			+ "22&form=T00032&ocid=HelpPane-BingIA"));
    }   
    
    @FXML
    public void onAboutEditor(ActionEvent event) {
    	try { 
    		Parent root=FXMLLoader.load(getClass().getResource("about.fxml"));
    		Stage newStage = new Stage();
    		Scene scene = new Scene(root);
    		newStage.setScene(scene);
            newStage.setTitle("关于\"记事本\"");
            newStage.setResizable(false);
            newStage.getIcons().add(new Image("/images/notepad.jpg"));
    		newStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
