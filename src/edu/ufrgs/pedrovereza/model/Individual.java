package edu.ufrgs.pedrovereza.model;

import static java.lang.System.arraycopy;
import static java.util.Collections.emptyList;
import com.lagodiuk.ga.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Chromosome<Individual> {

    public static final int CHROMOSOME_SIZE = 12;

    private static final double MINIMUM_VALUE = -2.0;
    private static final double MAXIMUM_VALUE = 4.0;

    private static final double TEN_PERCENT = 0.1;

    private static final int CROSSOVER_RANGE = 3;

    private byte[] chromosomeForY;
    private byte[] chromosomeForX;

    private final Random random;

    public static Individual fromChromosomes(byte[] chromosomeForX, byte[] chromosomeForY) {
        return new Individual(chromosomeForX, chromosomeForY, new Random());
    }

    public Individual(byte[] chromosomeForX, byte[] chromosomeForY, Random random) {
        this.chromosomeForY = chromosomeForY.clone();
        this.chromosomeForX = chromosomeForX.clone();
        this.random = random;
    }

    public double phenotypeForX() {
        double xValue = binaryToDecimal(chromosomeForX);
        return decimalToDomainRange(xValue);
    }

    public double phenotypeForY() {
        double yValue = binaryToDecimal(chromosomeForY);
        return decimalToDomainRange(yValue);
    }

    @Override
    public List<Individual> crossover(Individual individual) {
        if (random.nextDouble() > TEN_PERCENT) {
            return emptyList();
        }

        int positionToCrossover = random.nextInt(CHROMOSOME_SIZE);

        return crossoverAtPosition(individual, positionToCrossover);
    }

    @Override
    public Individual mutate() {
        byte[] newChromosomeForX = chromosomeForX.clone();
        byte[] newChromosomeForY = chromosomeForY.clone();

            mutateAtRandomPosition(newChromosomeForX);

            mutateAtRandomPosition(newChromosomeForY);

        return Individual.fromChromosomes(newChromosomeForX, newChromosomeForY);
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
        int max_crossover = CHROMOSOME_SIZE - positionToCrossover;

        byte[] firstChromosomeForX = chromosomeForX.clone();
        byte[] secondChromosomeForX = chromosomeForX.clone();

        arraycopy(individual.chromosomeForX, 0, firstChromosomeForX, 0, CROSSOVER_RANGE);

        arraycopy(individual.chromosomeForX, positionToCrossover, secondChromosomeForX, positionToCrossover,
                max_crossover >  CROSSOVER_RANGE ? CROSSOVER_RANGE : max_crossover);

        byte[] firstChromosomeForY = chromosomeForY.clone();
        byte[] secondChromosomeForY = chromosomeForY.clone();


        arraycopy(individual.chromosomeForY, 0, firstChromosomeForY, 0, CROSSOVER_RANGE);

        arraycopy(individual.chromosomeForY, positionToCrossover, secondChromosomeForY, positionToCrossover,
                max_crossover >  CROSSOVER_RANGE ? CROSSOVER_RANGE : max_crossover);

        Individual firstIndividual = Individual.fromChromosomes(firstChromosomeForX, firstChromosomeForY);
        Individual secondIndividual = Individual.fromChromosomes(secondChromosomeForX, secondChromosomeForY);

        offspring.add(firstIndividual);
        offspring.add(secondIndividual);

        return offspring;
    }

    private double binaryToDecimal(byte[] chromosome) {
        double value = 0;

        for (int i = 0; i < chromosome.length; ++i) {
            value += chromosome[i] * (Math.pow(2.0, i));
        }

        return value;
    }

    private double decimalToDomainRange(double decimal) {
        return MINIMUM_VALUE + (decimal * (MAXIMUM_VALUE / (4096 - 1)));
    }

    private void mutateAtRandomPosition(byte[] newChromosome) {
        int positionToMutate = random.nextInt(CHROMOSOME_SIZE);

        newChromosome[positionToMutate] = (byte) ((newChromosome[positionToMutate] + 1) % 2);
    }
}
