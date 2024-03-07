package com.andrewlowman.webscrapermodular.Utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class FileOut implements Serializable {

    public void save(int ctr, int person){
        try{
            ArrayList list = new ArrayList();
            list.add(ctr);
            list.add(person);
            FileOutputStream fileOutputStream = new FileOutputStream("filesave1.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
            //System.out.println("Wrote to the file");
        }catch(IOException i){
            i.printStackTrace();
        }
        //System.out.println("I am saving");
    }

    public void saveDepartmentName(String departmentName){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("filesaveDept1.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(departmentName);
            objectOutputStream.close();
            fileOutputStream.close();
            // System.out.println("Wrote to the file");
        }catch(IOException i){
            i.printStackTrace();
        }
        //System.out.println("I am saving");
    }
}
