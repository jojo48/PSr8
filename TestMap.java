/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Samreng
 */
public class TestMap {

//package com.java.myapp;
//import java.util.HashMap;
//import java.util.Iterator;
//public class MyClass {
    public static void main(String[] args) {
        String key1 = "Company";
        String val1 = "CAT Telecom PCL.";
        HashMap<String, String> map = new HashMap<String, String>();

        map = new HashMap<String, String>();
        map.put("MemberID", "1");
        map.put("Name", "Weerachai");
        map.put("Tel", "0819876107");
        map.put(key1, val1);

        Iterator<String> Vmap = map.keySet().iterator();
        while (Vmap.hasNext()) {
            String key = (String) (Vmap.next());  // Key
            String val = map.get(key); // Value
            System.out.println(key + " = " + val);
        }

        map.clear();
        


//##############################################################################################################################

        System.out.println("###################################### Test ArrayList #############################################");

        String country = "Belgium,France,Italy,Germany,Spain";
        String[] arr = country.split(",");

//        ArrayList<String> arrList = new ArrayList<String>(); 
        ArrayList<String> arrList = new ArrayList<>();

//        for (String a : arr) {
        arrList.addAll(Arrays.asList(arr));

        for (int i = 0; i < arrList.size(); i++) {
            System.out.println("arrList index[" + i + "] = " + arrList.get(i));
        }

        System.out.println("========================================");

//        for (String temp : arrList) {
        arrList.stream().forEach((temp) -> {
            System.out.println(temp);
        });
        
        
        arrList.clear();
    }

}
