package optimal.probabilitySampling;

class IterativeProbabilitySearcher extends ProbabilitySearcher {
    protected IterativeProbabilitySearcher(double myLeftProb, double myRightProb, double myPrecision) {
        super(myLeftProb, myRightProb, myPrecision);
    }

    @Override
    public double getNextProb(double feedback) {
        myLastReturnedPrecision = myLastReturnedPrecision + myPrecision;
        return myLastReturnedPrecision;
    }

    @Override
    public double getInitialProbability() {
        return myLeftProb;
    }

    @Override
    public boolean isFinished() {
        return Math.abs(myLastReturnedPrecision - myRightProb) < myPrecision;
    }
}
