package com.lexx.demos.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.deploy.util.StringUtils;


/**
 * Created by alexandruco on 012, 12 May.
 */
public class XmlSearcherSimple {
    public static void main(String[] args) throws URISyntaxException {
        String sourceFileName = "s:\\task\\Db.xml";
        String targetFileName = "s:\\task\\BussinesItem.xml";
        String searchableFileName = "s:\\task\\Searchable.xml";


        System.out.println("\n");
        //      ClassLoader classLoader = XmlCationSearcherSimple.class.getClassLoader();

        List<String> nodesDb = readAllDBNodeNames(sourceFileName);
        System.out.println("nodesDb: " + nodesDb.size());
  //      nodesDb.forEach(System.out::println);


        Set<String> nodeNamesContainedInXml = findNodeNamesInBI(targetFileName, nodesDb);
        System.out.println("nodeNamesContainedInXml: " + nodeNamesContainedInXml.size());

        List<String> searchableNodeList = readAllInSearchableXML(searchableFileName);

        System.out.println("searchableNodeList: " + searchableNodeList.size());

        nodeNamesContainedInXml.removeAll(searchableNodeList);
        System.out.println("nodeNamesContainedInXml after delete: " + nodeNamesContainedInXml.size());

//        nodeNamesContainedInXml.forEach(System.out::println);

        Set<String> nodeNamesInBIWithLines = findNodeNamesInBIWithLines(targetFileName, nodeNamesContainedInXml);
        System.out.println("nodeNamesInBIWithLines: " + nodeNamesInBIWithLines.size());
        nodeNamesInBIWithLines.forEach(System.out::println);

        System.out.println("****");
        nodeNamesContainedInXml.forEach(i -> {
            if (!nodesDb.contains(i)) System.out.println(i);
        });
    }

    private static List<String> readAllDBNodeNames(String inputFileName) {
        List<String> allLines = new ArrayList();
        try {
            File inputFile = new File(inputFileName);
            InputStream inputFS = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            br.lines().filter(line -> line.contains("<Name>"))
                    .forEach(line -> {
                        String replace = line.replaceAll("\\t", "").replace("<Name>", "").replace("</Name>", "").trim();

                        allLines.add(replace);
                    });
            br.close();
            inputFS.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }


    private static Set<String> findNodeNamesInBI(String inputFileName, List<String> itemList) {
        Set<String> set = new HashSet<>();
        try {
            File inputFile = new File(inputFileName);
            InputStream inputFS = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));


            br.lines().forEach(line ->
                    itemList.forEach(item -> {
                        if (line.contains(item)) {
                            set.add(item);
                         /*   System.out.println("<Field name='" + item + "'");
                            System.out.println("Item: " + item);
                            System.out.println("Line: " + line);*/

                        }
                    })
            );


            br.close();
            inputFS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    private static Set<String> findNodeNamesInBIWithLines(String inputFileName, Set<String> itemList) {
        Set<String> set = new HashSet<>();
        try {
            File inputFile = new File(inputFileName);
            InputStream inputFS = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));


            br.lines().forEach(line ->
                            itemList.forEach(item -> {
                                if (line.contains("'" + item + "'") && line.contains("<Field name='")) {
//                                    if (line.contains(item) && line.contains("<Field name='")) {
                                    set.add(line);
/*                            System.out.println("<Field name='" + item + "'");
                            System.out.println("Item: " + item);
                            System.out.println("Line: "  + line);*/

                                }
                            })
            );


            br.close();
            inputFS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    private static List<String> readAllInSearchableXML(String inputFileName) {
        List<String> allLines = new ArrayList();
        try {
            File inputFile = new File(inputFileName);
            InputStream inputFS = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            br.lines().forEach(line -> {
                Pattern pattern = Pattern.compile("name='(.*?)'");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
//                    System.out.println(matcher.group(1));
                    allLines.add(matcher.group(1).trim());
                }
            });
            br.close();
            inputFS.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }

}





