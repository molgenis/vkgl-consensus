package org.molgenis.vkgl.consensus.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
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
public class Consensus {

  @CsvBindByPosition(position = 0)
  @CsvBindByName(column = "chromosome", required = true)
  String chromosome;

  @CsvBindByPosition(position = 1)
  @CsvBindByName(column = "start", required = true)
  long start;

  @CsvBindByPosition(position = 2)
  @CsvBindByName(column = "stop", required = true)
  long stop;

  @CsvBindByPosition(position = 3)
  @CsvBindByName(column = "ref", required = true)
  String ref;

  @CsvBindByPosition(position = 4)
  @CsvBindByName(column = "alt", required = true)
  String alt;

  @CsvBindByPosition(position = 5)
  @CsvBindByName(column = "gene", required = true)
  String gene;

  @CsvBindByPosition(position = 6)
  @CsvBindByName(column = "nr_labs", required = true)
  int nrLabs;

  @CsvBindByPosition(position = 7)
  @CsvBindByName(column = "amc")
  String amc;

  @CsvBindByPosition(position = 8)
  @CsvBindByName(column = "erasmus_mc")
  String erasmusMc;

  @CsvBindByPosition(position = 9)
  @CsvBindByName(column = "lumc")
  String lumc;

  @CsvBindByPosition(position = 10)
  @CsvBindByName(column = "nki")
  String nki;

  @CsvBindByPosition(position = 11)
  @CsvBindByName(column = "radboud_mumc")
  String radboudMumc;

  @CsvBindByPosition(position = 12)
  @CsvBindByName(column = "umc_utrecht")
  String umcUtrecht;

  @CsvBindByPosition(position = 13)
  @CsvBindByName(column = "umcg")
  String umcg;

  @CsvBindByPosition(position = 14)
  @CsvBindByName(column = "vumc")
  String vumc;

  @CsvBindByPosition(position = 15)
  @CsvBindByName(column = "consensus")
  String pathogenicity;

  @CsvBindByPosition(position = 16)
  @CsvBindByName(column = "type")
  String type;
}
