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
public class OrdinalSemiringObject extends SemiringObject {

    public OrdinalSemiringObject(Integer value) {
        IntegerValue = value;
    }
    public OrdinalSemiringObject(String value){IntegerValue = Integer.parseInt(value);}

    public final Integer IntegerValue;

    @Override
    public String GetStringRepresentation() {
        return IntegerValue + "";
    }
}
