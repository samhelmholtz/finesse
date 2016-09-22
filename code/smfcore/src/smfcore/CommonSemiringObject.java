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
public class CommonSemiringObject extends SemiringObject {

    public CommonSemiringObject(boolean BooleanValue) {
        this.BooleanValue = BooleanValue;
    }
    public CommonSemiringObject(String value){
        BooleanValue = (value.equals("1"));
    }
        
    public boolean BooleanValue;

    @Override
    public String GetStringRepresentation() {
        return BooleanValue ? "1" : "0";
    }
}
