package org.skv.dailyenglish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {
    public static Integer[] numbers;
    public static String[] words;
    public static String[] pronunciations;
    public static String[] meanings;
    public static String[] sentences;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_NUMBER = "Number";
    public static final String KEY_WORD = "Word";
    public static final String KEY_PRONUNCIATION = "Pronunciation";
    public static final String KEY_MEANING = "Meaning";
    public static final String KEY_SENTENCE = "Sentence";

    private JSONArray wordList = null;

    private  String json;

    public ParseJSON(String json) {
        this.json = json;
    }

    protected void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            wordList = jsonObject.getJSONArray(JSON_ARRAY);

            numbers = new Integer[wordList.length()];
            words = new String[wordList.length()];
            pronunciations = new String[wordList.length()];
            meanings = new String[wordList.length()];
            sentences = new String[wordList.length()];

            for(int i=0; i<wordList.length(); i++) {
                JSONObject jo = wordList.getJSONObject(i);
                numbers[i] = jo.getInt(KEY_NUMBER);
                words[i] = jo.getString(KEY_WORD);
                pronunciations[i] = jo.getString(KEY_PRONUNCIATION);
                meanings[i] = jo.getString(KEY_MEANING);
                sentences[i] = jo.getString(KEY_SENTENCE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
