package org.molgenis.vkgl.consensus;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.Writer;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.molgenis.vkgl.consensus.model.Consensus;

public class ConsensusWriter {

  private final CustomMappingStrategy<Consensus> mappingStrategy;

  public ConsensusWriter() {
    mappingStrategy = new CustomMappingStrategy<>();
    mappingStrategy.setType(Consensus.class);
  }

  public void write(List<Consensus> consensuses, Writer writer) {
    try {
      new StatefulBeanToCsvBuilder<Consensus>(writer)
          .withSeparator('\t')
          .withApplyQuotesToAll(false)
          .withMappingStrategy(mappingStrategy)
          .build()
          .write(consensuses);
    } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /** source: https://stackoverflow.com/a/58833974 */
  private static class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
      final int numColumns = getFieldMap().values().size();
      super.generateHeader(bean);

      String[] header = new String[numColumns];

      BeanField<T, Integer> beanField;
      for (int i = 0; i < numColumns; i++) {
        beanField = findField(i);
        String columnHeaderName = extractHeaderName(beanField);
        header[i] = columnHeaderName;
      }
      return header;
    }

    private String extractHeaderName(final BeanField<T, Integer> beanField) {
      if (beanField == null
          || beanField.getField() == null
          || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
        return StringUtils.EMPTY;
      }

      final CsvBindByName bindByNameAnnotation =
          beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0];
      return bindByNameAnnotation.column();
    }
  }
}
