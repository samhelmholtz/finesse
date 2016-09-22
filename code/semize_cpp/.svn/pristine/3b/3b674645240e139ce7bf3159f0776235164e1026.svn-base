/* 
 * File:   DiscreteVectorBitMagician.h
 * Author: sam
 *
 * Created on May 6, 2015, 11:11 AM
 */

#ifndef DISCRETEVECTORBITMAGICIAN_H
#define	DISCRETEVECTORBITMAGICIAN_H

#include "DiscreteVectorStorageInfo.h"

/**
 * This class contains methods for performing bitwise operations on DiscreteVectors
 */
class DiscreteVectorBitMagician {
public:
    DiscreteVectorBitMagician();
    DiscreteVectorBitMagician(const DiscreteVectorBitMagician& orig);
    virtual ~DiscreteVectorBitMagician();
    virtual void BitwiseAnd(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    virtual void BitwiseOr(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    virtual void BitwiseXor(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    int PopCount(unsigned long long* vector, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
};

#endif	/* DISCRETEVECTORBITMAGICIAN_H */

