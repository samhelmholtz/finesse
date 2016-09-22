package smfcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sam on 20.01.15.
 */
public class CommonSemiringFeature implements ISemiringFeature {
    public static final String NAME = "Common";

    @Override
    public boolean HasTightMultiplication() {
        return false;
    }

    @Override
    public String Name() {
        return NAME;
    }

    @Override
    public SemiringObject DeserializeObject(String value) {
        return new CommonSemiringObject(value);
    }

    @Override
    public String Serialize() {
        return NAME;
    }

    @Override
    public SemiringObject GetIdentityElementForAddition() {
        return new CommonSemiringObject(false);
    }

    @Override
    public SemiringObject Addition(SemiringObject first, SemiringObject second) {
        if(((CommonSemiringObject)first).BooleanValue){
            return first;
        }
        return second;
    }

    @Override
    public SemiringObject TightMultiplication(SemiringObject first, SemiringObject second) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SemiringObject LooseMultiplication(boolean indicator, SemiringObject second) {
        if(indicator)
            return second;
        return new CommonSemiringObject(false);
    }

    @Override
    public double Dissimilarity(SemiringObject expected, SemiringObject reconstructed) {
        return ((CommonSemiringObject)expected).BooleanValue == ((CommonSemiringObject)reconstructed).BooleanValue ? 0 : 1;
    }

    @Override
    public List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject) {
        if(((CommonSemiringObject)baseObject).BooleanValue)
            return new ArrayList<>();
        return Arrays.asList((SemiringObject) new CommonSemiringObject(true));
    }
}
