/* 
 * File:   DiscreteVectorValueGetterInfo.cpp
 * Author: sam
 * 
 * Created on April 28, 2015, 4:12 PM
 */

#include "DiscreteVectorValueGetterInfo.h"

DiscreteVectorValueGetterInfo::DiscreteVectorValueGetterInfo(int indexOfElementToGetFromZeroToVectorDimensionMinusOne, int numScaleValues, int numUlonglongElementsRequiredToRepresentVectorInTotal) {
    // So the basic idea here is that we're trying to setup a set of masks that can be used to 'clear' the unnecessary elements from a vector element, so that we can use the popcnt function to count the bits a value in a DiscreteVector    

    int bitWidth = sizeof (unsigned long long)*8; // typically gives 64
    int widthOfAValueInBits = numScaleValues - 1;

    // First we need to find the index of the start and end bits that we need to focus on when getting the value, in the absolute sense (i.e. assuming we didn't have to break it down into different 64-bit ulongs)
    int startingBitAbsoluteIndexInclusive = indexOfElementToGetFromZeroToVectorDimensionMinusOne*widthOfAValueInBits;
    int endingBitAbsoluteIndexInclusive = (indexOfElementToGetFromZeroToVectorDimensionMinusOne + 1) * widthOfAValueInBits - 1;

    // Based on these indices, we can find the appropriate parts (elements) of the DiscreteVector that need to be modified
    int smallestIndexInclusiveOfAnElementThatNeedsToBeModified = startingBitAbsoluteIndexInclusive / bitWidth;
    int largestIndexInclusiveOfAnElementThatNeedsToBeModified = endingBitAbsoluteIndexInclusive / bitWidth;

    this->numDiscreteVectorElementIndicesToConsider = largestIndexInclusiveOfAnElementThatNeedsToBeModified - smallestIndexInclusiveOfAnElementThatNeedsToBeModified + 1;
    this->discreteVectorElementIndicesToConsider = new int[this->numDiscreteVectorElementIndicesToConsider];
    for (int i = 0; i < this->numDiscreteVectorElementIndicesToConsider; i++) {
        this->discreteVectorElementIndicesToConsider[i] = smallestIndexInclusiveOfAnElementThatNeedsToBeModified + i;
    }

    this->correspondingBitwiseAndMasksToUseForClearing = new unsigned long long[numUlonglongElementsRequiredToRepresentVectorInTotal](); // note the parentheses at the end...it means the values are initialized to zero
    for (int i = startingBitAbsoluteIndexInclusive; i <= endingBitAbsoluteIndexInclusive; i++) {
        int elementIndex = i / bitWidth;
        int elementOffset = i % bitWidth;
        unsigned long long mask = ((unsigned long long) 1) << (bitWidth - elementOffset - 1);
        this->correspondingBitwiseAndMasksToUseForClearing[elementIndex] = this->correspondingBitwiseAndMasksToUseForClearing[elementIndex] | mask;
    }
}

DiscreteVectorValueGetterInfo::DiscreteVectorValueGetterInfo(const DiscreteVectorValueGetterInfo& orig) {
}

DiscreteVectorValueGetterInfo::~DiscreteVectorValueGetterInfo() {
    delete[] this->correspondingBitwiseAndMasksToUseForClearing;
    delete[] this->discreteVectorElementIndicesToConsider;
}

