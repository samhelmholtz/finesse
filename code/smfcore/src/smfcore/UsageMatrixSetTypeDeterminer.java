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
public class UsageMatrixSetTypeDeterminer implements IUsageMatrixSetTypeDeterminer {

    @Override
    public boolean UsageMatrixCannotUseFeatureObjects(ISemiringFeature[] semiringFeatures) {
        String firstName = semiringFeatures[0].Name();
        for (int i = 0; i < semiringFeatures.length; i++) {
            ISemiringFeature semiringFeature = semiringFeatures[i];
            if (!firstName.equals(semiringFeature.Name())) {
                return true;
            }

            if (!semiringFeature.HasTightMultiplication()) {
                return true;
            }
        }

        return false;
    }
}
