package edu.ufrgs.pedrovereza.model;

import static java.lang.System.arraycopy;
import static java.util.Collections.emptyList;
import com.lagodiuk.ga.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Chromosome<Individual> {

    public static final int CHROMOSOME_SIZE = 24;

    private static final int PHENOTYPE_SIZE = 12;

    private static final int X_PHENOTYPE_START = 0;
    private static final int X_PHENOTYPE_END = 11;

    private static final int Y_PHENOTYPE_START = 12;
    private static final int Y_PHENOTYPE_END = 23;

    private static final double MINIMUM_VALUE = -2.0;
    private static final double MAXIMUM_VALUE = 4.0;

    private static final double TWENTY_FIVE_PERCENT = 0.25;
    private static final double TEN_PERCENT = 0.1;

    private byte[] chromosome;
    private final Random random;

    public static Individual fromChromosome(byte[] chromosome) {
        return new Individual(chromosome, new Random());
    }

    public Individual(byte[] chromosome, Random random) {
        this.chromosome = chromosome.clone();
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

    @Override
    public List<Individual> crossover(Individual individual) {
        if (random.nextDouble() < TWENTY_FIVE_PERCENT) {
            return emptyList();
        }

        int positionToCrossover = (int) (random.nextDouble() * (CHROMOSOME_SIZE - 1));

        return crossoverAtPosition(individual, positionToCrossover);
    }

    @Override
    public Individual mutate() {
        byte[] newChromosome = chromosome.clone();

        if (random.nextDouble() > TEN_PERCENT) {
            mutateAtRandomPosition(newChromosome);
        }

        return new Individual(newChromosome, random);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Individual.class) {
            return false;
        }

        Individual other = (Individual) o;

        return phenotypeForX() == other.phenotypeForX() &&
                phenotypeForY() == other.phenotypeForY();
    }

    @Override
    public String toString() {
        return String.format("Phenotypes: x: %f, y: %f", phenotypeForX(), phenotypeForY());
    }

    private List<Individual> crossoverAtPosition(Individual individual, int positionToCrossover) {
        List<Individual> offspring = new ArrayList<Individual>(2);
        byte[] firstChromosome = chromosome.clone();
        byte[] secondChromosome = chromosome.clone();

        arraycopy(individual.chromosome, 0, firstChromosome, 0, positionToCrossover);

        arraycopy(individual.chromosome, positionToCrossover, secondChromosome, positionToCrossover,
                CHROMOSOME_SIZE - positionToCrossover);

        Individual firstIndividual = new Individual(firstChromosome, random);
        Individual secondIndividual = new Individual(secondChromosome, random);

        offspring.add(firstIndividual);
        offspring.add(secondIndividual);

        return offspring;
    }

    private double binaryToDecimal(int start, int end) {
        double yValue = 0;

        for (int i = start; i <= end; ++i) {
            yValue += chromosome[i] * (Math.pow(2.0, (i % PHENOTYPE_SIZE)));
        }

        return yValue;
    }

    private double decimalToDomainRange(double decimal) {
        return MINIMUM_VALUE + (decimal * (MAXIMUM_VALUE / (4096 - 1)));
    }

    private void mutateAtRandomPosition(byte[] newChromosome) {
        int positionToMutate = (int) (random.nextDouble() * (CHROMOSOME_SIZE - 1));

        newChromosome[positionToMutate] = (byte) ((newChromosome[positionToMutate] + 1) % 2);
    }
}
