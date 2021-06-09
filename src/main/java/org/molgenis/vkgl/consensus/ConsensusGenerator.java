package org.molgenis.vkgl.consensus;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.molgenis.vkgl.consensus.model.Classification;
import org.molgenis.vkgl.consensus.model.Consensus;
import org.molgenis.vkgl.consensus.model.GeneVariant;
import org.molgenis.vkgl.consensus.model.Lab;

public class ConsensusGenerator {

  private final ClassificationParser classificationParser;
  private final ConsensusMapper consensusMapper;
  private final ConsensusWriter consensusWriter;

  ConsensusGenerator(
      ClassificationParser classificationParser,
      ConsensusMapper consensusMapper,
      ConsensusWriter consensusWriter) {
    this.classificationParser = requireNonNull(classificationParser);
    this.consensusMapper = requireNonNull(consensusMapper);
    this.consensusWriter = requireNonNull(consensusWriter);
  }

  void generateConsensus(Map<Lab, Path> labPaths, Path outputPath) {
    Map<GeneVariant, Map<Lab, Classification>> variantClassifications = new HashMap<>();

    labPaths.forEach(
        (lab, path) ->
            parseClassifications(path)
                .forEach(
                    classification ->
                        variantClassifications
                            .computeIfAbsent(
                                classification.getGeneVariant(), k -> new EnumMap<>(Lab.class))
                            .put(lab, classification)));

    List<Consensus> consensuses =
        variantClassifications.entrySet().stream()
            .map(entry -> consensusMapper.map(entry.getKey(), entry.getValue()))
            .sorted(new ConsensusComparator())
            .collect(toList());

    writeClassifications(consensuses, outputPath);
  }

  private List<Classification> parseClassifications(Path path) {
    List<Classification> classifications;
    try {
      classifications = classificationParser.parse(Files.newBufferedReader(path));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return classifications;
  }

  private void writeClassifications(List<Consensus> consensuses, Path outputPath) {
    try (Writer writer = Files.newBufferedWriter(outputPath, UTF_8)) {
      consensusWriter.write(consensuses, writer);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
