package edu.ufrgs.pedrovereza.app;

import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import edu.ufrgs.pedrovereza.UI.GraphicService;
import edu.ufrgs.pedrovereza.factory.IndividualFactory;
import edu.ufrgs.pedrovereza.model.Function;
import edu.ufrgs.pedrovereza.model.Individual;

import java.util.Random;

public class Minimarwin {

    private GraphicService graphicService;

    public void run() throws Exception {
        IndividualFactory factory = new IndividualFactory(new Random());

        Population<Individual> population = factory.createPopulation(80);
        Function function = new Function();

        graphicService = new GraphicService(function);

        GeneticAlgorithm<Individual, Double> ga = new GeneticAlgorithm<Individual, Double>(population, function);
        ga.setParentChromosomesSurviveCount(2);
        addListener(ga);

        ga.evolve(3000);

        Individual finalSolution = ga.getBest();

        graphicService.addFinalSolution(finalSolution.phenotypeForX(), finalSolution.phenotypeForY(),
                ga.fitness(finalSolution));

        graphicService.plot();
    }

    private void addListener(GeneticAlgorithm<Individual, Double> ga) {
        System.out.println(String.format("%s\t%s\t%s", "iter", "fit", "chromosome"));

        ga.addIterationListener(new IterartionListener<Individual, Double>() {

            @Override
            public void update(GeneticAlgorithm<Individual, Double> ga) {

                Individual best = ga.getBest();
                double bestFit = ga.fitness(best);
                int iteration = ga.getIteration();

                Individual worst = ga.getWorst();
                graphicService.addUniqueIndividual(worst.phenotypeForX(), worst.phenotypeForX(),
                        ga.fitness(worst));

                System.out.println(String.format("%s\t%s\t%s", iteration, bestFit, best));
            }
        });
    }
}
