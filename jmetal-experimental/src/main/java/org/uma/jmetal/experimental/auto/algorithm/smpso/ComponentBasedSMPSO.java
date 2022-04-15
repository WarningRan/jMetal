package org.uma.jmetal.experimental.auto.algorithm.smpso;

import com.googlecode.jfilechooserbookmarks.example.Default;
import org.uma.jmetal.experimental.auto.algorithm.ParticleSwarmOptimizationAlgorithm;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.common.evaluation.impl.SequentialEvaluation;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.common.solutionscreation.impl.RandomSolutionsCreation;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestinitialization.impl.DefaultGlobalBestInitialization;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestselection.GlobalBestSelection;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestselection.impl.BinaryTournamentGlobalBestSelection;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestupdate.impl.DefaultGlobalBestUpdate;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.inertiaweightcomputingstrategy.impl.ConstantValueStrategy;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.localbestinitialization.impl.DefaultLocalBestInitialization;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.localbestupdate.impl.DefaultLocalBestUpdate;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.perturbation.impl.FrequencySelectionMutationBasedPerturbation;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.positionupdate.impl.DefaultPositionUpdate;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.velocityinitialization.impl.DefaultVelocityInitialization;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.velocityupdate.impl.ConstrainedVelocityUpdate;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT4;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.archive.BoundedArchive;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.comparator.dominanceComparator.impl.DefaultDominanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.observer.impl.RunTimeChartObserver;
import org.uma.jmetal.util.termination.impl.TerminationByEvaluations;

public class ComponentBasedSMPSO {

  public static void main(String[] args) {
    DoubleProblem problem = new ZDT4();
    String referenceFrontFileName = "resources/referenceFrontsCSV/ZDT4.csv";
    int swarmSize = 100;
    int maximumNumberOfEvaluations = 25000;

    var swarmInitialization = new RandomSolutionsCreation<>(problem, swarmSize);
    var evaluation = new SequentialEvaluation<>(problem);
    var termination = new TerminationByEvaluations(maximumNumberOfEvaluations);
    var velocityInitialization = new DefaultVelocityInitialization();
    var localBestInitialization = new DefaultLocalBestInitialization();
    var globalBestInitialization = new DefaultGlobalBestInitialization();

    BoundedArchive<DoubleSolution> externalArchive = new CrowdingDistanceArchive<>(swarmSize);
    GlobalBestSelection globalBestSelection ; //= new TournamentGlobalBestSelection(2, externalArchive.getComparator());
    globalBestSelection = new BinaryTournamentGlobalBestSelection(externalArchive.getComparator()) ;
    //globalBestSelection = new RandomGlobalBestSelection() ;

    double r1Min = 0.0;
    double r1Max = 1.0;
    double r2Min = 0.0;
    double r2Max = 1.0;
    double c1Min = 1.5;
    double c1Max = 2.5;
    double c2Min = 1.5;
    double c2Max = 2.5;
    double weight = 0.1;
    var inertiaWeightStrategy = new ConstantValueStrategy(weight) ;

    var velocityUpdate = new ConstrainedVelocityUpdate(r1Min, r1Max, r2Min, r2Max, c1Min, c1Max,
        c2Min, c2Max, problem);

    double velocityChangeWhenLowerLimitIsReached = -1.0;
    double velocityChangeWhenUpperLimitIsReached = -1.0;
    var positionUpdate = new DefaultPositionUpdate(velocityChangeWhenLowerLimitIsReached,
        velocityChangeWhenUpperLimitIsReached, problem.getBoundsForVariables());

    int frequencyOfMutation = 6;
    MutationOperator<DoubleSolution> mutationOperator = new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0) ;
    //mutationOperator = new UniformMutation(1.0/problem.getNumberOfVariables(), 0.5) ;
    var perturbation = new FrequencySelectionMutationBasedPerturbation(mutationOperator, frequencyOfMutation);
    var globalBestUpdate = new DefaultGlobalBestUpdate();
    var localBestUpdate = new DefaultLocalBestUpdate(new DefaultDominanceComparator<>());

    var smpso = new ParticleSwarmOptimizationAlgorithm("SMPSO",
        swarmInitialization,
        evaluation,
        termination,
        velocityInitialization,
        localBestInitialization,
        globalBestInitialization,
        inertiaWeightStrategy,
        velocityUpdate,
        positionUpdate,
        perturbation,
        globalBestUpdate,
        localBestUpdate,
        globalBestSelection,
        externalArchive);

    RunTimeChartObserver<DoubleSolution> runTimeChartObserver = new RunTimeChartObserver<>("SMPSO",
        80, 100, referenceFrontFileName);
    smpso.getObservable().register(runTimeChartObserver);

    smpso.run();

    System.out.println("Total computing time: " + smpso.getTotalComputingTime());

    new SolutionListOutput(smpso.getResult())
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
        .print();

    System.exit(0);
  }
}
