package org.uma.jmetal.experimental.auto.experiments.DTLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.experimental.auto.algorithm.EvolutionaryAlgorithm;
import org.uma.jmetal.experimental.auto.algorithm.ParticleSwarmOptimizationAlgorithm;
import org.uma.jmetal.experimental.auto.algorithm.mopso.AutoMOPSO;
import org.uma.jmetal.experimental.auto.algorithm.nsgaii.AutoNSGAII;
import org.uma.jmetal.lab.experiment.Experiment;
import org.uma.jmetal.lab.experiment.ExperimentBuilder;
import org.uma.jmetal.lab.experiment.component.impl.ComputeQualityIndicators;
import org.uma.jmetal.lab.experiment.component.impl.ExecuteAlgorithms;
import org.uma.jmetal.lab.experiment.component.impl.GenerateBoxplotsWithR;
import org.uma.jmetal.lab.experiment.component.impl.GenerateFriedmanTestTables;
import org.uma.jmetal.lab.experiment.component.impl.GenerateHtmlPages;
import org.uma.jmetal.lab.experiment.component.impl.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.lab.experiment.component.impl.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.lab.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.lab.experiment.util.ExperimentProblem;
import org.uma.jmetal.lab.visualization.StudyVisualizer;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ5_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ6_2D;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ7_2D;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.NormalizedHypervolume;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.errorchecking.Check;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Automatic configuration of NSGA-II and MOPSO with jMetal and irace.
 *
 * @author Antonio J. Nebro
 * @author Daniel Doblas
 */
public class autoAlgorithmWithDTLZExperiments {
    private static final int INDEPENDENT_RUNS = 25;

