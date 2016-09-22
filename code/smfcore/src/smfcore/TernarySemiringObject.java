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
public class TernarySemiringObject extends SemiringObject {
    
    public TernarySemiringObject(Byte value){
        TernaryValue = value;
    }
    public TernarySemiringObject(String value){
        TernaryValue = Byte.parseByte(value);
    }
    
    public final Byte TernaryValue;

    @Override
    public String GetStringRepresentation() {
        return TernaryValue + "";
    }
}
