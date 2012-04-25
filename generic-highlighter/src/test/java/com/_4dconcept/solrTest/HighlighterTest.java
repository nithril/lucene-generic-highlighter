package com._4dconcept.solrTest;

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


import com._4dconcept.lucene.highlighter.GenericHighlighter;
import com._4dconcept.lucene.highlighter.HighlighterCallback;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HighlighterTest {

    String text = "anatomical inconveniences with Thought, discernible the dealing their Sub-Etha Sens-O-Matic and left going so amazingly primitive that got he would stop thinking about him, and the and he said. - Fine, - he didn't you? - No. That's the bar, - Cops! - Well, - that you're not being drunk. - started to of to social The out of these years. Who's Fun dressed dais before that, - the stale thin and very lucky it's been ordered to you, metalman? - wailed";


    @Test
    public void testHighlight() throws IOException, ParseException {

        EnglishAnalyzer standardAnalyzer = new EnglishAnalyzer(Version.LUCENE_35);
        QueryParser queryParser = new QueryParser(Version.LUCENE_35 , "text" , standardAnalyzer);

        Query query = queryParser.parse("left think wailed");

        final List<String> res = new ArrayList<String>();

        GenericHighlighter genericHighlighter = new GenericHighlighter(standardAnalyzer , query , new HighlighterCallback() {
            @Override
            public void terms(String term, int offset, float score) {
                if (score > 0){
                    res.add("[" + term + "]");
                }else{
                    res.add(term);
                }

            }
        });
        genericHighlighter.highlight(text , "text");
        Assert.assertEquals("anatomical inconveniences with Thought, discernible the dealing their Sub-Etha Sens-O-Matic and [left] going so amazingly primitive that got he would stop [thinking] about him, and the and he said. - Fine, - he didn't you? - No. That's the bar, - Cops! - Well, - that you're not being drunk. - started to of to social The out of these years. Who's Fun dressed dais before that, - the stale thin and very lucky it's been ordered to you, metalman? - [wailed]", StringUtils.join(res, ""));
    }



}
