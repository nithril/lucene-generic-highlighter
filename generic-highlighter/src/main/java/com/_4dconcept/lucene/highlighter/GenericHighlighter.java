package com._4dconcept.lucene.highlighter;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.QueryTermScorer;

import java.io.IOException;
import java.io.StringReader;

/**
 * From org.apache.lucene.search.highlight.Highlighter
 */
public class GenericHighlighter {

    private Analyzer analyzer;
    private Query query;
    private HighlighterCallback callback;


    public GenericHighlighter(Analyzer analyzer, Query query, HighlighterCallback callback) {
        this.analyzer = analyzer;
        this.query = query;
        this.callback = callback;
    }

    public void highlight(String toHighlight, String field) throws IOException, ParseException {

        TokenStream tokenStream = analyzer.reusableTokenStream(field, new StringReader(toHighlight));
        QueryTermScorer queryTermScorer = new QueryTermScorer(query);

        TokenStream newStream = queryTermScorer.init(tokenStream);
        if (newStream != null) {
            tokenStream = newStream;
        }


        //tokenStream.addAttribute(PositionIncrementAttribute.class);
        tokenStream.reset();

        queryTermScorer.startFragment(null);

        int lastEndOffset = 0;

        TokenGroup tokenGroup = new TokenGroup(tokenStream);

        for (boolean next = tokenStream.incrementToken(); next; next = tokenStream.incrementToken()) {

            if ((tokenGroup.numTokens > 0) && tokenGroup.isDistinct()) {
                lastEndOffset = extractText(tokenGroup, toHighlight, lastEndOffset);
            }
            tokenGroup.addToken(queryTermScorer.getTokenScore());
        }

        if (tokenGroup.numTokens > 0) {
            lastEndOffset = extractText(tokenGroup, toHighlight, lastEndOffset);
        }

        //Test what remains of the original text beyond the point where we stopped analyzing
        if ((lastEndOffset < toHighlight.length())) {
            //append it to the last fragment
            callback.terms(toHighlight.substring(lastEndOffset), lastEndOffset, tokenGroup.getTotalScore());
        }
    }


    private int extractText(TokenGroup tokenGroup, String stringToHighlithe, int lastEndOffset) {
        //flush the accumulated text (same code as in above loop)
        int startOffset = tokenGroup.matchStartOffset;
        int endOffset = tokenGroup.matchEndOffset;
        String tokenText = stringToHighlithe.substring(startOffset, endOffset);

        //store any whitespace etc from between this and last group
        if (startOffset > lastEndOffset) {
            callback.terms(stringToHighlithe.substring(lastEndOffset, startOffset), lastEndOffset, 0);
        }

        callback.terms(tokenText, startOffset, tokenGroup.getTotalScore());

        tokenGroup.clear();

        return Math.max(lastEndOffset, endOffset);
    }

}
