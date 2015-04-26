package edu.ufrgs.pedrovereza.factory;

import static edu.ufrgs.pedrovereza.model.Individual.CHROMOSOME_SIZE;
import com.lagodiuk.ga.Population;
import edu.ufrgs.pedrovereza.model.Individual;

import java.util.Random;

public class IndividualFactory {

    private final Random random;

    public IndividualFactory(Random random) {
        this.random = random;
    }

    public Population<Individual> createPopulation(int populationSize) {
        Population<Individual> population = new Population<Individual>();

        for (int i = 0; i < populationSize; i++) {
            population.addChromosome(Individual.fromChromosomes(randomChromosome(), randomChromosome()));
        }

        return population;
    }

    private byte[] randomChromosome() {
        byte[] chromosome = new byte[CHROMOSOME_SIZE];

        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = (byte) (random.nextDouble() > 0.5 ? 1 : 0);
        }

        return chromosome;
    }


}
