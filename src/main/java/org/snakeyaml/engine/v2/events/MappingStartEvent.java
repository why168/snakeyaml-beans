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
package org.snakeyaml.engine.v2.events;


import java.util.Optional;
import org.snakeyaml.engine.v2.common.Anchor;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.exceptions.Mark;

/**
 * Marks the beginning of a mapping node.
 * <p>
 * This event is followed by a number of key value pairs. <br>
 * The pairs are not in any particular order. However, the value always directly follows the
 * corresponding key. <br>
 * After the key value pairs follows a {@link MappingEndEvent}.
 * </p>
 * <p>
 * There must be an even number of node events between the start and end event.
 * </p>
 *
 * @see MappingEndEvent
 */
public final class MappingStartEvent extends CollectionStartEvent {

  public MappingStartEvent(Optional<Anchor> anchor, Optional<String> tag, boolean implicit,
      FlowStyle flowStyle, Optional<Mark> startMark, Optional<Mark> endMark) {
    super(anchor, tag, implicit, flowStyle, startMark, endMark);
  }

  public MappingStartEvent(Optional<Anchor> anchor, Optional<String> tag, boolean implicit,
      FlowStyle flowStyle) {
    this(anchor, tag, implicit, flowStyle, Optional.empty(), Optional.empty());
  }

  @Override
  public ID getEventId() {
    return ID.MappingStart;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("+MAP");
    if (getFlowStyle() == FlowStyle.FLOW) {
      builder.append(" {}");
    }
    builder.append(super.toString());
    return builder.toString();
  }
}
