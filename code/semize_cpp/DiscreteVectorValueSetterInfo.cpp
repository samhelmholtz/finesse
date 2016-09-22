/* 
 * File:   DiscreteVectorValueSetterInfo.cpp
 * Author: sam
 * 
 * Created on April 22, 2015, 10:53 AM
 */

#include "DiscreteVectorValueSetterInfo.h"

DiscreteVectorValueSetterInfo::DiscreteVectorValueSetterInfo(int indexOfElementToSetFromZeroToVectorDimensionMinusOne, int valueToSet, int numScaleValues, int numUlonglongElementsRequiredToRepresentVectorInTotal){
    // So the basic idea here is that we're trying to setup a set of masks that can be used to set a value in a DiscreteVector
    // Setting a value requires a bitwise AND to clear the appropriate bits, then a bitwise OR to set the appropriate bits. As we're only setting one value in the vector, most of the bits in the 
    // DiscreteVector won't be touched. Hence we do it a bit more efficiently: we simply record the parts of the discretevector that need
    // to be updated (OR'd) in order to have the value persisted, and the masks required to actually do that for each part.
    
    int bitWidth = sizeof(unsigned long long)*8; // typically gives 64
    int widthOfAValueInBits = numScaleValues - 1;
    
    // First we need to find the index of the start and end bits that we need to set to 1, in the absolute sense (i.e. assuming we didn't have to break it down into different 64-bit ulongs)
    int endingBitIndexInclusiveForValueToBeSetToOne = (indexOfElementToSetFromZeroToVectorDimensionMinusOne+1)*widthOfAValueInBits - 1;
    int startingBitIndexInclusiveForValueToBeSetToOne = endingBitIndexInclusiveForValueToBeSetToOne - valueToSet + 1;
        
    // Also find the indices of the bits that represent this full value (i.e. widthOfAValueInBits wide)
    // These bits need to be cleared with a bitwise AND before doing the OR
    int startingBitIndexInclusiveForValueToBeCleared = indexOfElementToSetFromZeroToVectorDimensionMinusOne*widthOfAValueInBits;
    int endingBitIndexInclusiveForValueToBeCleared = startingBitIndexInclusiveForValueToBeCleared + widthOfAValueInBits - 1;
    
    // Based on these indices, we can find the appropriate parts (elements) of the DiscreteVector that need to be modified
    int smallestIndexInclusiveOfAnElementThatNeedsToBeModified = startingBitIndexInclusiveForValueToBeSetToOne / bitWidth;
    int largestIndexInclusiveOfAnElementThatNeedsToBeModified = endingBitIndexInclusiveForValueToBeSetToOne / bitWidth;
    
    this->numDiscreteVectorElementIndicesToSet = largestIndexInclusiveOfAnElementThatNeedsToBeModified - smallestIndexInclusiveOfAnElementThatNeedsToBeModified + 1;
    this->discreteVectorElementIndicesToSet = new int[this->numDiscreteVectorElementIndicesToSet];
    for (int i = 0; i < this->numDiscreteVectorElementIndicesToSet; i++) {
        this->discreteVectorElementIndicesToSet[i] = smallestIndexInclusiveOfAnElementThatNeedsToBeModified+i;
    }
    
    // And now the masks. How are we going to do them?
    // We'll just store all the masks, even the ones that are fully zero. It makes the setup easier and doesn't cost us much runtime in the main algorithm.    
    this->correspondingBitwiseOrMasksToUseForSetting = new unsigned long long[numUlonglongElementsRequiredToRepresentVectorInTotal](); // note the parentheses at the end...it means the values are initialized to zero
    this->correspondingBitwiseAndMasksToUseForSetting = new unsigned long long[numUlonglongElementsRequiredToRepresentVectorInTotal](); // note the parentheses at the end...it means the values are initialized to zero
    for (int i = startingBitIndexInclusiveForValueToBeSetToOne; i <= endingBitIndexInclusiveForValueToBeSetToOne; i++) {
        int elementIndex = i/bitWidth;
        int elementOffset = i%bitWidth;
        unsigned long long mask = ((unsigned long long) 1) << (bitWidth - elementOffset - 1);
        this->correspondingBitwiseOrMasksToUseForSetting[elementIndex] = this->correspondingBitwiseOrMasksToUseForSetting[elementIndex] | mask;       
    }
    for (int i = startingBitIndexInclusiveForValueToBeCleared; i <= endingBitIndexInclusiveForValueToBeCleared; i++) {
        int elementIndex = i/bitWidth;
        int elementOffset = i%bitWidth;
        unsigned long long mask = ((unsigned long long)1) << (bitWidth - elementOffset - 1);
        this->correspondingBitwiseAndMasksToUseForSetting[elementIndex] = this->correspondingBitwiseAndMasksToUseForSetting[elementIndex] | mask;       
    }
    // Flip the AND masks so that they have zero in the appropriate places, not one
    for (int i = 0; i < numUlonglongElementsRequiredToRepresentVectorInTotal; i++) {
        this->correspondingBitwiseAndMasksToUseForSetting[i] = ~this->correspondingBitwiseAndMasksToUseForSetting[i];
    }
}

DiscreteVectorValueSetterInfo::DiscreteVectorValueSetterInfo(const DiscreteVectorValueSetterInfo& orig) {
}

DiscreteVectorValueSetterInfo::~DiscreteVectorValueSetterInfo() {
    delete[] this->correspondingBitwiseAndMasksToUseForSetting;
    delete[] this->correspondingBitwiseOrMasksToUseForSetting;
    delete[] this->discreteVectorElementIndicesToSet;
}