package parser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ParserDocument {

    public static ArrayList<String> parsedDoc() throws IOException {

        ArrayList<String> ll = new ArrayList<>();
        File file = new File(
                "ohsu.med.88-91");
        Scanner sc = new Scanner(file);

        String st;
        while (sc.hasNextLine()){

            st = sc.nextLine();
            if(st.startsWith(".U")){
                st = sc.nextLine();
                ll.add(st);
            }
            else if(st.startsWith(".T")) {
                st = sc.nextLine();
                ll.add(st.toLowerCase());
                sc.nextLine();
                sc.nextLine();
                st = sc.nextLine();
                if (st.startsWith(".W")) {
                    st = sc.nextLine();
                    ll.add(st.toLowerCase());
                } else {
                    ll.add(" ");
                }
            }
        }
        return ll;
    }
}
