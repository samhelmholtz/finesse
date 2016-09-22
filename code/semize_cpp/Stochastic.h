/* 
 * File:   Stochastic.h
 * Author: sam
 *
 * Created on April 28, 2015, 1:12 PM
 */

#ifndef STOCHASTIC_H
#define	STOCHASTIC_H

#include <random>

class Stochastic {
public:
    Stochastic(int upperLimitExclusive);
    Stochastic(const Stochastic& orig);
    virtual ~Stochastic();
    virtual int NextInt();
private:
    int upperLimitExclusive;
//    std::minstd_rand generator;
//    std::uniform_int_distribution<int> distribution;    
};

#endif	/* STOCHASTIC_H */

