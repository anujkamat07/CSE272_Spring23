package Queries;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static Queries.Boolean.formbooleanQuery;
import static parser.ParserDocument.parsedDoc;
import static parser.QueryParser.parsedQuery;

public class TfQuery {
    public static void createTfQuery(Directory index) throws Exception {
        PrintStream o = new PrintStream("TfOutput");

        List<String> queryID = parsedQuery().get(0);
        for(int j = 0; j < queryID.size(); j++) {
            BooleanQuery q = formbooleanQuery(j);

            Similarity tfsim  = new TF_Similarity();

            int hitsPerPage = 44;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(tfsim);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

//            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.getIndexReader().document(docId);
                System.setOut(o);
                System.out.println(queryID.get(j) + "\t" + "R0" + "\t" + d.get("isbn") + "\t" + (i + 1) + "\t" + hits[i].score + "\t" + "Tf");
            }
        }
    }
}


