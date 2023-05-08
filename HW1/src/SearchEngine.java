import Queries.TF_Similarity;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static Queries.TfQuery.createTfQuery;
import static parser.ParserDocument.parsedDoc;
import static parser.QueryParser.parsedQuery;
import static parser.relevantDocParser.qrelParser;

import static Queries.Boolean.*;
import static Queries.RelevanceFeedback.*;
import static Queries.MyOwnQuery.myQuery;
import static Queries.TfIdf.createTfIdfQuery;

public class SearchEngine {
    private static void addDoc(IndexWriter w, String isbn, String title, String desc) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("desc", desc, Field.Store.YES));
        w.addDocument(doc);
    }
    static Directory indexDoc(StandardAnalyzer analyzer) {
        Directory indexdoc = new ByteBuffersDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter wr = new IndexWriter(indexdoc, config)) {
            List<String> parsedDoc = parsedDoc();
            int l = parsedDoc.size();
            for(int i = 0; i < l; i = i+3) {
                addDoc(wr, parsedDoc.get(i), parsedDoc.get(i + 1), parsedDoc.get(i+2));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return indexdoc;

    }

    public static void main(String[] args) throws Exception {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        if(args.length == 0){
            System.out.println("ENTER BETWEEN 1-4 TO RUN SEARCH ENGINE");
        }
        else{
            if(Objects.equals(args[0], "1")){
                createBooleanQuery(indexDoc(analyzer));
            } else if (Objects.equals(args[0], "2")) {
                createTfQuery(indexDoc(analyzer));
            } else if (Objects.equals(args[0], "3")) {
                createTfIdfQuery(indexDoc(analyzer));
            } else if (Objects.equals(args[0], "4")) {
                relevanceFeedbackQuery(indexDoc(analyzer));
            } else if (Objects.equals(args[0], "5")) {
                myQuery(indexDoc(analyzer));
            }else{
                System.out.println("ENTER BETWEEN 1-4 TO RUN SEARCH ENGINE");
            }
        }
    }
}