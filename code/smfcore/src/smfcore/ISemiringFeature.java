/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.util.List;

/**
 *
 * @author sam
 */
public interface ISemiringFeature {        
    
        /**
     * Returns true if this semiring supports tight conjunction (e.g. Boolean, ordinal semirings)
     * @return 
     */
    boolean HasTightMultiplication();      
    
    /**
     * Returns the name of this semiring (e.g. Boolean, ordinal, ternary).
     * @return 
     */
    String Name();

    SemiringObject DeserializeObject(String value);

    String Serialize();
    
    /**
     * Returns the identity element for the semiring's addition operation.
     * @return 
     */
    SemiringObject GetIdentityElementForAddition();
        
    /**
     * Symmetric, closed binary operator.
     * @param first
     * @param second
     * @return 
     */
    SemiringObject Addition(SemiringObject first, SemiringObject second);
    
    /**
     * Closed binary operator for multiplication (only call this if HasTightMultiplication is true)
     * @param first
     * @param second
     * @return 
     */
    SemiringObject TightMultiplication(SemiringObject first, SemiringObject second);
        
    /**
     * Loose multiplication (indicator only). Returns 'second' if indicator is true, otherwise returns 
     * @param indicator
     * @param second
     * @return 
     */
    SemiringObject LooseMultiplication(boolean indicator, SemiringObject second);
    
    /**
     * A real-valued dissimilarity measure between two values. Return value is in [0,1].
     * @param expected
     * @param reconstructed
     * @return 
     */
    double Dissimilarity(SemiringObject expected, SemiringObject reconstructed);

    /**
     * Returns a set of values to try in the next iteration, not including the base value.
     * If T is a tree, for example, this method would return all trees with all possible single-child appendages.
     * @param baseObject
     * @return 
     */
    List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject);
}
