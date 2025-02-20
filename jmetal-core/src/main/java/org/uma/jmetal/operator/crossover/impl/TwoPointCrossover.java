package org.uma.jmetal.operator.crossover.impl;

import java.util.List;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

/**
 * Created by FlapKap on 27-05-2017.
 */
@SuppressWarnings("serial")
public class TwoPointCrossover<T extends Solution<S>, S extends Number> implements CrossoverOperator<T> {
  NPointCrossover<T,S> operator;

  public TwoPointCrossover(double probability) {
    this.operator = new NPointCrossover<T,S>(probability, 2);
  }

  @Override
  public List<T> execute(List<T> solutions) {
    return operator.execute(solutions);
  }

  @Override
  public double crossoverProbability() {
    return operator.crossoverProbability() ;
  }

  @Override
  public int numberOfRequiredParents() {
    return operator.numberOfRequiredParents();
  }

  @Override
  public int numberOfGeneratedChildren() {
    return 2;
  }
}
