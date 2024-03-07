package com.andrewlowman.webscrapermodular.Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FileIn {

    public ArrayList<Integer> load(){
        ArrayList<Integer> al = null;
        try{
            FileInputStream fileInputStream = new FileInputStream("filesave1.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            al = (ArrayList<Integer>)  objectInputStream.readObject();
            // System.out.println("AL" + al);
            fileInputStream.close();
            objectInputStream.close();
            // System.out.println("File read and closed");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return al;
    }

    public String loadDepartmentName(){
        String al = "";
        try{
            FileInputStream fileInputStream = new FileInputStream("filesaveDept1.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            al = (String) objectInputStream.readObject();
            // System.out.println("AL" + al);
            fileInputStream.close();
            objectInputStream.close();
            // System.out.println("File read and closed");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return al;
    }
}
