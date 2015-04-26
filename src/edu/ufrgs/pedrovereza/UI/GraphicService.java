package edu.ufrgs.pedrovereza.UI;

import edu.ufrgs.pedrovereza.model.Function;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbowNoBorder;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphicService extends AbstractAnalysis {

    public static final float RADIUS = 0.05f;
    public static final int SLICING = 8;
    private final Function function;
    private final List<Coord3d> solutionsCoordinates;
    private final List<Coord3d> populationCoordinates;
    private Coord3d finalSolution;

    public GraphicService(Function function) {
        this.function = function;
        solutionsCoordinates = new LinkedList<Coord3d>();
        populationCoordinates = new LinkedList<Coord3d>();
    }

    public void addSolution(double x, double y, double z) {
        Coord3d coordinate = new Coord3d(x, y, z);

        if (solutionsCoordinates.contains(coordinate)) {
            return;
        }

        solutionsCoordinates.add(coordinate);
    }

    public void addUniqueIndividual(double x, double y, double z) {
        Coord3d coordinate = new Coord3d(x, y, z);

        if (populationCoordinates.contains(coordinate)) {
            return;
        }

        populationCoordinates.add(coordinate);
    }

    public void plot() throws Exception {
        AnalysisLauncher.open(this);
        plotWithPopulation();
        plotWithSolutions();
        plotFinalSolution();
    }

    private void plotFinalSolution() {
        chart.getScene().add(toSphere(finalSolution, Color.GREEN));
    }

    public void plotWithSolutions() {
        List<AbstractDrawable> solutions = coordinatesToSpheres(solutionsCoordinates, Color.BLACK);
        chart.getScene().add(solutions);
    }

    public void plotWithPopulation() {
        List<AbstractDrawable> solutions = coordinatesToSpheres(populationCoordinates, Color.RED);
        chart.getScene().add(solutions);
    }

    private List<AbstractDrawable> coordinatesToSpheres(List<Coord3d> coordinates, Color color) {
        List<AbstractDrawable> spheres = new ArrayList<AbstractDrawable>(coordinates.size());

        for (Coord3d coordinate : coordinates) {
            spheres.add(toSphere(coordinate, color));
        }

        return spheres;
    }

    private static Sphere toSphere(Coord3d coordinate, Color color) {
        Sphere sphere = new Sphere(coordinate, RADIUS, SLICING, color);
        sphere.setWireframeColor(color);
        sphere.setWireframeDisplayed(true);
        sphere.setWireframeWidth(1);
        sphere.setFaceDisplayed(true);
        return sphere;
    }

    @Override
    public void init() throws Exception {
        plotFunctionInRange(-2, 2);
    }

    private void plotFunctionInRange(int min, int max) {
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return function.functionW16(x, y);
            }
        };

        Range range = new Range(min, max);
        int steps = 80;

        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);

        surface.setColorMapper(new ColorMapper(new ColorMapRainbowNoBorder(), surface.getBounds().getZmin(),
                surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));

        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        chart.getScene().getGraph().add(surface);
    }

    public void addFinalSolution(double x, double y, double z) {
        this.finalSolution = new Coord3d(x, y, z);

    }
}
