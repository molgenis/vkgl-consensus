package org.molgenis.vkgl.consensus;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.Reader;
import java.util.List;
import org.molgenis.vkgl.consensus.model.Classification;

public class ClassificationParser {

  List<Classification> parse(Reader reader) {
    CsvToBean<Classification> csvToBean =
        new CsvToBeanBuilder<Classification>(reader)
            .withSeparator('\t')
            .withType(Classification.class)
            .build();
    return csvToBean.parse();
  }
}
