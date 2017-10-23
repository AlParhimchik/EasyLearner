package com.example.sashok.easylearner.data;

import com.example.sashok.easylearner.model.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 12.10.17.
 */

public class StudyNowWordDataProvider extends WordDataProvider {
    public String folderURL;

    @Override
    public List<Word> getData() {

        try {
            Document doc = Jsoup.connect(folderURL).get();
            Element body = doc.body();
            Element tbody_elems = body.getElementsByTag("script").get(5);
            String body_str = tbody_elems.toString();
            int pos = body_str.indexOf("words[1]=new Word");
            body_str = body_str.substring(pos);
            int pos_next = 0;
            String word_str = null;
            final List<Word> words = new ArrayList<Word>();
            do {
                pos_next = body_str.indexOf(";words");
                if (pos_next != -1) {
                    word_str = body_str.substring(0, pos_next);
                    body_str = body_str.substring(pos_next + 1, body_str.length());
                    String[] strings = word_str.split("'");
                    Word new_word = new Word();
                    new_word.setTranslation(strings[5]);
                    new_word.setTranscription(strings[3]);
                    new_word.setEnWord(strings[1]);
                    new_word.setExample(strings[9].replace("&rsquo;", "'"));
                    new_word.setExampleTranslate(strings[11]);
                    words.add(new_word);
                }

            } while (pos_next != -1);
            return words;
        } catch (IOException e1) {
            return null;
        }
    }

    public StudyNowWordDataProvider(String folderURL) {
        this.folderURL = folderURL;

    }


}
