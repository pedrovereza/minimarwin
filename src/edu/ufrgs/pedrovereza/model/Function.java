package edu.ufrgs.pedrovereza.model;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import com.lagodiuk.ga.Fitness;

public class Function implements Fitness<Individual, Double> {
    @Override
    public Double calculate(Individual individual) {
        double x = individual.phenotypeForX();
        double y = individual.phenotypeForY();

        return functionW16(x, y);
    }

    private double functionW16(double x, double y) {
        return functionW14(x, y) + functionW4(x, y);
    }

    private double functionW14(double x, double y) {
        return functionZ(x, y) * (exp(sin(functionR1(x, y))));
    }

    private double functionR1(double x, double y) {
        return pow((y - (x * x)), 2.0) + pow((1 - x), 2.0);
    }

    private double functionW4(double x, double y) {
        return sqrt((pow(functionR(x, y), 2.0)) + pow(functionZ(x, y), 2.0));
    }

    private double functionZ(double x, double y) {
        return (-x * (sin(sqrt(abs(x)))))  - (y * (sin(sqrt(abs(y)))));
    }

    private double functionR(double x, double y) {
        return 100 * functionR1(x, y);
    }

}
