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
package org.snakeyaml.engine.v2.tokens;


import java.util.Optional;
import org.snakeyaml.engine.v2.exceptions.Mark;

public final class BlockSequenceStartToken extends Token {

  public BlockSequenceStartToken(Optional<Mark> startMark, Optional<Mark> endMark) {
    super(startMark, endMark);
  }

  @Override
  public Token.ID getTokenId() {
    return ID.BlockSequenceStart;
  }
}
