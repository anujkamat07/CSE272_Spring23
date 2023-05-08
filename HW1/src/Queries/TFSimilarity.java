package Queries;

import org.apache.lucene.util.BytesRef;

public interface TFSimilarity {
    float tf(float freq) ;

    float idf(long docFreq, long numDocs);

    float lengthNorm(int numTerms);

    float sloppyFreq(int distance);
    float scorePayload(int doc, int start, int end, BytesRef payload);
}
