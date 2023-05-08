package Queries;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static parser.QueryParser.parsedQuery;

public class Boolean {

    public static BooleanQuery formbooleanQuery( int j) throws Exception{
        int len = parsedQuery().size();
        List<List<String>> parsedquery = parsedQuery().subList(1,len);

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        for(int i = 0; i< (parsedquery.get(j)).size(); i++){
            Term T1 = new Term("desc", parsedquery.get(j).get(i));
            queryBuilder.add(new TermQuery(T1), BooleanClause.Occur.SHOULD);
        }
        return queryBuilder.build();
    }
    public static void createBooleanQuery(Directory index) throws Exception {

        PrintStream o = new PrintStream("BooleanOutput");

        List<String>queryID = parsedQuery().get(0);
        for(int j = 0; j < queryID.size(); j++){
            BooleanQuery q = formbooleanQuery(j);

            int hitsPerPage = 44;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

//            System.out.println("Found " + hits.length + " hits.");
            for (int k = 0; k < hits.length; ++k) {
                int docId = hits[k].doc;
                Document d = searcher.getIndexReader().document(docId);
//                System.out.println((k + 1) + ". " + queryID.get(j) + "\t" + d.get("isbn") + "\t" + hits[k].score);
                System.setOut(o);
                System.out.println(queryID.get(j) + "\t" + "R0" + "\t" + d.get("isbn") + "\t" + (k + 1)  + "\t" +  hits[k].score + "\t" + "Bool");
            }
        }
    }
}
