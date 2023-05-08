package Queries;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Queries.Boolean.formbooleanQuery;
import static parser.ParserDocument.parsedDoc;
import static parser.QueryParser.parsedQuery;
import static parser.relevantDocParser.qrelParser;


public class RelevanceFeedback {
    private static Query expandQuery(Query originalQuery, ArrayList<String> relevantDocIDs) throws IOException {
        // Collect the relevant documents and extract terms from them
        List<String> stopwords = Files.readAllLines(Paths.get("stopwords.txt"));
        List<String> parsedDoc = parsedDoc();
        ArrayList<String> relevantTerms = new ArrayList<>();
        for (String relevantDocID : relevantDocIDs) {
            int ind = parsedDoc.indexOf(relevantDocID);
            String[] arrSt = (parsedDoc.get(ind + 1).replaceAll("\\p{Punct}", "")).split("\\s+", -2);
            for (String a : arrSt) {
                if (!stopwords.contains(a.toLowerCase())) {
                    if(!relevantTerms.contains(a)){
                        relevantTerms.add(a.toLowerCase());
                    }
                }
            }
        }
        // Build a new BooleanQuery to expand the original query
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        queryBuilder.add(originalQuery, BooleanClause.Occur.MUST);

        // Add the terms from the relevant documents to the expanded query
        for (String term : relevantTerms) {
            Term T1 = new Term("desc", term);
            Query termQuery = new TermQuery(T1);
            queryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
        }
        // Return the expanded query
        return queryBuilder.build();
    }
    public static void relevanceFeedbackQuery(Directory index) throws Exception {

        PrintStream o = new PrintStream("RelevanceFeedbackOutput");

        List<String>queryID = parsedQuery().get(0);
        for(int j = 0; j < queryID.size(); j++) {
            BooleanQuery q = formbooleanQuery(j);

            List<List<String>> parsedRelDoc = qrelParser();
            ArrayList<String> docIDlst = new ArrayList<>();
            for (List<String> strings : parsedRelDoc) {
                if (Objects.equals(strings.get(0), queryID.get(j))) {
                    if (Objects.equals(strings.get(2), "2")) {
                        docIDlst.add(strings.get(1));
                    }
                }
            }
            int hitsPerPage = 44;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);

            Query expandedQuery = expandQuery(q, docIDlst);
            TopDocs docs = searcher.search(expandedQuery, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

//            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.getIndexReader().document(docId);
//                System.out.println((i + 1) + ". " + queryID.get(j) + "\t" + d.get("isbn") + "\t" + hits[i].score);
                System.setOut(o);
                System.out.println(queryID.get(j) + "\t" + "R0" + "\t" + d.get("isbn") + "\t" + (i + 1)  + "\t" +  hits[i].score + "\t" + "RelFeed");
            }
        }
    }
}
