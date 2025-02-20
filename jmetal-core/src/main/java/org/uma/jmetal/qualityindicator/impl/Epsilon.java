package org.uma.jmetal.qualityindicator.impl;

import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.util.errorchecking.Check;
import org.uma.jmetal.util.errorchecking.JMetalException;

/**
 * This class implements the unary epsilon additive indicator as proposed in E.
 * Zitzler, E. Thiele, L. Laummanns, M., Fonseca, C., and Grunert da Fonseca. V
 * (2003): Performance Assessment of Multiobjective Optimizers: An Analysis and
 * Review. The code is the a Java version of the original metric implementation
 * by Eckart Zitzler. It can be used also as a command line program just by
 * typing $java org.uma.jmetal.qualityindicator.impl.Epsilon <solutionFrontFile>
 * <trueFrontFile> <getNumberOfObjectives>
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class Epsilon extends QualityIndicator {

  /**
   * Default constructor
   */
  public Epsilon() {
  }

  /**
   * Constructor
   *
   * @param referenceFront
   */
  public Epsilon(double[][] referenceFront) {
    super(referenceFront) ;
  }

  @Override
  public boolean isTheLowerTheIndicatorValueTheBetter() {
    return true ;
  }

  @Override
  public QualityIndicator newInstance() {
    return new Epsilon();
  }

  /**
   * Evaluate() method
   *
   * @param front
   * @return
   */
  @Override public double compute(double[][] front) {
    Check.notNull(front);

    return epsilon(front, referenceFront);
  }

  /**
   * Returns the value of the epsilon indicator.
   *
   * @param front Solution front
   * @param referenceFront Reference Pareto front
   * @return the value of the epsilon indicator
   * @throws JMetalException
   */
  private double epsilon(double[][] front, double[][] referenceFront) throws JMetalException {
    int numberOfObjectives = front[0].length ;
    double eps = Double.MIN_VALUE;

    double epsJ = 0.0;
    double epsK = 0.0;
    for (int i = 0; i < referenceFront.length; i++) {
      for (int j = 0; j < front.length; j++) {
        for (int k = 0; k < numberOfObjectives; k++) {
          double epsTemp = front[j][k] - referenceFront[i][k];
          if (k == 0) {
            epsK = epsTemp;
          } else if (epsK < epsTemp) {
            epsK = epsTemp;
          }
        }
        if (j == 0) {
          epsJ = epsK;
        } else if (epsJ > epsK) {
          epsJ = epsK;
        }
      }
      if (i == 0) {
        eps = epsJ;
      } else if (eps < epsJ) {
        eps = epsJ;
      }
    }
    return eps;
  }

  @Override public String description() {
    return "Additive Epsilon quality indicator" ;
  }

  @Override public String name() {
    return "EP" ;
  }
}
