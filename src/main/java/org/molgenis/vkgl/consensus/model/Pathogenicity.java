package org.molgenis.vkgl.consensus.model;

public enum Pathogenicity {
  BENIGN("b"),
  LIKELY_BENIGN("lb"),
  VUS("vus"),
  LIKELY_PATHOGENIC("lp"),
  PATHOGENIC("p");

  private final String value;

  Pathogenicity(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
