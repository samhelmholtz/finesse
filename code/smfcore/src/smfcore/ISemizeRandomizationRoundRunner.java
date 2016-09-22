package smfcore;

/**
 * Created by sam on 30.01.15.
 */
public interface ISemizeRandomizationRoundRunner {
    SemizeResult RunSemizeForPRandomizationRoundsAndGetBestResult(SemiringObject[][] dataMatrix, ISemiringFeature[] semirings, int k, int p);
}
