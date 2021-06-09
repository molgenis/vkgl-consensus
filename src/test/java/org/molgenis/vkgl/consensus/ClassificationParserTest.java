package org.molgenis.vkgl.consensus;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.molgenis.vkgl.consensus.model.Classification;
import org.molgenis.vkgl.consensus.model.GeneVariant;
import org.molgenis.vkgl.consensus.model.Pathogenicity;

class ClassificationParserTest {

  private ClassificationParser classificationParser;

  @BeforeEach
  void setUp() {
    classificationParser = new ClassificationParser();
  }

  @Test
  void parse() {
    String tsv =
        "id\tchromosome\tstart\tstop\tref\talt\tgene\tc_dna\thgvs_g\thgvs_c\ttranscript\tprotein\ttype\tlocation\texon\teffect\tclassification\tcomments\tis_legacy\tlab_upload_date\n"
            + "my_id\t1\t12059085\t12059086\tG\tGA\tMFN2\tc.749G>A\tNC_000001.10:g.12059085G>A\t\tNM_014874.3\tp.R250Q\tsub\texonic\t8\tnonsynonymous\tvus\t\t\t2000-12-31 23:59:59";

    Classification classification =
        Classification.builder()
            .geneVariant(
                GeneVariant.builder()
                    .chromosome("1")
                    .start(12059085)
                    .stop(12059086)
                    .ref("G")
                    .alt("GA")
                    .gene("MFN2")
                    .build())
            .pathogenicity(Pathogenicity.VUS)
            .build();
    assertEquals(singletonList(classification), classificationParser.parse(new StringReader(tsv)));
  }
}
