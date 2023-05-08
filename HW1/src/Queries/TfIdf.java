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
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static Queries.Boolean.formbooleanQuery;
import static parser.ParserDocument.parsedDoc;
import static parser.QueryParser.parsedQuery;
import Queries.TF_Similarity.*;


public class TfIdf {

//    private static void addDoc(IndexWriter w, String isbn, String title, String desc) throws IOException {
//        Document doc = new Document();
//        doc.add(new StringField("isbn", isbn, Field.Store.YES));
//        doc.add(new TextField("title", title, Field.Store.YES));
//        doc.add(new TextField("desc", desc, Field.Store.YES));
//        w.addDocument(doc);
//    }
//    static Directory indexDoc(StandardAnalyzer analyzer, Similarity similarity) {
//        Directory indexdoc = new ByteBuffersDirectory();
//
//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        config.setSimilarity(similarity);
//        try (IndexWriter wr = new IndexWriter(indexdoc, config)) {
//            List<String> parsedDoc = parsedDoc();
//            int l = parsedDoc.size();
//            for(int i = 0; i < l; i = i+3) {
//                addDoc(wr, parsedDoc.get(i), parsedDoc.get(i + 1), parsedDoc.get(i+2));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return indexdoc;
//
//    }
    public static void createTfIdfQuery(Directory index) throws Exception {

        PrintStream o = new PrintStream("TfIdfOutput");

        List<String>queryID = parsedQuery().get(0);
        for(int j = 0; j < queryID.size(); j++) {
            BooleanQuery q = formbooleanQuery(j);
            TFIDFSimilarity tfidfSim = new ClassicSimilarity();
            int hitsPerPage = 44;

//            Directory index = indexDoc(analyzer, tfidfSim);
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(tfidfSim);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

//            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.getIndexReader().document(docId);
                System.setOut(o);
                System.out.println(queryID.get(j) + "\t" + "R0" + "\t" + d.get("isbn") + "\t" + (i + 1) + "\t" + hits[i].score + "\t" + "TfIdf");
            }
        }
    }
}