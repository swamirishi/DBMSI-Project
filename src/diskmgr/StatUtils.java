package diskmgr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class StatUtils {
    public static void main(String[] args) {
        String fileName = "/Users/suyash/Desktop/cse548-4/Stat/src/main/java/data.txt";
        HashSet<String> subjectSet = new HashSet<>();
        HashSet<String> objectSet = new HashSet<>();
        HashMap<String, Integer> subjectIndegreeMap = new HashMap<>();
        HashMap<String, Integer> subjectOutdegreeMap = new HashMap<>();
        HashMap<String, Integer> objectIndegreeMap = new HashMap<>();
        HashMap<String, Integer> objectOutdegreeMap = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 4) {
                    String subject = tokens[0], object = tokens[2];
                    subjectSet.add(subject);
                    objectSet.add(object);
                    subjectOutdegreeMap.put(subject, subjectOutdegreeMap.getOrDefault(subject, 0) + 1);
                    objectIndegreeMap.put(object, objectIndegreeMap.getOrDefault(object, 0) + 1);
                }
                line = reader.readLine();
            }
            System.out.println("Unique subject values:" + subjectSet.size());
            System.out.println("Unique object values:" + objectSet.size());
            for(Map.Entry<String, Integer> e : subjectOutdegreeMap.entrySet()){
                System.out.println(e.getKey() + " subject value has outdegree " + e.getValue());
            }
            for(Map.Entry<String, Integer> e : objectIndegreeMap.entrySet()){
                System.out.println(e.getKey() + " object value has indegree " + e.getValue());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void add(String[] tokens) {



    }
}
