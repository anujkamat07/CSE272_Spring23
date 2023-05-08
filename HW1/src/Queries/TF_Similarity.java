package Queries;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

public class TF_Similarity extends TFIDFSimilarity implements TFSimilarity {
    @Override
    public float tf(float freq) {
        return (float) Math.sqrt(freq);
    }

    @Override
    public float idf(long docFreq, long numDocs) {
        return 1;
    }

    @Override
    public float lengthNorm(int numTerms) {
        return 1;
    }

    @Override
    public float sloppyFreq(int distance) {
        return 1.0f / (distance + 1);
    }

    @Override
    public float scorePayload(int doc, int start, int end, BytesRef payload) {
        return 1;
    }

}
