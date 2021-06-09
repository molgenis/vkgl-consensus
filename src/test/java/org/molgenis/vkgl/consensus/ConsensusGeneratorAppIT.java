package org.molgenis.vkgl.consensus;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

class ConsensusGeneratorAppIT {

  @Test
  void main() throws IOException {
    Path outputDirectory = Files.createTempDirectory("vkgl_consensus");
    Path outputFile = Path.of(outputDirectory.toString(), "vkgl_consensus.tsv");
    try {
      ConsensusGeneratorApp.main(
          new String[] {
            "--amc",
            getPath("vkgl_transformed_amc.tsv"),
            "--erasmus_mc",
            getPath("vkgl_transformed_erasmus_mc.tsv"),
            "--lumc",
            getPath("vkgl_transformed_lumc.tsv"),
            "--nki",
            getPath("vkgl_transformed_nki.tsv"),
            "--radboud_mumc",
            getPath("vkgl_transformed_radboud_mumc.tsv"),
            "--umc_utrecht",
            getPath("vkgl_transformed_umc_utrecht.tsv"),
            "--umcg",
            getPath("vkgl_transformed_umcg.tsv"),
            "--vumc",
            getPath("vkgl_transformed_vumc.tsv"),
            "--output",
            outputFile.toString()
          });

      Path expectedOutputFile = Path.of("src", "test", "resources", "vkgl_consensus.tsv");

      String outputStr = Files.readString(expectedOutputFile, UTF_8).replaceAll("\r", "");
      String expectedOutputStr = Files.readString(outputFile, UTF_8).replaceAll("\r", "");
      assertEquals(expectedOutputStr, outputStr);
    } finally {
      FileUtils.deleteDirectory(outputDirectory.toFile());
    }
  }

  private static String getPath(String filename) {
    return Path.of("src", "test", "resources", filename).toString();
  }
}
