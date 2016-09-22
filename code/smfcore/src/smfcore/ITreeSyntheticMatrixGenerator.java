package smfcore;

/**
 * Created by sam on 23.01.15.
 */
public interface ITreeSyntheticMatrixGenerator {
    Tree GenerateMasterTree(int depth, int nary);
    SemiringObject[][] Generate(Tree masterTree, int n, int m, int k, int lambda, int averageNumberOfNodesSampledForTreeEntries, int noise);
}
