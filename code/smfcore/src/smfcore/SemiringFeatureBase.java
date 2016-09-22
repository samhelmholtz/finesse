/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

/**
 * Here we implement the operations that are common to all semirings.
 * @author sam
 */
public abstract class SemiringFeatureBase implements ISemiringFeature{   
    
    @Override
    public SemiringObject LooseMultiplication(boolean indicator, SemiringObject second){
        return indicator ? second : GetIdentityElementForAddition();
    }    
}
