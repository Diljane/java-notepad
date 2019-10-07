package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileServce {
	public static List<String> readFile(File file) {
		try {
			List<String> list = new ArrayList<String>();
			if(file != null) {
				BufferedReader in = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = in.readLine()) != null) {
	                list.add(line);
	            }
	            in.close();
			}
            return list;
        } catch (IOException e) {
        	return null;
        }
	}
	
	public static void writeFile(String text, File file) {
		try {
			if(file != null) {
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write(text);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
