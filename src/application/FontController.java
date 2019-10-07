package application;

import javafx.fxml.FXML;

import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FontController {
	@FXML
	private ListView<String> fontList;
	@FXML
	private ListView<String> styleList;
	@FXML
	private ListView<String> sizeList;
	@FXML
	private Label showLabel;
	
	private ObservableList<String> fontStyles = FXCollections.observableArrayList();
	
	private Map<String, Integer> sizeMap = new HashMap<String, Integer>();
	private Map<String, Integer> fontMap = new HashMap<String, Integer>();
	private String tempFamily;
	private FontWeight tempWeight;
	private FontPosture tempPosture;
	private int tempSize;

	@FXML
	public void initialize() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    String[] fontNames = e.getAvailableFontFamilyNames();
	    fontList.setItems(FXCollections.observableArrayList(fontNames));
	    for(int i = 0;i < fontList.getItems().size();i++) {
	    	fontMap.put(fontNames[i], i);
	    }
	    fontList.getSelectionModel().select("宋体");
	    fontList.scrollTo(fontMap.get("宋体"));
		
		fontStyles.add("常规");
		fontStyles.add("倾斜");
		fontStyles.add("粗体");
		fontStyles.add("粗偏斜体");
		styleList.getSelectionModel().select("常规");
	    
		styleList.setItems(fontStyles);
		
		String sizeStr[] = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22",
                "24", "26", "28", "36", "48", "72", "初号", "小初", "一号", "小一", "二号", "小二",
                "三号", "小三", "四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号" };

        int sizeValue[] = { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72,
        		42, 36, 26, 24, 22, 18, 16, 15, 14, 12, 11, 9, 8, 7, 6, 5 };
        
        sizeList.setItems(FXCollections.observableArrayList(sizeStr));
        for(int i = 0;i < sizeList.getItems().size();i++) {
        	sizeMap.put(sizeStr[i], sizeValue[i]);
        }
        sizeList.getSelectionModel().select(5);
        
        fontList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
	    	@Override
	    	public void changed(ObservableValue<? extends String> observable, 
	    			String oldValue, String newValue) {
	    		tempFamily = newValue;
	    		showLabel.setFont(Font.font(tempFamily,tempWeight,tempPosture,tempSize));
	    	}
		});
        
        styleList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
	    	@Override
	    	public void changed(ObservableValue<? extends String> observable, 
	    			String oldValue, String newValue) {
	    		switch (newValue) {
				case "常规":
					tempWeight = FontWeight.NORMAL;
					tempPosture = FontPosture.REGULAR;
					break;
				case "倾斜":
					tempWeight = FontWeight.NORMAL;
					tempPosture = FontPosture.ITALIC;
				case "粗体":
					tempWeight = FontWeight.BOLD;
					tempPosture = FontPosture.REGULAR;
				case "粗偏斜体":
					tempWeight = FontWeight.BOLD;
					tempPosture = FontPosture.ITALIC;
				default:
					break;
				}
                showLabel.setFont(Font.font(tempFamily,tempWeight,tempPosture,tempSize));
	    	}
		});
        
        sizeList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        	@Override
        	public void changed(ObservableValue<? extends String> observable, 
        			String oldValue, String newValue) {
        		tempSize = sizeMap.get(newValue);
        		showLabel.setFont(Font.font(tempFamily,tempWeight,tempPosture,tempSize));
        	}
		});
        
        tempFamily = EditorModel.font.getFamily();
        tempWeight = FontWeight.NORMAL;
        tempPosture = FontPosture.REGULAR;
        tempSize = (int)EditorModel.font.getSize();
        
        showLabel.setFont(Font.font(tempFamily,tempWeight,tempPosture,tempSize));
	}
	
	@FXML
	public void isOK(ActionEvent event) {
		EditorModel.fontFamily = tempFamily;
		EditorModel.fontWeight = tempWeight;
		EditorModel.fontPosture = tempPosture;
		EditorModel.fontSize = tempSize;
		EditorModel.textArea.setFont(Font.font(tempFamily,tempWeight,tempPosture,tempSize));
		Stage stage = (Stage) showLabel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) showLabel.getScene().getWindow();
		stage.close();
	}
}
