package org.uma.jmetal.util.point.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.doubleproblem.impl.FakeDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.point.Point;

/**
 * Created by ajnebro on 12/2/16.
 */
class NadirPointTest {
  private static final double EPSILON = 0.00000000001 ;
  private static final double DEFAULT_INITIAL_VALUE = Double.NEGATIVE_INFINITY ;
  private NadirPoint referencePoint ;

  @Test
  void shouldConstructorCreateANadirPointWithAllObjectiveValuesCorrectlyInitialized() {
    int numberOfObjectives = 4 ;

    referencePoint = new NadirPoint(numberOfObjectives) ;

    Assertions.assertEquals(numberOfObjectives, referencePoint.dimension());

    for (int i = 0 ; i < numberOfObjectives; i++) {
      Assertions.assertEquals(DEFAULT_INITIAL_VALUE, referencePoint.value(i), EPSILON);
    }
  }

  @Test
  void shouldUpdateWithOneSolutionMakeTheNadirPointHaveTheSolutionValues() {
    int numberOfObjectives = 2 ;

    referencePoint = new NadirPoint(numberOfObjectives) ;

    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution = problem.createSolution() ;
    solution.objectives()[0] = 1 ;
    solution.objectives()[1] = 2 ;

    referencePoint.update(solution.objectives());
    Assertions.assertEquals(1, referencePoint.value(0), EPSILON);
    Assertions.assertEquals(2, referencePoint.value(1), EPSILON);
  }

  @Test
  void shouldUpdateWithTwoSolutionsLeadToTheCorrectNadirPoint() {
    int numberOfObjectives = 2 ;

    referencePoint = new NadirPoint(numberOfObjectives) ;

    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0 ;
    solution1.objectives()[1] = 1 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1 ;
    solution2.objectives()[1] = 0 ;

    referencePoint.update(solution1.objectives());
    referencePoint.update(solution2.objectives());

    Assertions.assertEquals(1.0, referencePoint.value(0), EPSILON);
    Assertions.assertEquals(1.0, referencePoint.value(1), EPSILON);
  }

  @Test
  void shouldUpdateWithThreeSolutionsLeadToTheCorrectNadirPoint() {
    int numberOfObjectives = 3 ;

    referencePoint = new NadirPoint(numberOfObjectives) ;

    DoubleProblem problem = new FakeDoubleProblem(3, 3, 0) ;
    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 3.0 ;
    solution1.objectives()[1] = 1.0 ;
    solution1.objectives()[2] = 2.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 0.2 ;
    solution2.objectives()[1] = 4.0 ;
    solution2.objectives()[2] = 5.5 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 5.0 ;
    solution3.objectives()[1] = 6.0 ;
    solution3.objectives()[2] = 1.5 ;

    referencePoint.update(solution1.objectives());
    referencePoint.update(solution2.objectives());
    referencePoint.update(solution3.objectives());

    Assertions.assertEquals(5.0, referencePoint.value(0), EPSILON);
    Assertions.assertEquals(6.0, referencePoint.value(1), EPSILON);
    Assertions.assertEquals(5.5, referencePoint.value(2), EPSILON);
  }

  @Test
  void shouldUpdateAListOfSolutionsLeadToTheCorrectNadirPoint() {
    int numberOfObjectives = 3 ;

    referencePoint = new NadirPoint(numberOfObjectives) ;

    DoubleProblem problem = new FakeDoubleProblem(3, 3, 0) ;
    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 3.0 ;
    solution1.objectives()[1] = 1.0 ;
    solution1.objectives()[2] = 2.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 0.2 ;
    solution2.objectives()[1] = 4.0 ;
    solution2.objectives()[2] = 5.5 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 5.0 ;
    solution3.objectives()[1] = 6.0 ;
    solution3.objectives()[2] = 1.5 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3) ;

    referencePoint.update(solutionList);

    Assertions.assertEquals(5.0, referencePoint.value(0), EPSILON);
    Assertions.assertEquals(6.0, referencePoint.value(1), EPSILON);
    Assertions.assertEquals(5.5, referencePoint.value(2), EPSILON);
  }

  @Test
  void shouldSetAssignTheRightValues() {
    Point point = new ArrayPoint(new double[]{2, 3, 3}) ;

    point.set(new double[]{5, 6, 7}) ;
    Assertions.assertEquals(5, point.value(0), EPSILON);
    Assertions.assertEquals(6, point.value(1), EPSILON);
    Assertions.assertEquals(7, point.value(2), EPSILON);
  }
}