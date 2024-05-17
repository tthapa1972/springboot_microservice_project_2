package com.microservices.productservice;

import java.util.*;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("apple");
        list.add("banana");
        list.add("cherry");

        List<String> res = list.stream().map(str -> {
            char r = Character.toUpperCase(str.charAt(0));
            return r + str.substring(1);
        }).collect(Collectors.toList());

        System.out.println(res);
    }

    public List<String> capitalizeInitialLetter(List<String> list){
        List<String> res = list.stream().map(str -> {
            char r = Character.toUpperCase(str.charAt(0));
            return r + str.substring(1);
        }).collect(Collectors.toList());

        return res;
    }
    private static String find(String word){
        /*
        kyckyckk - kyc s
        pnnben - nbe p - 1, n - 2
        kyckyckkabcd - kabcd
        * */

        Map<Character, Integer> map = new LinkedHashMap<>();
        int start = 0;
        int actualStart = 0;
        int end = 0;
        int n = word.length();
        int maxLength = Integer.MIN_VALUE;

        for(int i = 0;i < n;i++){
            char ch = word.charAt(i);
            if (map.containsKey(ch)){
                int index = map.get(ch);
                if(i == (index + 1)){
                    map.clear();
                    start = i;
                }else {
                    map.remove(ch);
                    start++;
                }
            }

            map.put(ch, i);
            int len = i - start + 1;
            if(maxLength < len){
                end = i;
                actualStart = start;
                maxLength = len;
            }

        }


        return word.substring(actualStart, end+1);
    }

}
