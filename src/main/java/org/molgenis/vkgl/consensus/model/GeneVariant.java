package org.molgenis.vkgl.consensus.model;

import com.opencsv.bean.CsvBindByName;
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
public class GeneVariant {

  @CsvBindByName(column = "chromosome", required = true)
  String chromosome;

  @CsvBindByName(column = "start", required = true)
  long start;

  @CsvBindByName(column = "stop", required = true)
  long stop;

  @CsvBindByName(column = "ref", required = true)
  String ref;

  @CsvBindByName(column = "alt", required = true)
  String alt;

  @CsvBindByName(column = "gene", required = true)
  String gene;
}
