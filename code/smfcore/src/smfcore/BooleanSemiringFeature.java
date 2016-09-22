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
 *
 * @author sam
 */
public class BooleanSemiringFeature extends SemiringFeatureBase {

    public static final String NAME = "Boolean";
    private static final SemiringObject FALSE_OBJECT = new BooleanSemiringObject(false);
    private static final SemiringObject TRUE_OBJECT = new BooleanSemiringObject(true);
    private static final List<SemiringObject> NO_BOOLS = new ArrayList<>();
    private static final List<SemiringObject> TRUE_BOOL = Arrays.asList(TRUE_OBJECT);

    @Override
    public boolean HasTightMultiplication() {
        return true;
    }

    @Override
    public SemiringObject GetIdentityElementForAddition() {
        return (SemiringObject) FALSE_OBJECT;
    }

    @Override
    public SemiringObject Addition(SemiringObject first, SemiringObject second) {
        return (SemiringObject) new BooleanSemiringObject(((BooleanSemiringObject) first).BooleanValue || ((BooleanSemiringObject) second).BooleanValue);
    }

    @Override
    public SemiringObject TightMultiplication(SemiringObject first, SemiringObject second) {
        return (SemiringObject) new BooleanSemiringObject(((BooleanSemiringObject) first).BooleanValue && ((BooleanSemiringObject) second).BooleanValue);
    }

    @Override
    public double Dissimilarity(SemiringObject expected, SemiringObject reconstructed) {
        return Objects.equals(((BooleanSemiringObject) expected).BooleanValue, ((BooleanSemiringObject) reconstructed).BooleanValue) ? 0 : 1;
    }

    @Override
    public List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject) {
        return ((BooleanSemiringObject) baseObject).BooleanValue ? NO_BOOLS : TRUE_BOOL;
    }

    @Override
    public String Name() {
        return NAME;
    }

    @Override
    public SemiringObject DeserializeObject(String value) {
        return new BooleanSemiringObject(value);
    }

    @Override
    public String Serialize() {
        return NAME;
    }
}
