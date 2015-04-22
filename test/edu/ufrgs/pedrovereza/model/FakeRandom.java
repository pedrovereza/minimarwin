package edu.ufrgs.pedrovereza.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FakeRandom  extends Random {

    private Map<Integer, Double> fakeValues = new HashMap<Integer, Double>();
    private int callCounter;


    public void valueForCall(double value, int callNumber) {
        fakeValues.put(callNumber, value);
    }

    @Override
    public double nextDouble() {
        callCounter++;

        if (fakeValues.containsKey(callCounter)) {
            return fakeValues.get(callCounter);
        }

        return super.nextDouble();
    }
}
