package edu.ufrgs.pedrovereza.factory;

import static edu.ufrgs.pedrovereza.model.Individual.CHROMOSOME_SIZE;
import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import edu.ufrgs.pedrovereza.model.Function;
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

    public static void main(String[] args) {
        IndividualFactory factory = new IndividualFactory(new Random());

        Population<Individual> population = factory.createPopulation(80);
        Function function = new Function();

        GeneticAlgorithm<Individual, Double> ga = new GeneticAlgorithm<Individual, Double>(population, function);
        ga.setParentChromosomesSurviveCount(2);

        addListener(ga);

        ga.evolve(3000);
    }

    private static void addListener(GeneticAlgorithm<Individual, Double> ga) {
        System.out.println(String.format("%s\t%s\t%s", "iter", "fit", "chromosome"));

        ga.addIterationListener(new IterartionListener<Individual, Double>() {

            @Override
            public void update(GeneticAlgorithm<Individual, Double> ga) {

                Individual best = ga.getBest();
                double bestFit = ga.fitness(best);
                int iteration = ga.getIteration();

                System.out.println(String.format("%s\t%s\t%s", iteration, bestFit, best));
            }
        });
    }
}
