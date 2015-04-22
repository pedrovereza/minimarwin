package edu.ufrgs.pedrovereza.model;

import com.lagodiuk.ga.Chromosome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Individual implements Chromosome<Individual> {

    private static final int PHENOTYPE_SIZE = 12;

    private static final int X_PHENOTYPE_START = 0;
    private static final int X_PHENOTYPE_END = 11;

    private static final int Y_PHENOTYPE_START = 12;
    private static final int Y_PHENOTYPE_END = 23;
    public static final double MINIMUM_VALUE = -2.0;
    public static final double MAXIMUM_VALUE = 4.0;

    private byte[] chromosome;
    private final Random random;

    public static Individual fromChromosome(byte[] chromosome) {
        return new Individual(chromosome, new Random());
    }

    public Individual(byte[] chromosome, Random random) {
        this.chromosome = chromosome;
        this.random = random;
    }

    public double phenotypeForX() {
        double xValue = binaryToDecimal(X_PHENOTYPE_START, X_PHENOTYPE_END);

        return decimalToDomainRange(xValue);
    }

    public double phenotypeForY() {
        double yValue = binaryToDecimal(Y_PHENOTYPE_START, Y_PHENOTYPE_END);

        return decimalToDomainRange(yValue);
    }

    private double binaryToDecimal(int start, int end) {
        double yValue = 0;

        for (int i = start; i <= end; ++i) {
            yValue += chromosome[i] * (Math.pow(2.0, (i % PHENOTYPE_SIZE)));
        }

        return yValue;
    }

    private double decimalToDomainRange(double decimal) {
        return MINIMUM_VALUE + (decimal * (MAXIMUM_VALUE /(4096 -1)));
    }

    @Override
    public List<Individual> crossover(Individual individual) {
       return null;
    }

    @Override
    public Individual mutate() {
        byte[] newChromosome = Arrays.copyOf(chromosome, 24);

        if (random.nextDouble() > 0.1) {
            mutateAtRandomPosition(newChromosome);
        }

        return new Individual(newChromosome, random);
    }

    private void mutateAtRandomPosition(byte[] newChromosome) {
        int positionToMutate = (int) (random.nextDouble() * 23);

        newChromosome[positionToMutate] = (byte) ((newChromosome[positionToMutate] + 1) % 2);
    }
}