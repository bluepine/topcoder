/*
Q:
   You are given a String[] text, each element of which contains a single word. Encrypt the text by removing all vowels ('a', 'e', 'i', 'o', 'u') from every word that contains at least one non-vowel. If a word consists only of vowels, leave it as is. Return the result as a String[], where the ith element is the encrypted version of the ith element in text.

This problem statement is the exclusive and proprietary property of TopCoder, Inc. Any unauthorized use or reproduction of this information without the prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003, TopCoder, Inc. All rights reserved.

*/


import java.util.*;
public class VowelTest {
    public static void main(String args[]){
        String[] result;
        String[] values = {"he", "who", "is", "greedy", "is", "disgraced"};
        VowelEncryptor v = new VowelEncryptor();
        result = v.vowelEncrypt(values);
        for (int j = 0; j < result.length; j++) {
            System.out.println(result[j]);
        }
    }
}

class VowelEncryptor {
    public String[] vowelEncrypt(String[] text){
        String[] result = new String[text.length];
        int count = 0;
        for (int i = 0; i < text.length; i++) {
            StringBuffer temp = new StringBuffer(text.length);
            for (int j = 0; j < text[i].length(); j++){
                if (!isVowel(text[i].charAt(j))){
                    temp.append(text[i].charAt(j));
                }
                else {
                    count++;
                }
            }
            if (count == text[i].length()){
                result[i] = text[i];
            }
            else {
                result[i] = temp.toString();
            }
            count = 0;
        }
        return result;
    }

    public boolean isVowel(char vowel) {
        char[] vowel_set = {'a', 'e', 'i', 'o','u'};

        for (int i = 0; i < 5; i++) {
            if (vowel_set[i] == vowel) {
                return true;
            }
        }

        return false;
    }

}
