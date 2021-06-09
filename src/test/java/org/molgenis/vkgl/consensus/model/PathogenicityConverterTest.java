package org.molgenis.vkgl.consensus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.opencsv.exceptions.CsvConstraintViolationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PathogenicityConverterTest {

  private PathogenicityConverter<Integer> pathogenicityConverter;

  @BeforeEach
  void setUp() {
    pathogenicityConverter = new PathogenicityConverter<>();
  }

  static Stream<Arguments> convertProvider() {
    return Stream.of(
        Arguments.of("b", Pathogenicity.BENIGN),
        Arguments.of("lb", Pathogenicity.LIKELY_BENIGN),
        Arguments.of("vus", Pathogenicity.VUS),
        Arguments.of("lp", Pathogenicity.LIKELY_PATHOGENIC),
        Arguments.of("p", Pathogenicity.PATHOGENIC));
  }

  @ParameterizedTest
  @MethodSource("convertProvider")
  void convert(String value, Pathogenicity pathogenicity) throws CsvConstraintViolationException {
    assertEquals(pathogenicity, pathogenicityConverter.convert(value));
  }

  @Test
  void convertCsvConstraintViolationException() {
    assertThrows(
        CsvConstraintViolationException.class, () -> pathogenicityConverter.convert("invalid"));
  }
}
