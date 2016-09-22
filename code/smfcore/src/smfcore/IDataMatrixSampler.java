/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

/**
 *
 * @author sam
 */
public interface IDataMatrixSampler {
    SemiringObject[][] SampleK(SemiringObject[][] population, int k, ISemiringFeature[] semirings);    
}
