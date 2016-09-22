/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import com.google.gson.Gson;

import java.util.Set;

/**
 *
 * @author sam
 */
public class TreeSemiringObject extends SemiringObject {

    public TreeSemiringObject(Set<String> value) {
        TreeValue = value;
    }
    
    public final Set<String> TreeValue;

    @Override
    public String GetStringRepresentation() {

        // A single tree node is in the format: type1|type2$=$fullName=>type3|type4$=$fullName
        // We store the collection as a JSON object
        Gson gson = new Gson();
        return gson.toJson(TreeValue);
    }
}
