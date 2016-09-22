/* 
 * File:   DiscreteMatrix.cpp
 * Author: sam
 * 
 * Created on May 6, 2015, 12:57 PM
 */

#include "DiscreteMatrix.h"
#include "DiscreteVectorStorageInfo.h"

DiscreteMatrix::DiscreteMatrix(int numRows, int numColumns, DiscreteVectorStorageInfo* discreteVectorStorageInfo) {
    this->numRows = numRows;
    this->numCols = numColumns;    
    int numUlongElementsRequiredToRepresentAllEntries = discreteVectorStorageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal*numRows;
    this->entries = new unsigned long long[numUlongElementsRequiredToRepresentAllEntries](); // Note we initialize to zero here
    this->numBytesUsedToRepresentAllEntries = discreteVectorStorageInfo->numBytesRequiredToRepresentOneRowVectorInTotal*numRows;
}

DiscreteMatrix::DiscreteMatrix(const DiscreteMatrix& orig) {
}

DiscreteMatrix::~DiscreteMatrix() {
    delete[] this->entries;    
}

void DiscreteMatrix::SetValue(int rowIndex, int colIndex, int value, DiscreteVectorStorageInfo* discreteVectorStorageInfo) {
    // Our setterInfos are the same for each row vector, so we get the setterInfo by column index
    // When using it we then need to be careful to offset by the appropriate row index
    
    int rowOffset = rowIndex*discreteVectorStorageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal;
    DiscreteVectorValueSetterInfo* setterInfo = discreteVectorStorageInfo->setterInfos[colIndex][value];
    for (int i = 0; i < setterInfo->numDiscreteVectorElementIndicesToSet; i++) {
        int indexOfUlongToModify = setterInfo->discreteVectorElementIndicesToSet[i];
        // To actually set the value, we have to AND the vector ULONG entry with our "clearing" mask, then
        // OR it with our "setting" mask
        int indexIntoEntries = rowOffset + indexOfUlongToModify;
        this->entries[indexIntoEntries] = this->entries[indexIntoEntries] & setterInfo->correspondingBitwiseAndMasksToUseForSetting[indexOfUlongToModify];
        this->entries[indexIntoEntries] = this->entries[indexIntoEntries] | setterInfo->correspondingBitwiseOrMasksToUseForSetting[indexOfUlongToModify];
    }
}

int DiscreteMatrix::GetValue(int rowIndex, int colIndex, DiscreteVectorStorageInfo* discreteVectorStorageInfo) {
    // For getting the value, we can use a combination of masks and the popcnt intrinsic
    // E.g. If our DiscreteVector is represented by two vector elements:                    00..011, 110...0
    // and the mask that indicates which bits are used to store the value in question is:   00..001, 100...0
    // then we can first AND our mask with the vector to give:                              00..001, 100...0
    // and then do a popcount across the entire discrete vector, which will give 2 (two bits set)
    // and then we know that represents three on the ordinal scale, so we return that

    // Our getterInfos are the same for each row vector, so we get the getterInfo by column index
    // When using it we then need to be careful to offset by the appropriate row index
    int rowOffset = rowIndex*discreteVectorStorageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal;
    DiscreteVectorValueGetterInfo* getterInfo = discreteVectorStorageInfo->getterInfos[colIndex];
    int popCnt = 0;
    for (int i = 0; i < getterInfo->numDiscreteVectorElementIndicesToConsider; i++) {
        int indexOfUlongToModify = getterInfo->discreteVectorElementIndicesToConsider[i];
        unsigned long long andResult = this->entries[rowOffset+ indexOfUlongToModify] & getterInfo->correspondingBitwiseAndMasksToUseForClearing[indexOfUlongToModify];
        popCnt += __builtin_popcountll(andResult);
    }
    return popCnt;
}

unsigned long long* DiscreteMatrix::GetPointerToRow(int rowIndex, DiscreteVectorStorageInfo* discreteVectorStorageInfo) {
    return (this->entries + (rowIndex * discreteVectorStorageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal));
}
