package application;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class EditorModel {
	public static TextArea textArea;
	private File curFile;
	public static int curRow;
	
	public static Font font = Font.getDefault();
	public static String fontFamily = Font.getDefault().getName();
	public static FontWeight fontWeight;
	public static FontPosture fontPosture;
	public static int fontSize = 14;
	
	public static String findContext = "";
	public static boolean isCompareNoCase = false;
	public static boolean up = false;

	public EditorModel(TextArea textArea) {
		EditorModel.textArea = textArea;
		curFile = null;
	}
	
	public File getCurrentFile() {
		return curFile;
	}
	
	public void setCurrentFile(File file) {
		curFile = file;
	}
	
	public boolean isChanged() {
		String text = textArea.getText();
		
		if(curFile == null) {
			return !text.equals("");
		}
		else {
			try {
				List<String> list = FileServce.readFile(curFile);
				String fileText = list.stream().collect(Collectors.joining("\n"));
				fileText = fileText == null ? "":fileText;
				return !fileText.equals(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void onFileOpen(File file) {
		if(file == null) {
			return;
		}
		else {
			try {
				List<String> list = FileServce.readFile(file);
				String text = list.stream().collect(Collectors.joining("\n"));
				text = text == null ? "" : text;
				textArea.setText(text);
				curFile = file;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onFileSave() {
		if(!isChanged()) {
			return;
		}
		onFileSave(curFile);
	}
	
	public void onFileSave(File file) {
		if(file == null) {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialDirectory(new File("./"));
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
				File saveFile = fileChooser.showSaveDialog(null);
				curFile = saveFile;
				String text = textArea.getText();
				FileServce.writeFile(text, saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				String text = textArea.getText();
				FileServce.writeFile(text, file);
				if(!file.equals(curFile)) {
					curFile = file;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onExit() {
		System.exit(0);
	}
	
	public int getRow() {
		int caretPos = textArea.getCaretPosition();
		String subText = textArea.getText().substring(0,caretPos);
		int row = 0;
		Pattern pattern = Pattern.compile("\n");
		Matcher matcher = pattern.matcher(subText);
		while (matcher.find()) {
			row++;
		}
		return row + 1;
	}
	
	public int getAllRow() {
		int row = 0;
		Pattern pattern = Pattern.compile("\n");
		Matcher matcher = pattern.matcher(textArea.getText());
		while (matcher.find()) {
			row++;
		}
		return row + 1;
	}
	
	public int getColumn() {
		int caretPos = textArea.getCaretPosition();
		int lastLine = textArea.getText().substring(0,caretPos).lastIndexOf('\n');
		return caretPos - lastLine;
	}
	
	public void showErrorCaret() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("记事本-跳行");
		alert.setHeaderText("行数超过了总行数");
		alert.setContentText("Ooops, there was an error!");
		alert.showAndWait();
	}
	
	public void setCaret() {
		int tmp = -1;
		int pos = 1;
		for(int i = 0;i < curRow - 1;i++) {
			pos = textArea.getText().indexOf('\n',tmp+1);
			tmp = pos;
		}
		textArea.positionCaret(pos+1);
	}
	
	public void showDate() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
		String date = df.format(new Date());
    	StringBuilder sb = new StringBuilder(textArea.getText());
    	int pos = textArea.getCaretPosition();
    	sb.insert(pos, date);
    	textArea.setText(sb.toString());
	}
	
	public static int getCurCaret() {
		return textArea.getCaretPosition();
	}
}
