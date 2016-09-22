/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * We use the value 0 for false, 1 for unknown and 2 for true here.
 *
 * @author sam
 */
public class TernarySemiringFeature extends SemiringFeatureBase {

    public static final String NAME = "Ternary";
    private static final Byte FALSE_BYTE = 0;
    private static final Byte UNKNOWN_BYTE = 1;
    private static final Byte TRUE_BYTE = 2;
    private static final SemiringObject FALSE = new TernarySemiringObject(FALSE_BYTE);
    private static final SemiringObject UNKNOWN = new TernarySemiringObject(UNKNOWN_BYTE);
    private static final SemiringObject TRUE = new TernarySemiringObject(TRUE_BYTE);
    private static final List<SemiringObject> EMPTY_SET = new ArrayList<>();
    private static final List<SemiringObject> UNKNOWN_AND_TRUE_SET = Arrays.asList(UNKNOWN, TRUE);
    private static final List<SemiringObject> TRUE_SET = Arrays.asList(TRUE);

    @Override
    public boolean HasTightMultiplication() {
        return false;
    }

    @Override
    public SemiringObject GetIdentityElementForAddition() {
        return FALSE;
    }

    @Override
    public SemiringObject Addition(SemiringObject first, SemiringObject second) {
        if (((TernarySemiringObject) first).TernaryValue > ((TernarySemiringObject) second).TernaryValue) {
            return first;
        }
        return second;
    }

    @Override
    public SemiringObject TightMultiplication(SemiringObject first, SemiringObject second) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double Dissimilarity(SemiringObject expected, SemiringObject reconstructed) {

//        // Here the missing-value dissimilarity
//        if(((TernarySemiringObject) reconstructed).TernaryValue == UNKNOWN_BYTE){
//            return 1;
//        } else if(((TernarySemiringObject) expected).TernaryValue == UNKNOWN_BYTE){
//            return 0;
//        } else if(((TernarySemiringObject) expected).TernaryValue.equals(((TernarySemiringObject) reconstructed).TernaryValue)){
//            return 0;
//        }
//        return 1;

        // Here the unbiased dissimilarity
        if (((TernarySemiringObject) expected).TernaryValue.equals(((TernarySemiringObject) reconstructed).TernaryValue)) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject) {
        if (Objects.equals(((TernarySemiringObject) baseObject).TernaryValue, FALSE_BYTE)) {
            return UNKNOWN_AND_TRUE_SET;
        }
        if (Objects.equals(((TernarySemiringObject) baseObject).TernaryValue, UNKNOWN_BYTE)) {
            return TRUE_SET;
        }
        return EMPTY_SET;
    }

    @Override
    public String Name() {
        return NAME;
    }

    @Override
    public SemiringObject DeserializeObject(String value) {
        return new TernarySemiringObject(value);
    }

    @Override
    public String Serialize() {
        return NAME;
    }
}
