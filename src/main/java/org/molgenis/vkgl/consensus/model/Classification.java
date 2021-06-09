package org.molgenis.vkgl.consensus.model;

import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Classification {

  @CsvRecurse GeneVariant geneVariant;

  @CsvCustomBindByName(
      column = "classification",
      required = true,
      converter = PathogenicityConverter.class)
  Pathogenicity pathogenicity;
}
