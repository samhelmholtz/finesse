/* 
 * File:   DiscreteVectorStorageInfo.h
 * Author: sam
 *
 * Created on May 6, 2015, 1:13 PM
 */

#ifndef DISCRETEVECTORSTORAGEINFO_H
#define	DISCRETEVECTORSTORAGEINFO_H

#include "DiscreteVectorValueSetterInfo.h"
#include "DiscreteVectorValueGetterInfo.h"


class DiscreteVectorStorageInfo {
public:
    DiscreteVectorStorageInfo(int numVectorEntries, int numDiscreteValues);
    DiscreteVectorStorageInfo(const DiscreteVectorStorageInfo& orig);
    virtual ~DiscreteVectorStorageInfo();
    int vectorDimension;
    int numDiscreteValues;
    int num128BitChunksInVector;    
    int numUlonglongElementsRequiredToRepresentVectorInTotal;
    int numBytesRequiredToRepresentOneRowVectorInTotal;    
    
    // A collection of setter info objects. The size is numVectorEntries*numDiscreteValues
    DiscreteVectorValueSetterInfo*** setterInfos;
    
    // A collection of getter info objects. The size is numVectorEntries
    DiscreteVectorValueGetterInfo** getterInfos;
};

#endif	/* DISCRETEVECTORSTORAGEINFO_H */

