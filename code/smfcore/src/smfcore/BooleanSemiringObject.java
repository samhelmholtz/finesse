/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

/**
 *
 * @author sam
 */
public class BooleanSemiringObject extends SemiringObject {
    
    public BooleanSemiringObject(Boolean value){
        BooleanValue = value;
    }
    public BooleanSemiringObject(String value){
        BooleanValue = (value.equals("1"));
    }
    
    public final Boolean BooleanValue;

    @Override
    public String GetStringRepresentation() {
        return BooleanValue ? "1" : "0";
    }
}
