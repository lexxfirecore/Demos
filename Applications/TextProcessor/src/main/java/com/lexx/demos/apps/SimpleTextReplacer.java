package com.lexx.demos.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by alexandruco on 012, 12 May.
 */
public class SimpleTextReplacer {
    public static void main(String[] args) throws URISyntaxException {
        String inputFileName = "changes_htr.csv";
        String outputFileName = "s:\\htr_out.csv";
        List<String> allLines = new ArrayList();
        ClassLoader classLoader = SimpleTextReplacer.class.getClassLoader();

        try {
            File inputFile = new File(classLoader.getResource(inputFileName).getFile());
            InputStream inputFS = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            br.lines().forEach(line -> {
                String[] columns = line.split(",");
                if (allLines.size() > 0) {
                    line = line.replace(columns[1], columns[2]);
                }
                allLines.add(line);
            });

            File file = new File(outputFileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            allLines.forEach(line -> {
                try {
                    fw.write(line + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fw.flush();
            fw.close();

            br.close();
            inputFS.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
