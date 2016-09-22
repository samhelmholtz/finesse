package smfcore;

/**
 * Created by sam on 23.01.15.
 */
public interface IStochastic {
    int NextInt(int topValueExclusive);
    double NextUniformlyDistributedDouble();
}
