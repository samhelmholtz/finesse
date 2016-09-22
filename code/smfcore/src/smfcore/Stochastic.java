package smfcore;

import java.util.Random;

/**
 * Created by sam on 23.01.15.
 */
public class Stochastic implements IStochastic {

    private Random rand;

    public Stochastic(){
        // Can add a seed here for reproducibility if required
        this.rand = new Random();
    }

    @Override
    public int NextInt(int topValueExclusive) {
        return rand.nextInt(topValueExclusive);
    }

    @Override
    public double NextUniformlyDistributedDouble() {
        return rand.nextDouble();
    }
}
