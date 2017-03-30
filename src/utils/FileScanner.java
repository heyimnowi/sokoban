package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class FileScanner {
    
    public static List<String> listFiles(String directoryName){
        File directory = new File(directoryName);
        List<String> filesArray = new ArrayList<String>();
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
                filesArray.add(file.getName());
            }
        }
        return filesArray;
    }
}
