package org.molgenis.vkgl.consensus;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static org.molgenis.vkgl.consensus.model.ConsensusType.AGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.DISAGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.TOTAL_AGREEMENT;
import static org.molgenis.vkgl.consensus.model.ConsensusType.TOTAL_DISAGREEMENT;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_BENIGN;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.LIKELY_PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.PATHOGENIC;
import static org.molgenis.vkgl.consensus.model.Pathogenicity.VUS;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.molgenis.vkgl.consensus.model.Classification;
import org.molgenis.vkgl.consensus.model.Consensus;
import org.molgenis.vkgl.consensus.model.Consensus.ConsensusBuilder;
import org.molgenis.vkgl.consensus.model.ConsensusType;
import org.molgenis.vkgl.consensus.model.GeneVariant;
import org.molgenis.vkgl.consensus.model.Lab;
import org.molgenis.vkgl.consensus.model.Pathogenicity;

public class ConsensusMapper {

  public Consensus map(GeneVariant geneVariant, Map<Lab, Classification> labClassifications) {
    ConsensusBuilder builder =
        Consensus.builder()
            .chromosome(geneVariant.getChromosome())
            .start(geneVariant.getStart())
            .stop(geneVariant.getStop())
            .ref(geneVariant.getRef())
            .alt(geneVariant.getAlt())
            .gene(geneVariant.getGene())
            .nrLabs(labClassifications.size());

    setLabPathogenicity(labClassifications, builder);
    setConsensus(labClassifications, builder);

    return builder.build();
  }

  private static void setLabPathogenicity(
      Map<Lab, Classification> labClassifications, ConsensusBuilder builder) {
    labClassifications.forEach(
        (lab, classification) -> {
          String pathogenicity = classification.getPathogenicity().toString();
          switch (lab) {
            case AMC:
              builder.amc(pathogenicity);
              break;
            case ERASMUS_MC:
              builder.erasmusMc(pathogenicity);
              break;
            case LUMC:
              builder.lumc(pathogenicity);
              break;
            case NKI:
              builder.nki(pathogenicity);
              break;
            case RADBOUD_MUMC:
              builder.radboudMumc(pathogenicity);
              break;
            case UMCG:
              builder.umcg(pathogenicity);
              break;
            case UMCU:
              builder.umcUtrecht(pathogenicity);
              break;
            case VUMC:
              builder.vumc(pathogenicity);
              break;
            default:
              throw new IllegalArgumentException(format("unexpected enum '%s'", lab));
          }
        });
  }

  private static void setConsensus(
      Map<Lab, Classification> labClassifications, ConsensusBuilder builder) {
    Set<Pathogenicity> pathogenicitySet =
        labClassifications.values().stream().map(Classification::getPathogenicity).collect(toSet());

    Pathogenicity pathogenicity;
    ConsensusType consensusType;
    if (pathogenicitySet.size() == 1) {
      pathogenicity = pathogenicitySet.iterator().next();
      consensusType = TOTAL_AGREEMENT;
    } else if (pathogenicitySet.equals(EnumSet.of(BENIGN, LIKELY_BENIGN))) {
      pathogenicity = LIKELY_BENIGN;
      consensusType = AGREEMENT;
    } else if (pathogenicitySet.equals(EnumSet.of(LIKELY_PATHOGENIC, PATHOGENIC))) {
      pathogenicity = LIKELY_PATHOGENIC;
      consensusType = AGREEMENT;
    } else if (pathogenicitySet.contains(VUS)
            && (containsBenignFlavor(pathogenicitySet)
                && !containsPathogenicFlavor(pathogenicitySet))
        || (!containsBenignFlavor(pathogenicitySet)
            && containsPathogenicFlavor(pathogenicitySet))) {
      pathogenicity = null;
      consensusType = DISAGREEMENT;
    } else {
      pathogenicity = null;
      consensusType = TOTAL_DISAGREEMENT;
    }

    if (pathogenicity != null) {
      builder.pathogenicity(pathogenicity.toString());
    }
    builder.type(consensusType.toString());
  }

  private static boolean containsBenignFlavor(Set<Pathogenicity> pathogenicitySet) {
    return pathogenicitySet.contains(BENIGN) || pathogenicitySet.contains(LIKELY_BENIGN);
  }

  private static boolean containsPathogenicFlavor(Set<Pathogenicity> pathogenicitySet) {
    return pathogenicitySet.contains(LIKELY_PATHOGENIC) || pathogenicitySet.contains(PATHOGENIC);
  }
}
