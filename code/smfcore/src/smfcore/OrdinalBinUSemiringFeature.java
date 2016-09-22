package smfcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 19.05.15.
 */
public class OrdinalBinUSemiringFeature extends SemiringFeatureBase {

    public static final String NAME = "OrdinalBinU";
    private static final SemiringObject FALSE = new OrdinalSemiringObject(0);
    private static final List<SemiringObject> EMPTY_SET = new ArrayList<>();
    private final Integer _maxScaleValue;
    public final Integer NumScaleValues;
    private final Map<Integer, List<SemiringObject>> _incrementalSubsets;

    public OrdinalBinUSemiringFeature(Integer numScaleValues) {
        NumScaleValues = numScaleValues;
        _maxScaleValue = numScaleValues - 1;

        // Now we predefine and cache the relevant incremental subsets we might need.
        _incrementalSubsets = new HashMap<>();
        for (int i = 0; i < _maxScaleValue; i++) {
            int sizeOfSubset = _maxScaleValue- i;
            ArrayList<SemiringObject> subset = new ArrayList<>(sizeOfSubset);
            for (int j = 0; j < sizeOfSubset; j++) {
                subset.add(j, new OrdinalSemiringObject(i + j + 1));
            }
            _incrementalSubsets.put(i, subset);
        }
        _incrementalSubsets.put(_maxScaleValue, EMPTY_SET);
    }

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
        if (((OrdinalSemiringObject) first).IntegerValue > ((OrdinalSemiringObject) second).IntegerValue) {
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
        int expectedInt = ((OrdinalSemiringObject) expected).IntegerValue;
        int reconstructedInt = ((OrdinalSemiringObject) reconstructed).IntegerValue;

        int difference = expectedInt > reconstructedInt ? expectedInt - reconstructedInt : reconstructedInt - expectedInt;
        return (double) difference / (double) _maxScaleValue;
    }

    @Override
    public List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject) {
        return _incrementalSubsets.get(((OrdinalSemiringObject) baseObject).IntegerValue);
    }

    @Override
    public String Name() {
        return NAME;
    }

    @Override
    public SemiringObject DeserializeObject(String value) {
        return new OrdinalSemiringObject(value);
    }

    @Override
    public String Serialize() {
        return NAME + "," + NumScaleValues;
    }
}
