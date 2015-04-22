package edu.ufrgs.pedrovereza.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import edu.ufrgs.pedrovereza.model.FakeRandom;
import edu.ufrgs.pedrovereza.model.Individual;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class IndividualTest {

    private byte[] chromosome;

    @Before
    public void setUp() throws Exception {
        chromosome = new byte[24];
    }

    @Test
    public void lowest_phenotype_is_minus_2() throws Exception {
        Individual individual = Individual.fromChromosome(chromosome);

        double xValue = individual.phenotypeForX();
        assertEquals(-2.0, xValue, 0.0);
    }

    @Test
    public void highest_phenotype_is_2() throws Exception {
        Arrays.fill(chromosome, (byte) 1);

        Individual individual = Individual.fromChromosome(chromosome);

        double xValue = individual.phenotypeForX();
        assertEquals(2.0, xValue, 0.0);
    }

    @Test
    public void x_and_y_are_one_chromosome() throws Exception {
        Individual individual = Individual.fromChromosome(chromosome);

        double xValue = individual.phenotypeForX();
        double yValue = individual.phenotypeForY();

        assertEquals(-2.0, xValue, 0.0);
        assertEquals(-2.0, yValue, 0.0);
    }

    @Test
    public void mutation_has_ten_percent_change_to_happen() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.09, 1);

        Individual individual = new Individual(chromosome, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertEquals(newIndividual.phenotypeForX(), individual.phenotypeForX(), 0.0);
    }

    @Test
    public void mutated_individual_should_be_different_from_original() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);

        Individual individual = new Individual(chromosome, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertNotEquals(newIndividual.phenotypeForX(), individual.phenotypeForX(), 0.0);
    }

    @Test
    public void mutation_affects_only_one_phenotype() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);
        fakeRandom.valueForCall(0.9, 2);

        Individual individual = new Individual(chromosome, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertEquals(newIndividual.phenotypeForX(), individual.phenotypeForX(), 0.0);
        assertNotEquals(newIndividual.phenotypeForY(), individual.phenotypeForY(), 0.0);
    }
}
