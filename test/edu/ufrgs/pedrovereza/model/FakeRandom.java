package edu.ufrgs.pedrovereza.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FakeRandom  extends Random {

    private Map<Integer, Double> fakeValues = new HashMap<Integer, Double>();
    private Map<Integer, Integer> fakeInts = new HashMap<Integer, Integer>();
    private int callCounter;


    public void valueForCall(double value, int callNumber) {
        fakeValues.put(callNumber, value);
    }

    public void valueForNextIntWithMax(int value, int max) {
        fakeInts.put(max, value);
    }


    @Override
    public int nextInt(int n) {
        if (fakeInts.containsKey(n)) {
            return fakeInts.get(n);
        }

        return super.nextInt(n);
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
