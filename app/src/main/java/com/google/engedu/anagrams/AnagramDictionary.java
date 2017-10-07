/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.jar.Pack200;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordlength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String,ArrayList<String>> lettersToWord = new HashMap<String,ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeTowords = new HashMap<Integer, ArrayList<String>>();
    ArrayList<String> wordlist = new ArrayList<String>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            int n=word.length();
            wordlist.add(word);
            wordSet.add(word);
            String s_word=sortLetters(word);
            if(!sizeTowords.containsKey(n)){
                ArrayList<String> a1 = new ArrayList<String>();
                a1.add(word);
                sizeTowords.put(n,a1);
            }
            else{
                ArrayList<String> a1 = sizeTowords.get(n);
                a1.add(word);
                sizeTowords.put(n,a1);
            }
            if(!lettersToWord.containsKey(s_word)){
                ArrayList<String> a1 = new ArrayList<String>();
                a1.add(word);
                lettersToWord.put(s_word,a1);
            }
            else{
                ArrayList<String> a1 = lettersToWord.get(s_word);
                a1.add(word);
                lettersToWord.put(s_word,a1);
            }
        }
    }

    public String sortLetters(String s)
    {
        char c[] = s.toCharArray();
        Arrays.sort(c);
        String sorted = new String(c);
        return sorted;
    }

    public boolean isGoodWord(String word, String base) {
        if( !(word.contains(base)) && wordSet.contains(word))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String st = sortLetters(targetWord);
        /*int n=targetWord.length();
        for(String temp:words){
            if(temp.length()==n){
                if(st.equals(sortLetters(temp))){
                    result.add(temp);
                }
            }
        }*/
        result=lettersToWord.get(st);
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char i='a'; i<='z' ; i++){

            String temp=sortLetters(word+i);

            if(lettersToWord.containsKey(temp)){
                ArrayList<String> tempal = lettersToWord.get(temp);
                for(String j:tempal){
                    if(isGoodWord(j,word)){
                        result.add(j);
                    }
                }
            }

        }
        return result;
    }

    public String pickGoodStarterWord() {
            int temp_random;
            ArrayList<String> list = sizeTowords.get(wordlength);
            temp_random = random.nextInt(list.size());
            int i = temp_random;
            String temp=list.get(temp_random);
            String temp1 = sortLetters(temp);
            while (lettersToWord.get(temp1).size() != MIN_NUM_ANAGRAMS) {
                temp_random++;
                temp_random %= list.size();
                if (temp_random == i) {
                    break;
                }
                temp = list.get(temp_random);
                temp1 = sortLetters(temp);
            }
            if((wordlength+1)<=(MAX_WORD_LENGTH)){
                wordlength=wordlength+1;
            }
            return temp;
    }
}
