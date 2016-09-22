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
public interface ISemiringScalarProductCalculator {
    
    /**
     * Here the FIRST vector is assumed to have the 0/1 elements
     * @param firstVectorWithCommonElements
     * @param secondVector
     * @return 
     */
    SemiringObject CalculateScalarProductWithLooseMultiplication(boolean[] indicatorElements, SemiringObject[] secondVector, ISemiringFeature semiringFeature);
    
    
    SemiringObject CalculateScalarProductWithTightMultiplication(SemiringObject[] firstVector, SemiringObject[] secondVector, ISemiringFeature semiringFeature);    
}
