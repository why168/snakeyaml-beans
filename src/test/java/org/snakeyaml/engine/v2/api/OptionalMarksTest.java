/*
 * Copyright (c) 2018, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.snakeyaml.engine.v2.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.usecases.external_test_suite.SuiteData;
import org.snakeyaml.engine.usecases.external_test_suite.SuiteUtils;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.exceptions.ParserException;
import org.snakeyaml.engine.v2.nodes.Node;

@Tag("fast")
class OptionalMarksTest {

  @Test
  @DisplayName("Compose: no marks")
  void composeWithoutMarks() {
    SuiteData data = SuiteUtils.getOne("2AUY");
    LoadSettings settings =
        LoadSettings.builder().setLabel(data.getLabel()).setUseMarks(false).build();
    Optional<Node> node = new Compose(settings).composeString("{a: 4}");
    assertTrue(node.isPresent());
  }

  @Test
  @DisplayName("Compose: failure with marks")
  void composeErrorWithoutMarks2() {
    SuiteData data = SuiteUtils.getOne("2AUY");
    LoadSettings settings =
        LoadSettings.builder().setLabel(data.getLabel()).setUseMarks(true).build();
    ParserException exception =
        assertThrows(ParserException.class, () -> new Compose(settings).composeString("{a: 4}}"));
    assertTrue(exception.getMessage().contains("line 1, column 7:"),
        "The error must contain Mark data.");
  }


  @Test
  @DisplayName("Compose: failure without marks")
  void composeErrorWithoutMarks() {
    SuiteData data = SuiteUtils.getOne("2AUY");
    LoadSettings settings =
        LoadSettings.builder().setLabel(data.getLabel()).setUseMarks(false).build();
    ParserException exception =
        assertThrows(ParserException.class, () -> new Compose(settings).composeString("{a: 4}}"));
    assertEquals("expected '<document start>', but found '}'\n", exception.getMessage());
  }
}

