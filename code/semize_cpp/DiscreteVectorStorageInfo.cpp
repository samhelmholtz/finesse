/* 
 * File:   DiscreteVectorStorageInfo.cpp
 * Author: sam
 * 
 * Created on May 6, 2015, 1:13 PM
 */

#include "DiscreteVectorStorageInfo.h"

DiscreteVectorStorageInfo::DiscreteVectorStorageInfo(int vectorDimension, int numDiscreteValues) {
    this->vectorDimension = vectorDimension;
    this->numDiscreteValues = numDiscreteValues;
    
    
    
    int numBytesInUlonglong = sizeof (unsigned long long);
    int numBitsInUlonglong = numBytesInUlonglong * 8;
    int numBitsRequiredToRepresentOneVectorValue = (numDiscreteValues - 1);
    int numBitsRequiredToRepresentOneRowVector = numBitsRequiredToRepresentOneVectorValue*vectorDimension;
    int numNormalChunksIn128Bits = 128 / numBitsInUlonglong; // usually gives 2
    
    // We have to make sure that each row is represented by a number of ULONG elements that is 
    // exactly divisible by numNormalChunksIn128Bits
    this->numUlonglongElementsRequiredToRepresentVectorInTotal = (numBitsRequiredToRepresentOneRowVector + numBitsInUlonglong - 1) / numBitsInUlonglong;
    // Pad the vector out to fill the required number of ulongs if required
    if(this->numUlonglongElementsRequiredToRepresentVectorInTotal%numNormalChunksIn128Bits != 0){
        this->numUlonglongElementsRequiredToRepresentVectorInTotal+= (numNormalChunksIn128Bits - (this->numUlonglongElementsRequiredToRepresentVectorInTotal%numNormalChunksIn128Bits));
    }
    this->numBytesRequiredToRepresentOneRowVectorInTotal = numUlonglongElementsRequiredToRepresentVectorInTotal*numBytesInUlonglong;
    
    this->num128BitChunksInVector = this->numUlonglongElementsRequiredToRepresentVectorInTotal / numNormalChunksIn128Bits;    
    
    this->setterInfos = new DiscreteVectorValueSetterInfo**[vectorDimension];
    for (int i = 0; i < vectorDimension; i++) {
        this->setterInfos[i] = new DiscreteVectorValueSetterInfo*[numDiscreteValues];
        for (int j = 0; j < numDiscreteValues; j++) {
            this->setterInfos[i][j] = new DiscreteVectorValueSetterInfo(i, j, numDiscreteValues, this->numUlonglongElementsRequiredToRepresentVectorInTotal);
        }
    }
    
    this->getterInfos = new DiscreteVectorValueGetterInfo*[vectorDimension];
    for (int i = 0; i < vectorDimension; i++) {
        this->getterInfos[i] = new DiscreteVectorValueGetterInfo(i, numDiscreteValues, this->numUlonglongElementsRequiredToRepresentVectorInTotal);
    }
}

DiscreteVectorStorageInfo::DiscreteVectorStorageInfo(const DiscreteVectorStorageInfo& orig) {
}

DiscreteVectorStorageInfo::~DiscreteVectorStorageInfo() {
    
    for (int i = 0; i < vectorDimension; i++) {
        delete this->getterInfos[i];
    }
    delete[] this->getterInfos;
    
    for (int i = 0; i < vectorDimension; i++) {        
        for (int j = 0; j < numDiscreteValues; j++) {
            delete this->setterInfos[i][j];
        }
        delete[] this->setterInfos[i];
    }
    
    delete[] this->setterInfos;
}

