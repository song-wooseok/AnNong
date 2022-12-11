package ksnu.sw.uilab.annong.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ksnu.sw.uilab.annong.utils.enums.AppResourceName;

public class CSVUtils {
    private static final String COMMA = ",";

    public static List<List<String>> getFullDataFromDir(Context context, AppResourceName appResourceName){
        BufferedReader csvBr = FileUtils.openInternalFileReader(context, appResourceName);

        return readAllCsvLine(csvBr);
    }
    public static void writeCsvData(Context context, AppResourceName appResourceName, String data){
        BufferedWriter csvWr = FileUtils.openInternalFileWriter(context, appResourceName);
        try{
            csvWr.write(data+COMMA);
            csvWr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static List<List<String>> readAllCsvLine(BufferedReader br){
        List<List<String>> allLine = new ArrayList<>();

        try{
            String line;
            while((line=br.readLine()) != null){
                allLine.add(readCsvLine(line));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return allLine;
    }

    private static List<String> readCsvLine(String csvLine){
        List<String> line = new ArrayList<>();
        String[] dataSplitByComma = csvLine.split(COMMA);

        Collections.addAll(line, dataSplitByComma);

        return line;
    }
}
