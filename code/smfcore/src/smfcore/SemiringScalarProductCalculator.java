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
public class SemiringScalarProductCalculator implements ISemiringScalarProductCalculator {

    @Override
    public SemiringObject CalculateScalarProductWithLooseMultiplication(boolean[] indicatorElements, SemiringObject[] secondVector, ISemiringFeature semiringFeature) {
        SemiringObject result = semiringFeature.GetIdentityElementForAddition();
        int vectorLength = indicatorElements.length;
        for (int i = 0; i < vectorLength; i++) {
            result = semiringFeature.Addition(result, semiringFeature.LooseMultiplication(indicatorElements[i], secondVector[i]));
        }

        return result;
    }

    @Override
    public SemiringObject CalculateScalarProductWithTightMultiplication(SemiringObject[] firstVector, SemiringObject[] secondVector, ISemiringFeature semiringFeature) {
        SemiringObject result = semiringFeature.GetIdentityElementForAddition();
        int vectorLength = firstVector.length;
        for (int i = 0; i < vectorLength; i++) {
            result = semiringFeature.Addition(result, semiringFeature.TightMultiplication(firstVector[i], secondVector[i]));
        }

        return result;                
    }

}
