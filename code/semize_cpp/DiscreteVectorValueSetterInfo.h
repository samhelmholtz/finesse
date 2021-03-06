/* 
 * File:   DiscreteVectorValueSetterInfo.h
 * Author: sam
 *
 * Created on April 22, 2015, 10:53 AM
 */

#ifndef DISCRETEVECTORVALUESETTERINFO_H
#define	DISCRETEVECTORVALUESETTERINFO_H

class DiscreteVectorValueSetterInfo {
public:
    int numDiscreteVectorElementIndicesToSet;
    int* discreteVectorElementIndicesToSet;
    unsigned long long* correspondingBitwiseOrMasksToUseForSetting;
    unsigned long long* correspondingBitwiseAndMasksToUseForSetting;
    DiscreteVectorValueSetterInfo(int indexOfElementToSetFromZeroToVectorDimensionMinusOne, int valueToSet, int numScaleValues, int numUlonglongElementsRequiredToRepresentVectorInTotal);
    DiscreteVectorValueSetterInfo(const DiscreteVectorValueSetterInfo& orig);
    DiscreteVectorValueSetterInfo(int numVectorEntries, int numDiscreteValues);
    virtual ~DiscreteVectorValueSetterInfo();
private:

};

#endif	/* DISCRETEVECTORVALUESETTERINFO_H */

