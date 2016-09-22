/* 
 * File:   Stochastic.cpp
 * Author: sam
 * 
 * Created on April 28, 2015, 1:12 PM
 */

#include "Stochastic.h"
#include <time.h>
#include <stdlib.h>

Stochastic::Stochastic(int upperLimitExclusive) //: distribution(0, upperLimitExclusive - 1) 
{
    this->upperLimitExclusive = upperLimitExclusive;
    srand(time(0));
    //    std::random_device rd;
    //    generator.seed(rd());
}

Stochastic::Stochastic(const Stochastic& orig) {
}

Stochastic::~Stochastic() {
}

int Stochastic::NextInt() {

    return rand() % this->upperLimitExclusive;
//    int result = distribution(generator);
//    return result;
}