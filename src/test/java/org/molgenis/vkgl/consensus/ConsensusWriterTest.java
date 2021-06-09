package org.molgenis.vkgl.consensus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.molgenis.vkgl.consensus.model.ConsensusType.AGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.TOTAL_DISAGREEMENT;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.VUS;

import java.io.StringWriter;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.molgenis.vkgl.consensus.model.Consensus;

class ConsensusWriterTest {

  private ConsensusWriter consensusWriter;

  @BeforeEach
  void setUp() {
    consensusWriter = new ConsensusWriter();
  }

  static Stream<Arguments> writeProvider() {
    return Stream.of(
        Arguments.of(
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("T")
                .alt("TA")
                .gene("GENE")
                .nrLabs(8)
                .amc(BENIGN.toString())
                .erasmusMc(LIKELY_BENIGN.toString())
                .lumc(VUS.toString())
                .nki(LIKELY_PATHOGENIC.toString())
                .radboudMumc(PATHOGENIC.toString())
                .umcUtrecht(BENIGN.toString())
                .umcg(LIKELY_BENIGN.toString())
                .vumc(VUS.toString())
                .type(TOTAL_DISAGREEMENT.toString())
                .build(),
            "chromosome\tstart\tstop\tref\talt\tgene\tnr_labs\tamc\terasmus_mc\tlumc\tnki\tradboud_mumc\tumc_utrecht\tumcg\tvumc\tconsensus\ttype\n"
                + "1\t1\t2\tT\tTA\tGENE\t8\tb\tlb\tvus\tlp\tp\tb\tlb\tvus\t\ttotal_disagreement\n"),
        Arguments.of(
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .stop(2)
                .ref("T")
                .alt("TA")
                .gene("GENE")
                .nrLabs(2)
                .amc(BENIGN.toString())
                .erasmusMc(LIKELY_BENIGN.toString())
                .pathogenicity(LIKELY_BENIGN.toString())
                .type(AGREEMENT.toString())
                .build(),
            "chromosome\tstart\tstop\tref\talt\tgene\tnr_labs\tamc\terasmus_mc\tlumc\tnki\tradboud_mumc\tumc_utrecht\tumcg\tvumc\tconsensus\ttype\n"
                + "1\t1\t2\tT\tTA\tGENE\t2\tb\tlb\t\t\t\t\t\t\tlb\tagreement\n"));
  }

  @ParameterizedTest
  @MethodSource("writeProvider")
  void write(Consensus consensus, String tsvValue) {
    StringWriter stringWriter = new StringWriter();
    consensusWriter.write(Collections.singletonList(consensus), stringWriter);
    assertEquals(tsvValue, stringWriter.toString());
  }
}
