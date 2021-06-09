package org.molgenis.vkgl.consensus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.molgenis.vkgl.consensus.model.ConsensusType.AGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.DISAGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.TOTAL_AGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.TOTAL_DISAGREEMENT;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.VUS;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.molgenis.vkgl.consensus.model.Classification;
import org.molgenis.vkgl.consensus.model.Consensus;
import org.molgenis.vkgl.consensus.model.GeneVariant;
import org.molgenis.vkgl.consensus.model.Lab;

class ConsensusMapperTest {

  private ConsensusMapper consensusMapper;

  @BeforeEach
  void setUp() {
    consensusMapper = new ConsensusMapper();
  }

  static Stream<Arguments> compareProvider() {
    GeneVariant geneVariant =
        GeneVariant.builder()
            .chromosome("1")
            .start(1)
            .stop(2)
            .ref("A")
            .alt("TA")
            .gene("ABC")
            .build();

    Arguments arguments0 =
        Arguments.of(
            geneVariant,
            Map.of(Lab.AMC, Classification.builder().pathogenicity(BENIGN).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(1)
                .pathogenicity(BENIGN.toString())
                .type(TOTAL_AGREEMENT.toString())
                .amc(BENIGN.toString())
                .build());

    Arguments arguments1 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(BENIGN).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(BENIGN).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(2)
                .pathogenicity(BENIGN.toString())
                .type(TOTAL_AGREEMENT.toString())
                .amc(BENIGN.toString())
                .erasmusMc(BENIGN.toString())
                .build());

    Arguments arguments2 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(BENIGN).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(LIKELY_BENIGN).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(2)
                .pathogenicity(LIKELY_BENIGN.toString())
                .type(AGREEMENT.toString())
                .amc(BENIGN.toString())
                .erasmusMc(LIKELY_BENIGN.toString())
                .build());

    Arguments arguments3 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(LIKELY_PATHOGENIC).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(PATHOGENIC).build(),
                Lab.LUMC,
                Classification.builder().pathogenicity(PATHOGENIC).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(3)
                .pathogenicity(LIKELY_PATHOGENIC.toString())
                .type(AGREEMENT.toString())
                .amc(LIKELY_PATHOGENIC.toString())
                .erasmusMc(PATHOGENIC.toString())
                .lumc(PATHOGENIC.toString())
                .build());

    Arguments arguments4 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(VUS).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(LIKELY_BENIGN).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(2)
                .type(DISAGREEMENT.toString())
                .amc(VUS.toString())
                .erasmusMc(LIKELY_BENIGN.toString())
                .build());

    Arguments arguments5 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(VUS).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(PATHOGENIC).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(2)
                .type(DISAGREEMENT.toString())
                .amc(VUS.toString())
                .erasmusMc(PATHOGENIC.toString())
                .build());

    Arguments arguments6 =
        Arguments.of(
            geneVariant,
            Map.of(
                Lab.AMC,
                Classification.builder().pathogenicity(BENIGN).build(),
                Lab.ERASMUS_MC,
                Classification.builder().pathogenicity(VUS).build(),
                Lab.LUMC,
                Classification.builder().pathogenicity(PATHOGENIC).build()),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("A")
                .alt("TA")
                .gene("ABC")
                .nrLabs(3)
                .type(TOTAL_DISAGREEMENT.toString())
                .amc(BENIGN.toString())
                .erasmusMc(VUS.toString())
                .lumc(PATHOGENIC.toString())
                .build());

    return Stream.of(
        arguments0, arguments1, arguments2, arguments3, arguments4, arguments5, arguments6);
  }

  @ParameterizedTest
  @MethodSource("compareProvider")
  void map(
      GeneVariant geneVariant, Map<Lab, Classification> labClassifications, Consensus consensus) {
    assertEquals(consensus, consensusMapper.map(geneVariant, labClassifications));
  }
}
