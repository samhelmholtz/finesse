/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.util.List;

/**
 * Returns true if the usage matrix must be binary. This is the case if 1) the data matrix has heterogeneous features, 
 * or 2) there exists one or more features for which multiplication is not defined.
 * @author sam
 */
public interface IUsageMatrixSetTypeDeterminer {
    boolean UsageMatrixCannotUseFeatureObjects(ISemiringFeature[] semiringFeatures);
}
