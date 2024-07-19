package org.uma.jmetal.util.neighborhood.impl;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.doubleproblem.impl.FakeDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

class KNearestNeighborhoodTest {

  /**
   * Case A: The solution list has two solutions and the neighbor size is 1
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseA() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(1) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 0) ;

    Assertions.assertEquals(1, neighbors.size());
    Assertions.assertSame(solution2, neighbors.get(0));
  }

  /**
   * Case B: The solution list has three solutions, the index of the solution is 0, and the neighbor size is 2
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseB() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 2.0 ;
    solution3.objectives()[1] = 2.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(2) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 0) ;

    Assertions.assertEquals(2, neighbors.size());
    Assertions.assertSame(solution2, neighbors.get(0));
    Assertions.assertSame(solution3, neighbors.get(1));
  }

  /**
   * Case C: The solution list has three solutions, the index of the solution is 1, and the neighbor size is 2
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseC() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 2.0 ;
    solution3.objectives()[1] = 2.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(2) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 1) ;

    Assertions.assertEquals(2, neighbors.size());
    Assertions.assertSame(solution1, neighbors.get(0));
    Assertions.assertSame(solution3, neighbors.get(1));
  }

  /**
   * Case D: The solution list has three solutions, the index of the solution is 2, and the neighbor size is 2
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseD() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 2.0 ;
    solution3.objectives()[1] = 2.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(2) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 2) ;

    Assertions.assertEquals(2, neighbors.size());
    Assertions.assertSame(solution2, neighbors.get(0));
    Assertions.assertSame(solution1, neighbors.get(1));
  }

  /**
   * Case E: The solution list has five solutions, the index of the solution is 0, and the neighbor size is 3
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseE() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 2.0 ;
    solution3.objectives()[1] = 2.0 ;

    DoubleSolution solution4 = problem.createSolution() ;
    solution4.objectives()[0] = 3.0 ;
    solution4.objectives()[1] = 3.0 ;

    DoubleSolution solution5 = problem.createSolution() ;
    solution5.objectives()[0] = 4.0 ;
    solution5.objectives()[1] = 4.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3, solution4, solution5) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(3) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 0) ;

    Assertions.assertEquals(3, neighbors.size());
    Assertions.assertSame(solution2, neighbors.get(0));
    Assertions.assertSame(solution3, neighbors.get(1));
    Assertions.assertSame(solution4, neighbors.get(2));
  }

  /**
   * Case F: The solution list has five solutions, the index of the solution is 2, and the neighbor size is 3
   */
  @Test
  void shouldGetNeighborsWorkProperlyCaseF() {
    DoubleProblem problem = new FakeDoubleProblem(3, 2, 0) ;

    DoubleSolution solution1 = problem.createSolution() ;
    solution1.objectives()[0] = 0.0 ;
    solution1.objectives()[1] = 0.0 ;

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.objectives()[0] = 1.0 ;
    solution2.objectives()[1] = 1.0 ;

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.objectives()[0] = 2.0 ;
    solution3.objectives()[1] = 2.0 ;

    DoubleSolution solution4 = problem.createSolution() ;
    solution4.objectives()[0] = 3.0 ;
    solution4.objectives()[1] = 3.0 ;

    DoubleSolution solution5 = problem.createSolution() ;
    solution5.objectives()[0] = 4.0 ;
    solution5.objectives()[1] = 4.0 ;

    List<DoubleSolution> solutionList = Arrays.asList(solution1, solution2, solution3, solution4, solution5) ;

    KNearestNeighborhood<DoubleSolution> neighborhood = new KNearestNeighborhood<>(3) ;
    List<DoubleSolution> neighbors = neighborhood.getNeighbors(solutionList, 2) ;

    Assertions.assertEquals(3, neighbors.size());
    Assertions.assertSame(solution2, neighbors.get(0));
    Assertions.assertSame(solution4, neighbors.get(1));
    Assertions.assertSame(solution1, neighbors.get(2));
  }
}