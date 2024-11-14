package org.uma.jmetal.problem.multiobjective.zcat;

import java.util.Collections;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.zcat.ffunction.F8;
import org.uma.jmetal.problem.multiobjective.zcat.gfunction.G0;
import org.uma.jmetal.problem.multiobjective.zcat.gfunction.G2;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

/**
 * Problem ZCAT8, defined in: "Challenging test problems for multi-and many-objective optimization",
 * DOI: https://doi.org/10.1016/j.swevo.2023.101350
 */
public class ZCAT8 extends ZCAT1 {

  public ZCAT8() {
    this(
        DefaultZCATSettings.numberOfObjectives,
        DefaultZCATSettings.numberOfVariables,
        DefaultZCATSettings.complicatedParetoSet,
        DefaultZCATSettings.level,
        DefaultZCATSettings.bias,
        DefaultZCATSettings.imbalance);
  }

  public ZCAT8(
      int numberOfObjectives,
      int numberOfVariables,
      boolean complicatedParetoSet,
      int level,
      boolean bias,
      boolean imbalance) {
    super(numberOfObjectives, numberOfVariables, complicatedParetoSet, level, bias, imbalance);
    name("ZCAT8");

    paretoSetDimension = numberOfObjectives - 1;

    fFunction = new F8(numberOfObjectives);
    gFunction =
        complicatedParetoSet
            ? new G2(numberOfVariables, paretoSetDimension)
            : new G0(numberOfVariables, paretoSetDimension);
  }

  public static void main(String[] args) {
    DoubleProblem problem = new ZCAT8();

    DoubleSolution solution = problem.createSolution();
    Collections.fill(solution.variables(), 0.45);

    problem.evaluate(solution);
    System.out.println(solution);
  }
}
