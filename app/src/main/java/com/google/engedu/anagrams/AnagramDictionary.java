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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    public static ArrayList<String> wordList = new ArrayList<String>();
    public  static HashMap<String,HashSet<String>> lettersToWord = new HashMap<String,HashSet<String>>();
    public static HashSet wordSet = new HashSet();
    public static HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<Integer,ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            if(lettersToWord.containsKey(sortedWord))
            {
                HashSet<String> list = lettersToWord.get(sortedWord);
                list.add(word);
                lettersToWord.remove(sortedWord);
                lettersToWord.put(sortedWord,list);
            }else {
                HashSet<String> list = new HashSet<String>();
                list.add(word);
                lettersToWord.put(sortedWord,list);
            }
            int wordLength = word.length();
            if(sizeToWords.containsKey(wordLength))
            {
                ArrayList<String> words = sizeToWords.get(wordLength);
                words.add(word);
                sizeToWords.put(wordLength,words);
            }else
            {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                sizeToWords.put(wordLength,list);
            }
            wordList.add(word);
            wordSet.add(word);
        }
    }

    private String sortLetters(String word)
    {
        char[] arr = word.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }

    public boolean isGoodWord(String word, String base)
    {
        if(word.contains(base))
            return false;
        if(wordSet.contains(word.toLowerCase()))
            return true;
         return  false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetters(targetWord);
        for (String word:wordList )
        {
            String sortedNewWord = sortLetters(word);
            if(sortedNewWord.length() == sortedTargetWord.length() && sortedTargetWord.compareToIgnoreCase(sortedNewWord) == 0)
            {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        String sorttedWord = sortLetters(word);
        ArrayList<String> result = new ArrayList<String>();
        for(char alpha = 'a'; alpha <= 'z' ; alpha++)
        {
            String newWord = word+alpha;
            String sorttedNewWord = sortLetters(newWord);
            if(lettersToWord.containsKey(sorttedNewWord))
            {
             result.addAll(lettersToWord.get(sorttedNewWord));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int index = random.nextInt(wordList.size());
        int count = 0;
        String word = "";
        while (true)
        {
            for (int i = index; i <= wordList.size(); i++)
            {
                word = wordList.get(i);
                String sword = sortLetters(word);
                if(lettersToWord.containsKey(sword))
                {
                    if(lettersToWord.get(sword).size() >= MIN_NUM_ANAGRAMS)
                    {
                        count = MIN_NUM_ANAGRAMS;
                        return word;
                    }
                }
            }
            index = 0;
        }
      //  return word;
    }
}