    public static void main(String[] args) throws IOException {
        Check.that(args.length == 1, "Missing argument: experimentBaseDirectory");

        String experimentBaseDirectory = args[0];

        List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new DTLZ1_2D()).setReferenceFront("DTLZ1.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ2_2D()).setReferenceFront("DTLZ2.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ3_2D()).setReferenceFront("DTLZ3.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ4_2D()).setReferenceFront("DTLZ4.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ5_2D()).setReferenceFront("DTLZ5.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ6_2D()).setReferenceFront("DTLZ6.2D.csv"));
        problemList.add(new ExperimentProblem<>(new DTLZ7_2D()).setReferenceFront("DTLZ7.2D.csv"));


        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
                configureAlgorithmList(problemList);

        Experiment<DoubleSolution, List<DoubleSolution>> experiment =
                new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("DTLZAutoAlgorithmExperiments")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setReferenceFrontDirectory("resources/referenceFrontsCSV")
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        .setIndicatorList(Arrays.asList(
                                new Epsilon(),
                                new Spread(),
                                new GenerationalDistance(),
                                new PISAHypervolume(),
                                new NormalizedHypervolume(),
                                new InvertedGenerationalDistance(),
                                new InvertedGenerationalDistancePlus()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();

        new ExecuteAlgorithms<>(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
        new GenerateWilcoxonTestTablesWithR<>(experiment).run();
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run();
        new GenerateHtmlPages<>(experiment, StudyVisualizer.TYPE_OF_FRONT_TO_SHOW.MEDIAN).run();
    }

    /**
     * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
     * a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
     */
    static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
            List<ExperimentProblem<DoubleSolution>> problemList) {
        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();
        for (int run = 0; run < INDEPENDENT_RUNS; run++) {
            for (ExperimentProblem<DoubleSolution> experimentProblem : problemList) {
                Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<DoubleSolution>(
                        experimentProblem.getProblem(),
                        new SBXCrossover(1.0, 20.0),
                        new PolynomialMutation(1.0 / experimentProblem.getProblem().getNumberOfVariables(),
                                20.0),
                        100)
                        .build();
                algorithms.add(new ExperimentAlgorithm<>(algorithm, experimentProblem, run));
            }

            for (ExperimentProblem<DoubleSolution> experimentProblem : problemList) {
                double mutationProbability = 1.0 / experimentProblem.getProblem().getNumberOfVariables();
                double mutationDistributionIndex = 20.0;
                Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder(
                        (DoubleProblem) experimentProblem.getProblem(),
                        new CrowdingDistanceArchive<DoubleSolution>(100))
                        .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
                        .setMaxIterations(500)
                        .setSwarmSize(100)
                        .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
                        .build();
                algorithms.add(new ExperimentAlgorithm<>(algorithm, experimentProblem, run));
            }


            for (ExperimentProblem<DoubleSolution> experimentProblem : problemList) {

                /* AutoMOPSO without config*/
                String[] parametersAutoMOPSOWithoutConfig = ("--problemName " + experimentProblem.getProblem().getClass().getName() + " "
                        + "--referenceFrontFileName " + experimentProblem.getReferenceFront() + " "
                        + "--maximumNumberOfEvaluations 25000 "
                        + "--swarmSize 109 --archiveSize 100 "
                        + "--externalArchive crowdingDistanceArchive "
                        + "--swarmInitialization random "
                        + "--velocityInitialization defaultVelocityInitialization "
                        + "--perturbation frequencySelectionMutationBasedPerturbation "
                        + "--mutation uniform "
                        + "--mutationProbability 0.4716 "
                        + "--mutationRepairStrategy round "
                        + "--uniformMutationPerturbation 0.3557 "
                        + "--frequencyOfApplicationOfMutationOperator 6 "
                        + "--inertiaWeightComputingStrategy constantValue "
                        + "--weight 0.9698 "
                        + "--velocityUpdate constrainedVelocityUpdate "
                        + "--c1Min 1.9028 "
                        + "--c1Max 2.0387 "
                        + "--c2Min 1.9519 "
                        + "--c2Max 2.1047 "
                        + "--localBestInitialization defaultLocalBestInitialization "
                        + "--globalBestInitialization defaultGlobalBestInitialization "
                        + "--globalBestSelection binaryTournament "
                        + "--globalBestUpdate defaultGlobalBestUpdate "
                        + "--localBestUpdate defaultLocalBestUpdate "
                        + "--positionUpdate defaultPositionUpdate "
                        + "--velocityChangeWhenLowerLimitIsReached -0.8814 "
                        + "--velocityChangeWhenUpperLimitIsReached -0.3476"
                )
                        .split("\\s+");
                AutoMOPSO AutoMOPSOWithoutConfig = new AutoMOPSO();
                AutoMOPSOWithoutConfig.parseAndCheckParameters(parametersAutoMOPSOWithoutConfig);
                ParticleSwarmOptimizationAlgorithm automopsowithoutconfig = AutoMOPSOWithoutConfig.create();

                algorithms.add(new ExperimentAlgorithm<>(automopsowithoutconfig, "AutoMOPSOWithoutConfig", experimentProblem, run));



                /* AutoMOPSO with config*/
                String[] parametersAutoMOPSOWithConfig = ("--problemName " + experimentProblem.getProblem().getClass().getName() + " "
                        + "--referenceFrontFileName " + experimentProblem.getReferenceFront() + " "
                        + "--maximumNumberOfEvaluations 25000 "
                        + "--swarmSize 35 --archiveSize 100 "
                        + "--externalArchive crowdingDistanceArchive "
                        + "--swarmInitialization scatterSearch "
                        + "--velocityInitialization defaultVelocityInitialization "
                        + "--perturbation frequencySelectionMutationBasedPerturbation "
                        + "--mutation polynomial "
                        + "--mutationProbability 0.2339 "
                        + "--mutationRepairStrategy bounds "
                        + "--polynomialMutationDistributionIndex 352.4615 "
                        + "--frequencyOfApplicationOfMutationOperator 10 "
                        + "--inertiaWeightComputingStrategy constantValue "
                        + "--weight 0.9992 "
                        + "--velocityUpdate constrainedVelocityUpdate "
                        + "--c1Min 1.9335 "
                        + "--c1Max 2.0132 "
                        + "--c2Min 1.9428 "
                        + "--c2Max 2.1666 "
                        + "--localBestInitialization defaultLocalBestInitialization "
                        + "--globalBestInitialization defaultGlobalBestInitialization "
                        + "--globalBestSelection binaryTournament "
                        + "--globalBestUpdate defaultGlobalBestUpdate "
                        + "--localBestUpdate defaultLocalBestUpdate "
                        + "--positionUpdate defaultPositionUpdate "
                        + "--velocityChangeWhenLowerLimitIsReached -0.6101 "
                        + "--velocityChangeWhenUpperLimitIsReached -0.1871"
                )
                        .split("\\s+");
                AutoMOPSO AutoMOPSOWithConfig = new AutoMOPSO();
                AutoMOPSOWithConfig.parseAndCheckParameters(parametersAutoMOPSOWithConfig);
                ParticleSwarmOptimizationAlgorithm automopsowithconfig = AutoMOPSOWithConfig.create();

                algorithms.add(new ExperimentAlgorithm<>(automopsowithconfig, "AutoMOPSOWithConfig", experimentProblem, run));

                /* AutoMOPSO with config and HVIGDPlus*/
                String[] parametersAutoMOPSOWithConfigHVIGDPlus = ("--problemName " + experimentProblem.getProblem().getClass().getName() + " "
                        + "--referenceFrontFileName " + experimentProblem.getReferenceFront() + " "
                        + "--maximumNumberOfEvaluations 25000 "
                        + "--swarmSize 38 --archiveSize 100 "
                        + "--externalArchive crowdingDistanceArchive "
                        + "--swarmInitialization latinHypercubeSampling "
                        + "--velocityInitialization defaultVelocityInitialization "
                        + "--perturbation frequencySelectionMutationBasedPerturbation "
                        + "--mutation polynomial "
                        + "--mutationProbability 0.0066 "
                        + "--mutationRepairStrategy random "
                        + "--polynomialMutationDistributionIndex 269.407 "
                        + "--frequencyOfApplicationOfMutationOperator 10 "
                        + "--inertiaWeightComputingStrategy linearDecreasingValue "
                        + "--weightMin 0.3095 --weightMax 0.9954 "
                        + "--velocityUpdate constrainedVelocityUpdate "
                        + "--c1Min 1.9219 "
                        + "--c1Max 2.0297 "
                        + "--c2Min 1.94 "
                        + "--c2Max 2.1127 "
                        + "--localBestInitialization defaultLocalBestInitialization "
                        + "--globalBestInitialization defaultGlobalBestInitialization "
                        + "--globalBestSelection binaryTournament "
                        + "--globalBestUpdate defaultGlobalBestUpdate "
                        + "--localBestUpdate defaultLocalBestUpdate "
                        + "--positionUpdate defaultPositionUpdate "
                        + "--velocityChangeWhenLowerLimitIsReached -0.7964 "
                        + "--velocityChangeWhenUpperLimitIsReached -0.4"
                )
                        .split("\\s+");
                AutoMOPSO AutoMOPSOWithConfigHVIGDPlus = new AutoMOPSO();
                AutoMOPSOWithConfigHVIGDPlus.parseAndCheckParameters(parametersAutoMOPSOWithConfigHVIGDPlus);
                ParticleSwarmOptimizationAlgorithm automopsoWithConfigHVIGDPlus = AutoMOPSOWithConfigHVIGDPlus.create();

                algorithms.add(new ExperimentAlgorithm<>(automopsoWithConfigHVIGDPlus, "AutoMOPSOWithConfigHVIGDPlus", experimentProblem, run));


                /* OMOPSO */
                String[] parametersOMOPSO = ("--problemName " + experimentProblem.getProblem().getClass()
                        .getName() + " "
                        + "--referenceFrontFileName " + experimentProblem.getReferenceFront() + " "
                        + "--maximumNumberOfEvaluations 25000 "
                        + "--swarmSize 100 "
                        + "--archiveSize 100 "
                        + "--swarmInitialization random "
                        + "--velocityInitialization defaultVelocityInitialization "
                        + "--externalArchive crowdingDistanceArchive "
                        + "--localBestInitialization defaultLocalBestInitialization "
                        + "--globalBestInitialization defaultGlobalBestInitialization "
                        + "--globalBestSelection binaryTournament "
                        + "--perturbation frequencySelectionMutationBasedPerturbation "
                        + "--frequencyOfApplicationOfMutationOperator 7 "
                        + "--mutation polynomial "
                        + "--mutationProbability 1.0 "
                        + "--mutationRepairStrategy round "
                        + "--polynomialMutationDistributionIndex 20.0 "
                        + "--positionUpdate defaultPositionUpdate "
                        + "--globalBestUpdate defaultGlobalBestUpdate "
                        + "--localBestUpdate defaultLocalBestUpdate "
                        + "--velocityUpdate defaultVelocityUpdate "
                        + "--inertiaWeightComputingStrategy randomSelectedValue "
                        + "--c1Min 1.5 "
                        + "--c1Max 2.0 "
                        + "--c2Min 1.5 "
                        + "--c2Max 2.0 "
                        + "--weightMin 0.1  "
                        + "--weightMax 0.5 "
                        + "--velocityChangeWhenLowerLimitIsReached -1.0 "
                        + "--velocityChangeWhenUpperLimitIsReached -1.0 "
                )
                        .split("\\s+");
                AutoMOPSO OMOPSO = new AutoMOPSO();
                OMOPSO.parseAndCheckParameters(parametersOMOPSO);
                ParticleSwarmOptimizationAlgorithm omopso = OMOPSO.create();

                algorithms.add(new ExperimentAlgorithm<>(omopso, "OMOPSO", experimentProblem, run));
            }

        }
        return algorithms;
    }
}
