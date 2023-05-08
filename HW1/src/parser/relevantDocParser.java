package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class relevantDocParser {

    public static List<List<String>> qrelParser() throws IOException {

        List<List<String>> llfinal = new ArrayList<>();
        File file = new File(
                "qrels.ohsu.batch.87");
        Scanner sc = new Scanner(file);

        String st;
        while (sc.hasNextLine()){
            st = sc.nextLine();
            String[] arrOfStr = st.split("\\s+", -2);
            List<String> ll = new ArrayList<>(Arrays.asList(arrOfStr));
            llfinal.add(ll);
        }
        return llfinal;
    }
}
