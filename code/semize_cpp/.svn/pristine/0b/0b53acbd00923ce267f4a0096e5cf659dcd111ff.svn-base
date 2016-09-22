/* 
 * File:   DiscreteMatrix.h
 * Author: sam
 *
 * Created on May 6, 2015, 12:57 PM
 */

#ifndef DISCRETEMATRIX_H
#define	DISCRETEMATRIX_H

#include "DiscreteVectorStorageInfo.h"


class DiscreteMatrix {
public:
    DiscreteMatrix(int numRows, int numColumns, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    DiscreteMatrix(const DiscreteMatrix& orig);
    virtual ~DiscreteMatrix();
    virtual void SetValue(int rowIndex, int colIndex, int value, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    virtual int GetValue(int rowIndex, int colIndex, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    virtual unsigned long long* GetPointerToRow(int rowIndex, DiscreteVectorStorageInfo* discreteVectorStorageInfo);
    
    // This is our pointer to the matrix entries. The full matrix is encoded in binary and represented in a contiguous chunk of
    // memory for cache-efficiency purposes.
    // Each value takes up (numScaleValues-1) bits. This pointer points to the matrix entry in row 1, column 1.
    // After that entry comes the matrix entry in row 1, column 2, and so on.
    // Note we don't pad anything within a row...each entry does not necessarily take up an integer number of bytes!
    // However, we pad between rows if necessary, so that each row starts at a multiple of sizeof(unsigned long long) bytes (i.e. so that we can index the rows using normal offsets)
    unsigned long long* entries;
    int numRows;
    int numCols;
    int numBytesUsedToRepresentAllEntries;
    
private:
    
    
};

#endif	/* DISCRETEMATRIX_H */

