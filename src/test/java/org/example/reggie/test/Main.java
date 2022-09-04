package org.example.reggie.test;

public class Main {
    //    左子串中0的相对数量最多时，得分最高
    public int maxScore(String s) {
        int max = 0;
        int tmp = 0;
        int mid = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (++tmp > max) {
                    max = tmp;
                    mid = i;
                }
            } else {
                tmp--;
            }
        }
        return getScore(s, mid);
    }

    public int getScore(String s, int index) {
        int result = 0;
        for (int i = 0; i <= index; i++) {
            if (s.charAt(i) == '0') {
                result++;
            }
        }
        for (int i = index + 1; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                result++;
            }
        }
        return index == s.length() - 1 ? --result : result;

    }

    public static void main(String[] args) {
        int score = new Main().maxScore("011101");
        System.out.println(score);
    }
}