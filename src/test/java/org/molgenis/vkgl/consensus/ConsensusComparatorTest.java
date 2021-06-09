package org.molgenis.vkgl.consensus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.molgenis.vkgl.consensus.model.Consensus;

class ConsensusComparatorTest {

  private ConsensusComparator consensusComparator;

  @BeforeEach
  void setUp() {
    consensusComparator = new ConsensusComparator();
  }

  static Stream<Arguments> compareProvider() {
    return Stream.of(
        Arguments.of(
            Consensus.builder().chromosome("1").build(),
            Consensus.builder().chromosome("2").build(),
            -1),
        Arguments.of(
            Consensus.builder().chromosome("2").build(),
            Consensus.builder().chromosome("1").build(),
            1),
        Arguments.of(
            Consensus.builder().chromosome("1").build(),
            Consensus.builder().chromosome("X").build(),
            -1),
        Arguments.of(
            Consensus.builder().chromosome("X").build(),
            Consensus.builder().chromosome("1").build(),
            1),
        Arguments.of(
            Consensus.builder().chromosome("X").build(),
            Consensus.builder().chromosome("Y").build(),
            -1),
        Arguments.of(
            Consensus.builder().chromosome("Y").build(),
            Consensus.builder().chromosome("X").build(),
            1),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).build(),
            Consensus.builder().chromosome("1").start(2).build(),
            -1),
        Arguments.of(
            Consensus.builder().chromosome("1").start(2).build(),
            Consensus.builder().chromosome("1").start(1).build(),
            1),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("A").build(),
            Consensus.builder().chromosome("1").start(1).ref("C").build(),
            -2),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("C").build(),
            Consensus.builder().chromosome("1").start(1).ref("A").build(),
            2),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").build(),
            Consensus.builder().chromosome("1").start(1).ref("A").alt("T").build(),
            -17),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("A").alt("T").build(),
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").build(),
            17),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").gene("ABC").build(),
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").gene("BCD").build(),
            -1),
        Arguments.of(
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").gene("BCD").build(),
            Consensus.builder().chromosome("1").start(1).ref("A").alt("C").gene("ABC").build(),
            1),
        Arguments.of(
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(2)
                .build(),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(3)
                .build(),
            -1),
        Arguments.of(
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(3)
                .build(),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(2)
                .build(),
            1),
        Arguments.of(
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(2)
                .build(),
            Consensus.builder()
                .chromosome("1")
                .start(1)
                .ref("A")
                .alt("<sv>")
                .gene("ABC")
                .stop(2)
                .build(),
            0));
  }

  @ParameterizedTest
  @MethodSource("compareProvider")
  void compare(Consensus thisConsensus, Consensus thatConsensus, int result) {
    assertEquals(result, consensusComparator.compare(thisConsensus, thatConsensus));
  }
}
