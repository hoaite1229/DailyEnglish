package org.skv.dailyenglish;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/**
 * Created by SKybro on 2017-06-06.
 */

@DynamoDBTable(tableName = "Words")
public class Word {
        private int number;
        private String word;
        private String pronunciation;
        private String meaning;
        private String sentence;

        @DynamoDBHashKey(attributeName = "Number")
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @DynamoDBAttribute(attributeName = "Word")
        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        @DynamoDBAttribute(attributeName = "Pronunciation")
        public String getPronunciation() {
            return pronunciation;
        }

        public void setPronunciation(String pronunciation) {
            this.pronunciation = pronunciation;
        }

        @DynamoDBAttribute(attributeName = "Meaning")
        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }

        @DynamoDBAttribute(attributeName = "Sentence")
        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }
}
