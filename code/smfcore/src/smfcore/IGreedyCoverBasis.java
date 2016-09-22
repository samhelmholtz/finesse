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
public interface IGreedyCoverBasis {
    SemiringObject[] GreedyCoverBasisWithLooseConjunction(SemiringObject[] dataColumnVector, boolean[][] usageMatrix, ISemiringFeature semiringFeature);
    SemiringObject[] GreedyCoverBasisWithTightConjunction(SemiringObject[] dataColumnVector, SemiringObject[][] usageMatrix, ISemiringFeature semiringFeature);        
}