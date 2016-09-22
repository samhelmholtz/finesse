/* 
 * File:   DiscreteVectorValueGetterInfo.h
 * Author: sam
 *
 * Created on April 28, 2015, 4:12 PM
 */

#ifndef DISCRETEVECTORVALUEGETTERINFO_H
#define	DISCRETEVECTORVALUEGETTERINFO_H

class DiscreteVectorValueGetterInfo {
public:
    int numDiscreteVectorElementIndicesToConsider;
    int* discreteVectorElementIndicesToConsider;    
    unsigned long long* correspondingBitwiseAndMasksToUseForClearing;
    DiscreteVectorValueGetterInfo(int indexOfElementToGetFromZeroToVectorDimensionMinusOne, int numScaleValues, int numUlonglongElementsRequiredToRepresentVectorInTotal);
    DiscreteVectorValueGetterInfo(const DiscreteVectorValueGetterInfo& orig);
    virtual ~DiscreteVectorValueGetterInfo();
};

#endif	/* DISCRETEVECTORVALUEGETTERINFO_H */

