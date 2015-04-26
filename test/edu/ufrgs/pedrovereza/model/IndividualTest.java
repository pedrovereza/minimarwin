package edu.ufrgs.pedrovereza.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IndividualTest {

    private byte[] chromosomeForX;
    private byte[] chromosomeForY;

    @Before
    public void setUp() throws Exception {
        chromosomeForX = new byte[12];
        chromosomeForY = new byte[12];
    }

    @Test
    public void lowest_phenotype_is_minus_2() throws Exception {
        Individual individual = Individual.fromChromosomes(chromosomeForX, chromosomeForY);

        double xValue = individual.phenotypeForX();
        assertEquals(-2.0, xValue, 0.0);
    }

    @Test
    public void highest_phenotype_is_2() throws Exception {
        Arrays.fill(chromosomeForX, (byte) 1);

        Individual individual = Individual.fromChromosomes(chromosomeForX, chromosomeForY);

        double xValue = individual.phenotypeForX();
        assertEquals(2.0, xValue, 0.0);
    }

    @Test
    public void mutation_always_happens() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);

        Individual individual = new Individual(chromosomeForX, chromosomeForY, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertNotEquals(newIndividual, individual);
    }

    @Test
    public void mutated_individual_should_be_different_from_original() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);
        fakeRandom.valueForCall(0.09, 1);

        Individual individual = new Individual(chromosomeForX, chromosomeForY, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertNotEquals(newIndividual, individual);
    }

    @Test
    public void mutation_affects_both_phenotypes() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);
        fakeRandom.valueForCall(0.20, 2);
        fakeRandom.valueForCall(0.9, 3);
        fakeRandom.valueForCall(0.1, 4);

        Individual individual = new Individual(chromosomeForX, chromosomeForY, fakeRandom);

        Individual newIndividual = individual.mutate();

        assertNotEquals(newIndividual.phenotypeForX(), individual.phenotypeForX(), 0.0);
        assertNotEquals(newIndividual.phenotypeForY(), individual.phenotypeForY(), 0.0);
    }

    @Test
    public void cross_over_has_25_percent_chance_to_happen() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.20, 1);

        Individual individual = new Individual(chromosomeForX, chromosomeForY, fakeRandom);

        Arrays.fill(chromosomeForX, (byte) 1);

        Individual other = new Individual(chromosomeForX, chromosomeForY,  fakeRandom);

        List<Individual> offspring = individual.crossover(other);

        assertTrue(offspring.isEmpty());
    }

    @Test
    public void cross_over_generates_two_individuals() throws Exception {
        FakeRandom fakeRandom = new FakeRandom();
        fakeRandom.valueForCall(0.09, 1);
        fakeRandom.valueForNextIntWithMax(4, 12);

        Individual individual = new Individual(chromosomeForX, chromosomeForY,  fakeRandom);

        Arrays.fill(chromosomeForX, (byte) 1);
        Arrays.fill(chromosomeForY, (byte) 1);

        Individual other = new Individual(chromosomeForX, chromosomeForY, fakeRandom);

        List<Individual> offspring = individual.crossover(other);

        Individual expectedChild = individualFromChromosomes("111000000000", "111000000000");
        Individual otherExpectedChild = individualFromChromosomes("000011100000", "000011100000");

        assertFalse(offspring.isEmpty());
        assertEquals(expectedChild, offspring.get(0));
        assertEquals(otherExpectedChild, offspring.get(1));
    }


    private Individual individualFromChromosomes(String chromosomeX, String chromosomeY) {
        byte[] actualChromosomeX = new byte[12];
        byte[] actualChromosomeY = new byte[12];

        for (int i = 0; i < chromosomeX.length(); i++) {
            actualChromosomeX[i] = (byte) (chromosomeX.charAt(i) == '1' ? 1 : 0);
        }

        for (int i = 0; i < chromosomeY.length(); i++) {
            actualChromosomeY[i] = (byte) (chromosomeY.charAt(i) == '1' ? 1 : 0);
        }


        return Individual.fromChromosomes(actualChromosomeX, actualChromosomeY);
    }
}
