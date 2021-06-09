package org.molgenis.vkgl.consensus;

import java.util.Comparator;
import org.molgenis.vkgl.consensus.model.Consensus;

public class ConsensusComparator implements Comparator<Consensus> {

  @Override
  public int compare(Consensus thisConsensus, Consensus thatConsensus) {
    int result = compareChromosome(thisConsensus, thatConsensus);
    if (result == 0) {
      result = Long.compare(thisConsensus.getStart(), thatConsensus.getStart());
    }
    if (result == 0) {
      result = thisConsensus.getRef().compareTo(thatConsensus.getRef());
    }
    if (result == 0) {
      result = thisConsensus.getAlt().compareTo(thatConsensus.getAlt());
    }
    if (result == 0) {
      result = thisConsensus.getGene().compareTo(thatConsensus.getGene());
    }
    if (result == 0) {
      result = Long.compare(thisConsensus.getStop(), thatConsensus.getStop());
    }
    return result;
  }

  private int compareChromosome(Consensus thisConsensus, Consensus thatConsensus) {
    String thisChromosome = thisConsensus.getChromosome();
    String thatChromosome = thatConsensus.getChromosome();

    boolean thisIsInteger = isInteger(thisChromosome);
    boolean thatIsInteger = isInteger(thatChromosome);

    int result;
    if (thisIsInteger && thatIsInteger) {
      result = Integer.compare(Integer.parseInt(thisChromosome), Integer.parseInt(thatChromosome));
    } else if (thisIsInteger) {
      result = -1;
    } else if (thatIsInteger) {
      result = 1;
    } else {
      result = thisChromosome.compareTo(thatChromosome);
    }
    return result;
  }

  private static boolean isInteger(String s) {
    boolean isInteger;

    try {
      Integer.parseInt(s);
      isInteger = true;
    } catch (NumberFormatException e) {
      isInteger = false;
    }

    return isInteger;
  }
}
