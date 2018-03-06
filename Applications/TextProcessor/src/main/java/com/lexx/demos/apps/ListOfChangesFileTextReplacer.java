package com.lexx.demos.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;


/**
 * Created by alexandruco on 012, 12 May.
 */
public class ListOfChangesFileTextReplacer {

    private static final String ITEM_LIST_HTR_CSV = "itemList_htr.csv";
    private static final String ITEM_LIST_GBD_CSV = "itemList_gbd.csv";
    private static final String ITEM_LIST_ATT_CSV = "itemList_att.csv";
    private static final String CHANGES_HTR_CSV = "changes_htr.csv";
    private static final String CHANGES_GBD_CSV = "changes_gbd.csv";
    private static final String CHANGES_ATT_CSV = "changes_gbd.csv";
    private static final String CHANGES_CKP_CSV = "changes_ckp.csv";
    private static final String CHANGES_NCPDP_CSV = "changes_ncpdp.csv";
    private static final String CHANGES_HIX_CSV = "changes_hix.csv";


    private static final String OUT_PATH = "s:\\processor_out\\";
    private static List<String> listOfItems = new ArrayList<>();
    private static Map<String, String> listOfChanges = new HashMap<>();
    private static ClassLoader classLoader = SimpleTextReplacer.class.getClassLoader();
// XML
//    private static final String IN_PATH = "S:\\Projects\\tm-trunk\\trunk\\tm-core\\src\\main\\resources\\com\\edifecs\\cdesk\\tm\\item\\itemdefinition";
//    private static final String CAPTION = "<Caption>"



// CSV
//    private static final String CAPTION = ",Caption,";
//    private static final String IN_PATH = "S:\\Projects\\tm-trunk\\trunk\\tm-core\\src\\main\\resources\\resources\\datasets";
//    private static final String IN_PATH = "S:\\Projects\\tm-trunk\\trunk\\tm-core\\src\\main\\resources\\resources\\itemdefinitions";


// XML GBD
    private static final String CAPTION = "caption=\"";
//    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\HIPAAX12N\\trunk\\foundation\\src\\main\\resources\\GBD\\UI";
//    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\HIPAAX12N\\trunk\\standard\\src\\main\\resources\\GBD\\UI";


//XML ATTACHMENT
//    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\attachment-knowledge\\trunk\\foundation\\src\\main\\resources\\content-pack\\GBD\\UI";
//    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\attachment-knowledge\\trunk\\member-rfi\\member-rfi-cp\\src\\main\\resources\\content-pack\\GBD\\UI";

    //XML CKP
    //private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\apps\\CNC\\CKP\\trunk\\dist\\member-correlation\\src\\main\\resources\\GBD\\UI";
    //private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\apps\\CNC\\CKP\\trunk\\dist\\standard\\src\\main\\resources\\GBD\\UI";

    // XML CDPD
//    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\NCPDP\\trunk\\ncpdp\\src\\main\\resources\\GBD\\UI";

    // XML HIX
    private static final String IN_PATH = "S:\\Projects\\tm\\engineering\\components\\content-packs\\HIX-TRANSACTIONS\\trunk\\hix-content-pack\\src\\main\\resources\\GBD\\UI";


    public static void main(String[] args) {

        createFolder(OUT_PATH);

        //List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_HTR_CSV).getFile());
        //List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_GBD_CSV).getFile());
        //List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_ATT_CSV).getFile());
        //List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_CKP_CSV).getFile());
        //List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_NCPDP_CSV).getFile());
        List<String> linesOfChangesFromFile = readFromFile(classLoader.getResource(CHANGES_HIX_CSV).getFile());
        listOfChanges = getListOfChanges(linesOfChangesFromFile);
        if (listOfChanges.size() != 16 && listOfChanges.size() != 31 && listOfChanges.size() != 22 && listOfChanges.size() != 9 && listOfChanges.size() != 34) { // Service Begin Date / Service End Date repeats
            System.out.println("not all changes loaded. Found only " + linesOfChangesFromFile.size());
            return;
        }


        // READ FROM FOLDER OR LIST FROM FILE
       // listOfItems = getItemListFromFile(ITEM_LIST_ATT_CSV);
        listOfItems = readAllFilesFromFolder(IN_PATH);
        listOfItems.remove("NavigationBar.xml");
/*      if (listOfItems.size() != 20 && listOfItems.size() != 22 && listOfItems.size() != 34 && listOfItems.size() != 3) {
            System.out.println("not all items loaded. items loaded: " + listOfItems.size());
            return;
        }
*/
        if(IN_PATH.contains("itemdefinitions")) {
            listOfItems.clear();
            listOfItems.add("resources.csv");
        }

        for (String itemName : listOfItems) {
            System.out.println("\nProcessing item file " + itemName);
            List<String> linesFromFile = readFromFile(IN_PATH + "\\" + itemName);
            List<String> processedLines = processLines(linesFromFile, CAPTION);
            if(!processedLines.isEmpty()) {
                int numberOfLinesSaved = saveLinesToFile(processedLines, OUT_PATH + itemName);

                System.out.println("\n--------------------------");
                System.out.println("numberOfLinesChangedXml: " + processedLines.size());
                System.out.println("numberOfLinesSaved: " + numberOfLinesSaved);
            }
        }
    }

