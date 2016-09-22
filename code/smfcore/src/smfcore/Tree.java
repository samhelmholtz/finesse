/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

/**
 *
 * @author sam
 */
public class Tree {
    public String Id;
    public HashMap<String, Tree> Children;
    
    public Tree(String id){
        Id = id;
        Children = new HashMap<>();
    }

    public void PutChild(Tree tree){
        Children.put(tree.Id, tree);
    }

    public static String ToJson(Tree tree){
        Gson gson = new Gson();
        return gson.toJson(tree);
    }

    public static Tree FromJson(String filename){
        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new FileReader(filename);
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileContent = sb.toString();
        return gson.fromJson(fileContent, Tree.class);
    }
}
