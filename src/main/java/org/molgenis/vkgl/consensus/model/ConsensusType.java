package org.molgenis.vkgl.consensus.model;

public enum ConsensusType {
  /** all lab classifications are equal */
  TOTAL_AGREEMENT("total_agreement"),
  /**
   * either all lab classifications are b/lb with at least one b and at least one lb or all lab
   * classifications are lp/p with at least one lp and at least one p
   */
  AGREEMENT("agreement"),
  /** at least one lab classification is vus and at least one lab classification is b/lb/lp/p */
  DISAGREEMENT("disagreement"),
  /** at least one lab classification is b/lb and at least one lab classification is lp/p */
  TOTAL_DISAGREEMENT("total_disagreement");

  private final String value;

  ConsensusType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
