/* 
 * File:   DiscreteVectorBitMagician.cpp
 * Author: sam
 * 
 * Created on May 6, 2015, 11:11 AM
 */

#include "DiscreteVectorBitMagician.h"
#include "emmintrin.h"
#include "smmintrin.h"

DiscreteVectorBitMagician::DiscreteVectorBitMagician() {
}

DiscreteVectorBitMagician::DiscreteVectorBitMagician(const DiscreteVectorBitMagician& orig) {
}

DiscreteVectorBitMagician::~DiscreteVectorBitMagician() {
}

void DiscreteVectorBitMagician::BitwiseAnd(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo){
    __m128i* v0 = (__m128i*) (vector1);
    __m128i* v1 = (__m128i*) (vector2);
    __m128i* vresult = (__m128i*) (result);
    for (int i = 0; i < discreteVectorStorageInfo->num128BitChunksInVector; i++) {
        vresult[i] = _mm_and_si128(v0[i], v1[i]); // bitwise AND over 128 bits
    }    
}

void DiscreteVectorBitMagician::BitwiseOr(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo){
    __m128i* v0 = (__m128i*) (vector1);
    __m128i* v1 = (__m128i*) (vector2);
    __m128i* vresult = (__m128i*) (result);
    for (int i = 0; i < discreteVectorStorageInfo->num128BitChunksInVector; i++) {
        vresult[i] = _mm_or_si128(v0[i], v1[i]); // bitwise OR over 128 bits
    }    
}

void DiscreteVectorBitMagician::BitwiseXor(unsigned long long* vector1, unsigned long long* vector2, unsigned long long* result, DiscreteVectorStorageInfo* discreteVectorStorageInfo){
    __m128i* v0 = (__m128i*) (vector1);
    __m128i* v1 = (__m128i*) (vector2);
    __m128i* vresult = (__m128i*) (result);
    for (int i = 0; i < discreteVectorStorageInfo->num128BitChunksInVector; i++) {
        vresult[i] = _mm_xor_si128(v0[i], v1[i]); // bitwise XOR over 128 bits
    }    
}

int DiscreteVectorBitMagician::PopCount(unsigned long long* vector, DiscreteVectorStorageInfo* discreteVectorStorageInfo){
    // It doesn't look like we have a 128-bit PopCount instruction :(
    // So we'll have to do it with 64-bit chunks    
    int popCnt = 0;
    for (int i = 0; i < discreteVectorStorageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal; i++) {               
        popCnt += __builtin_popcountll(vector[i]);
    }
    return popCnt;
}