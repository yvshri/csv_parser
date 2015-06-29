/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv.parser;

/**
 *
 * @author best1yash
 */
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.*;
import com.opencsv.*;
import java.io.FileNotFoundException;

public class CSVParser {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        String file_to_parse;
        String[] val_array;
        file_to_parse = "./input/E-library-data-3.csv";
        //Build reader instance
        CSVReader reader = new CSVReader(new FileReader(file_to_parse), ';', '"', 1);

        //Read all rows at once
        List<String[]> allRows = reader.readAll();

//        Read CSV line by line and use the string array as you want
        for (String[] row : allRows) {
            for (int i = 0; i < row.length; i++) {
                row[i] = row[i].replaceAll("(\\r|\\n|\\r\\n)+", " ");
                row[i] = row[i].replaceAll(System.getProperty("line.separator"), "; ");
                row[i] = row[i].replaceAll("&", "and");

            }

            System.out.println(Arrays.toString(row));
        }
        List<String[]> map = getMap();
        String[] field;
        long fin;
        fin = 0;
        for (String[] row : allRows) {
            File file1 = new File("./output//newdir//folder" + fin + "");
            file1.mkdirs();
//            File file3 = new File("./output//newdir//folder" + fin + "//content");
//            file3.mkdirs();
            PrintWriter writer_content = new PrintWriter("./output//newdir//folder" + fin + "//contents", "UTF-16");
            PrintWriter writer_lrmi = new PrintWriter("./output//newdir//folder" + fin + "//metadata_lrmi.xml", "UTF-16");
            PrintWriter writer = new PrintWriter("./output//newdir//folder" + fin + "//content.xml", "UTF-16");
            writer.println("<?xml version=\"1.0\" encoding=\"utf-16\" standalone=\"no\"?>");
            writer.println("<dublin_core schema=\"dc\">");
            writer_lrmi.println("<?xml version=\"1.0\" encoding=\"utf-16\" standalone=\"no\"?>");
            writer_lrmi.println("<dublin_core shema=\"lrmi\">");
            for (int i = 0; i < row.length; i++) {
                if (i == 43) {
                    continue;
                } else if (i == 43) {
                    field = map.get(42);
                } else if (i == 44) {
                    field = map.get(43);
                } else if (i == 45 || i == 46) {
                    continue;
                } else {
                    field = map.get(i);
                }
                val_array = parseVal(row[i]);
                if (val_array.length == 0) {
                    continue;
                }
                PrintWriter useWriter;
                if (field[0].equals("dc")) {
                    useWriter = writer;
                } else {
                    System.out.println("LRMI");
                    useWriter = writer_lrmi;
                }
                switch (field.length) {
                    case 2:
                        writeXML(useWriter, field[1], "", val_array);
                        break;
                    case 3:
                        writeXML(useWriter, field[1], field[2], val_array);
                        break;
                    default:

                }
            }
            fin++;
            writer.println("</dublin_core>");
            writer_lrmi.println("</dublin_core>");
            writer.close();
            writer_lrmi.close();
            writer_content.close();
        }
    }

    private static void writeXML(PrintWriter writer, String elem, String qual,
            String[] val_array) {
        if (elem.equals("language")) {
            val_array = getLang(val_array);
        } else if (elem.equals("educationalAlignment") && qual.equals("educationalFramework")) {
            val_array = getEduFrmwrk(val_array);
        } else if (elem.equals("educationalAlignment") && qual.equals("educationalLevel")) {
            val_array = getEduLvl(val_array);
        } else if (elem.equals("learningResourceType")) {
            val_array = getLearningResourceType(val_array);
        }
        for (int i = 0; i < val_array.length; i++) {
            if (val_array[i].equals("") || val_array[i] == null) {
                continue;
            } else {
                String writer1 = "<dcvalue element =\"" + elem + "\"";
                if (qual != "") {
                    writer1 = writer1 + " qualifier=\"" + qual + "\"";
                }
                if (val_array[i] != "") {
                    writer1 = writer1 + ">" + val_array[i] + "</dcvalue>";
                } else {
                    writer1 = writer1 + " />";
                }
                writer.println(writer1);
            }
        }
    }

    private static String[] getLang(String[] val) {
        for (int i = 0; i < val.length; i++) {
            if (val[i].equals("English")) {
                val[i] = "eng";
            } else if (val[i].equals("Hindi")) {
                val[i] = "hin";
            } else if (val[i].equals("Bengali")) {
                val[i] = "ben";
            }
        }
        return val;
    }

    private static List getMap() throws FileNotFoundException, IOException {
        String file_to_parse;
        file_to_parse = "./input/map.csv";
        //Build reader instance
        CSVReader reader = new CSVReader(new FileReader(file_to_parse), '.');

        //Read all rows at once
        List<String[]> allRows = reader.readAll();
        for (String[] row : allRows) {
            for (int i = 0; i < row.length; i++) {
                row[i] = row[i].replaceAll("(\\r|\\n|\\r\\n|)+", "");
                row[i] = row[i].replaceAll("\\s+", "");
                row[i] = row[i].replaceAll(System.getProperty("line.separator"), "; ");
            }
        }
        return allRows;

    }

    private static String[] parseVal(String val) {
        val = val.replaceAll("\"", "");
        val = val.replace("; |;", " ");
        String delimiter = ";";
        String[] val_array = val.split(delimiter);
        for (int i = 0; i < val_array.length; i++) {
            val_array[i] = val_array[i].trim();
        }
        return val_array;
    }

    private static String[] getEduLvl(String[] val) {
        for (int i = 0; i < val.length; i++) {
            if (val[i].equals("Under Graduate") || val[i].equals("Post Graduate")) {
                val[i] = "ug_pg";
            } else if (val[i].equals("I") || val[i].equals("II") || val[i].equals("III") || val[i].equals("IV")) {
                val[i] = "lowerPrimary";
            } else if (val[i].equals("V") || val[i].equals("VI") || val[i].equals("VII") || val[i].equals("VIII")) {
                val[i] = "upperPrimary";
            } else if (val[i].equals("IX") || val[i].equals("X")) {
                val[i] = "middleSchool";
            } else if (val[i].equals("XI") || val[i].equals("XII")) {
                val[i] = "highSchool";
            }
        }
        int end = val.length;
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < end; i++) {
            set.add(val[i]);
        }
        val = set.toArray(new String[set.size()]);
        return val;
    }

    private static String[] getEduFrmwrk(String[] val) {
        for (int i = 0; i < val.length; i++) {
            if (val[i].equals("I.C.S.E")) {
                val[i] = "Indian Board of School Education";
            } else if (val[i].equals("C.B.S.E")) {
                val[i] = "Central Board of Secondary Education";
            }
        }
        return val;
    }

    private static String[] getLearningResourceType(String[] val_array) {
        return val_array;
    }
}
