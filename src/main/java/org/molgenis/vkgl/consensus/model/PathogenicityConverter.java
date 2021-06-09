package org.molgenis.vkgl.consensus.model;

import static java.lang.String.format;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class PathogenicityConverter<I> extends AbstractBeanField<Pathogenicity, I> {

  @Override
  protected Pathogenicity convert(String value) throws CsvConstraintViolationException {

    Pathogenicity pathogenicity;
    switch (value) {
      case "b":
        pathogenicity = Pathogenicity.BENIGN;
        break;
      case "lb":
        pathogenicity = Pathogenicity.LIKELY_BENIGN;
        break;
      case "vus":
        pathogenicity = Pathogenicity.VUS;
        break;
      case "lp":
        pathogenicity = Pathogenicity.LIKELY_PATHOGENIC;
        break;
      case "p":
        pathogenicity = Pathogenicity.PATHOGENIC;
        break;
      default:
        throw new CsvConstraintViolationException(
            format("invalid pathogenicity value '%s'", value));
    }
    return pathogenicity;
  }
}
