package Queries;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static Queries.Boolean.formbooleanQuery;
import static parser.ParserDocument.parsedDoc;
import static parser.QueryParser.parsedQuery;

public class MyOwnQuery {
    public static void myQuery(Directory index) throws Exception {

        PrintStream o = new PrintStream("MyOwnOutput");

        int len = parsedQuery().size();
        List<List<String>> parsedquery = parsedQuery().subList(1,len);
        List<String>queryID = parsedQuery().get(0);
        List<String>parsedDoc = parsedDoc();
        int l = parsedDoc.size();
        for(int k = 0; k<queryID.size(); k++) {
            int[] bonusScore = new int[l / 3];
            for (int j = 1; j < l; j = j + 3) {
                int sc = 0;
                for (int i = 0; i < (parsedquery.get(k)).size(); i++) {
                    String st = parsedDoc.get(j);
                    if (st.contains(parsedquery.get(k).get(i))) {
                        sc += 2;
                    }
                }
                bonusScore[j / 3] = sc;
            }
            BooleanQuery q = formbooleanQuery(k);

            int hitsPerPage = 44;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;


//            System.out.println("Found " + hits.length + " hits.");
            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.getIndexReader().document(docId);
                int ind = parsedDoc.indexOf(d.get("isbn"));
                hit.score += bonusScore[ind / 3];
            }
            for (int i = 0; i < hits.length - 1; i++) {
                for (int j = 0; j < hits.length - i - 1; j++) {
                    if (hits[j].score < hits[j + 1].score) {
                        ScoreDoc temp = hits[j];
                        hits[j] = hits[j + 1];
                        hits[j + 1] = temp;
                    }
                }
            }
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.getIndexReader().document(docId);
//                System.out.println((i + 1)+ ". " + queryID.get(k) + "\t" + d.get("isbn") + "\t" + hits[i].score);
                System.setOut(o);
                System.out.println(queryID.get(k) + "\t" + "R0" + "\t" + d.get("isbn") + "\t" + (i + 1)  + "\t" +  hits[i].score + "\t" + "MyOwn");
            }
        }
    }

}