    private static List<String> getItemListFromFile(String inputCsvList) {
        ClassLoader classLoader = SimpleTextReplacer.class.getClassLoader();
        File inputFile = new File(classLoader.getResource(inputCsvList).getFile());
        List<String> list = new ArrayList<>();
        try {
            InputStream ifs = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(ifs));
            br.lines().forEach(line -> {
                list.add(line.split(",")[0]);
            });
            br.close();
            ifs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> processLines(List<String> lines, String caption) {

        List<String> inputLines = new ArrayList<>(lines);
        List<String> processedLines = new ArrayList<>();

        for (Map.Entry<String, String> changes : listOfChanges.entrySet()) {
               /* String target = "<Caption>Current Validation Status Date Time</Caption>";//= "<Caption>" + oldCaption + "</Caption>";
                String replacement = "<Caption>Current Validation Status</Caption>";//"<Caption>" + newCaption + "</Caption>";*/
            String target = caption + changes.getKey();
            String replacement = caption + changes.getValue();
            System.out.println("\n" + target + "\n" + replacement);

            for (String line : inputLines) {
                String replaced = line.replace(target, replacement);
                processedLines.add(replaced);
            }
            inputLines = new ArrayList<>(processedLines);
            processedLines.clear();
        }
        return inputLines;
    }

    private static List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList();
        try {
            File inputFile = new File(fileName);
            InputStream ifs = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(ifs));
            br.lines().forEach(lineInFile -> {
                lines.add(lineInFile);
            });

            br.close();
            ifs.close();
        } catch (FileNotFoundException e) {
            System.err.println("File " + fileName + " does not exist.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("File " + fileName + " does not exist.");
        }
        return lines;
    }

    /* private static Map<String, String> getListOfChanges(String inputCsv) {
         ClassLoader classLoader = SimpleTextReplacer.class.getClassLoader();
         File inputFile = new File(classLoader.getResource(inputCsv).getFile());
         HashMap<String, String> map = new HashMap<>();
         try {
             InputStream ifs = new FileInputStream(inputFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(ifs));
             br.lines().forEach(line -> {
                 String[] columns = line.split(",");
                 map.put(columns[1], columns[2]);
             });
             map.remove("Title/Caption");
             br.close();
             ifs.close();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return map;
     }
 */
    private static Map<String, String> getListOfChanges(List<String> lines) {
        HashMap<String, String> map = new HashMap<>();
        lines.forEach(line -> {
            String[] columns = line.split(",");
            map.put(columns[1], columns[2]);
        });
        map.remove("Title/Caption");
        return map;
    }

    private static int saveLinesToFile(List<String> lines, String outputFile) {
        try {
            if(!lines.isEmpty()) {
                FileWriter fw = new FileWriter(getFile(outputFile).getAbsoluteFile());
                lines.forEach(line -> {
                    try {
                        fw.write(line + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countLinesFromFile(outputFile);
    }

    private static int countLinesFromFile(String fileName) {
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new FileReader(getFile(fileName)));
            lnr.skip(Long.MAX_VALUE);
            lnr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lnr.getLineNumber();
    }

    private static File getFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private static void createFolder(String directory) {
        try {
            File folder = new File(directory);
            if (folder.exists()) {
                FileUtils.cleanDirectory(folder); //clean out directory (this is optional -- but good know)
                FileUtils.forceDelete(folder); //delete directory
            }
            if (!folder.exists()) {
                FileUtils.forceMkdir(folder); //create directory
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readAllFilesFromFolder(String path){
        List<String> listOfFiles = new ArrayList<>();
        File folder = new File(path);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                //listFilesForFolder(fileEntry);
                System.err.println("folders inside");
            } else {
                listOfFiles.add(fileEntry.getName());
            }
        }
        return listOfFiles;
    }


}
