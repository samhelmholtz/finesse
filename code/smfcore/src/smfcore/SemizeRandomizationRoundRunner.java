package smfcore;

/**
 * Created by sam on 30.01.15.
 */
public class SemizeRandomizationRoundRunner implements ISemizeRandomizationRoundRunner {

    private final ISemize semize;

    public SemizeRandomizationRoundRunner(ISemize semize) {
        this.semize = semize;
    }

    @Override
    public SemizeResult RunSemizeForPRandomizationRoundsAndGetBestResult(SemiringObject[][] dataMatrix, ISemiringFeature[] semirings, int k, int p) {

        SemizeResult greedyResult = null;
        for (int i = 0; i < p; i++) {
            SemizeResult iterationResult = semize.Semize(dataMatrix, semirings, k);
            if(greedyResult == null || iterationResult.Error < greedyResult.Error){
                greedyResult = iterationResult;
            }
        }

        return greedyResult;
    }
}
