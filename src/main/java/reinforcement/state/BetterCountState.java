package reinforcement.state;

public class BetterCountState implements State{

    @Override
    public int calculate(int numberOfBetter) {
        return numberOfBetter;
    }
}
