package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class QueryParser {
    public static List<List<String>> parsedQuery() throws Exception
    {
        ArrayList<String> ll = new ArrayList<>();
        List<String> stopwords = Files.readAllLines(Paths.get("stopwords.txt"));

        File file = new File(
                "query.ohsu.1-63");

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null){
            String[] arrOfStr = st.split("\\<(.*?)\\>", -2);
            ll.addAll(Arrays.asList(arrOfStr));
        }
        ll.removeIf(s -> s.equals(""));
        ll.removeIf(s -> s.equals(" Description:"));
        for (int i = 0; i < ll.size();i++)
        {
            if(( ll.get(i)).charAt(0) == ' '){
                ll.set(i, ( ll.get(i)).substring(1));
            }
        }
//        List<List<List<String>>> lfinal = new ArrayList<>();
        List<List<String>> lfinalwords = new ArrayList<>();
        ArrayList<String> lfinalID = new ArrayList<>();

        for(int i = 0; i < ll.size();i = i+3){
            String[] arrOfStr1 = (ll.get(i+1).replaceAll("\\p{Punct}", "")).split(" ", -2);
            String[] arrOfStr2 = (ll.get(i+2).replaceAll("\\p{Punct}", "")).split(" ", -2);
            ArrayList<String> ls = new ArrayList<>();
            lfinalID.add((ll.get(i)).substring(8));
//            lfinalwords.add(lfinalID);
            for (String a : arrOfStr1) {
                if(!stopwords.contains(a.toLowerCase())){
                    ls.add(a.toLowerCase());
                }
            }
            for (String a : arrOfStr2) {
                if(!ls.contains(a)){
                    if(!stopwords.contains(a.toLowerCase())){
                        ls.add(a.toLowerCase());
                    }
                }
            }
            lfinalwords.add(ls);
//            lfinalID.add((ll.get(i)).substring(8));
        }
//        System.out.println(lfinalwords);
//        System.out.println(lfinalID);
//        System.out.println(stopwords);
        lfinalwords.add(0,lfinalID);
        return lfinalwords;
    }
}