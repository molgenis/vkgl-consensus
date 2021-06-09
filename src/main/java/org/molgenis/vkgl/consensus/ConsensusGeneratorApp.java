package org.molgenis.vkgl.consensus;

import static java.lang.String.format;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import ch.qos.logback.classic.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.molgenis.vkgl.consensus.model.Lab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsensusGeneratorApp {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsensusGeneratorApp.class);

  private static final String OPTION_AMC = "amc";
  private static final String OPTION_ERASMUS_MC = "erasmus_mc";
  private static final String OPTION_LUMC = "lumc";
  private static final String OPTION_NKI = "nki";
  private static final String OPTION_RADBOUD_MUMC = "radboud_mumc";
  private static final String OPTION_UMCG = "umcg";
  private static final String OPTION_UMC_UTRECHT = "umc_utrecht";
  private static final String OPTION_VUMC = "vumc";
  private static final String OPTION_OUTPUT = "output";
  private static final String OPTION_FORCE = "force";

  public static void main(String[] args) {
    ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME))
        .setLevel(Level.INFO);

    CommandLineParser parser = new DefaultParser();
    Options options = createOptions();

    Map<Lab, Path> labPaths;
    Path outputPath;

    try {
      CommandLine line = parser.parse(options, args);

      labPaths = new EnumMap<>(Lab.class);
      labPaths.put(Lab.AMC, getPath(line, OPTION_AMC));
      labPaths.put(Lab.ERASMUS_MC, getPath(line, OPTION_ERASMUS_MC));
      labPaths.put(Lab.LUMC, getPath(line, OPTION_LUMC));
      labPaths.put(Lab.NKI, getPath(line, OPTION_NKI));
      labPaths.put(Lab.RADBOUD_MUMC, getPath(line, OPTION_RADBOUD_MUMC));
      labPaths.put(Lab.UMCG, getPath(line, OPTION_UMCG));
      labPaths.put(Lab.UMCU, getPath(line, OPTION_UMC_UTRECHT));
      labPaths.put(Lab.VUMC, getPath(line, OPTION_VUMC));

      outputPath = Path.of(line.getOptionValue(OPTION_OUTPUT));
      if (!line.hasOption(OPTION_FORCE) && Files.exists(outputPath)) {
        LOGGER.error("file '{}' already exist, use -f / --force to overwrite", outputPath);
        printHelp(options);
        return;
      }
    } catch (ParseException exp) {
      LOGGER.error("Parsing failed.  Reason: {}", exp.getMessage());
      printHelp(options);
      return;
    } catch (RuntimeException e) {
      LOGGER.error(e.getMessage());
      printHelp(options);
      return;
    }

    LOGGER.info("generating VKGL variant classification consensus ...");
    ConsensusGenerator consensusGenerator =
        new ConsensusGenerator(
            new ClassificationParser(), new ConsensusMapper(), new ConsensusWriter());

    consensusGenerator.generateConsensus(labPaths, outputPath);

    LOGGER.info("generating VKGL variant classification consensus done");
  }

  private static void printHelp(Options options) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.setWidth(100);
    helpFormatter.printHelp(
        "vkgl_consensus",
        null,
        options,
        "creates a tab-separated consensus file based on tab-separated files per lab that are produced by the the data-transform-vkgl pipeline.");
  }

  private static Path getPath(CommandLine commandLine, String option) {
    String optionValue = commandLine.getOptionValue(option);
    Path path = Path.of(optionValue);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException(format("file '%s' does not exist", optionValue));
    }
    return path;
  }

  private static Options createOptions() {
    Options options = new Options();
    options.addRequiredOption("a", OPTION_AMC, true, "Required: transformed AMC lab data (.tsv).");
    options.addRequiredOption(
        "e", OPTION_ERASMUS_MC, true, "Required: transformed Erasmus MC lab data (.tsv).");
    options.addRequiredOption(
        "l", OPTION_LUMC, true, "Required: transformed LUMC lab data (.tsv).");
    options.addRequiredOption("n", OPTION_NKI, true, "Required: transformed NKI lab data (.tsv).");
    options.addRequiredOption(
        "r", OPTION_RADBOUD_MUMC, true, "Required: transformed Radboud MUMC lab data (.tsv).");
    options.addRequiredOption(
        "u", OPTION_UMC_UTRECHT, true, "Required: transformed UMC Utrecht lab data (.tsv).");
    options.addRequiredOption(
        "g", OPTION_UMCG, true, "Required: transformed UMCG lab data (.tsv).");
    options.addRequiredOption(
        "v", OPTION_VUMC, true, "Required: transformed VUMC lab data (.tsv).");
    options.addRequiredOption("o", OPTION_OUTPUT, true, "Required: output consensus data (.tsv)");
    options.addOption("f", OPTION_FORCE, false, "Overwrite output file if it already exists.");
    return options;
  }
}
