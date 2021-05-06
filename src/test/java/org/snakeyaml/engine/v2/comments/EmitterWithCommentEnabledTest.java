/*
 * Copyright (c) 2018, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snakeyaml.engine.v2.comments;


import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.StreamDataWriter;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.composer.Composer;
import org.snakeyaml.engine.v2.emitter.Emitter;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.parser.ParserImpl;
import org.snakeyaml.engine.v2.scanner.StreamReader;
import org.snakeyaml.engine.v2.serializer.Serializer;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmitterWithCommentEnabledTest {

    private String runEmitterWithCommentsEnabled(String data) throws IOException {
        StreamDataWriter output = new MyWriter();

        DumpSettings dumpSettings = DumpSettings.builder()
                .setDefaultScalarStyle(ScalarStyle.PLAIN)
                .setDefaultFlowStyle(FlowStyle.BLOCK)
                .build();
        Serializer serializer = new Serializer(dumpSettings, new Emitter(dumpSettings, output));

        serializer.open();
        LoadSettings loadSettings = LoadSettings.builder().setParseComments(true).build();
        Composer composer = new Composer(loadSettings, new ParserImpl(loadSettings, new StreamReader(loadSettings, data)));
        while (composer.hasNext()) {
            Node node = composer.next();
            //System.out.println(node);
            serializer.serialize(node);
        }
        serializer.close();

        return output.toString();
    }

    @Test
    public void testEmpty() throws Exception {
        String data = "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testWithOnlyComment() throws Exception {
        String data = "# Comment\n\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testCommentEndingALine() throws Exception {
        String data = "" + //
                "key: # Comment\n" + //
                "  value\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testMultiLineComment() throws Exception {
        String data = "" + //
                "key: # Comment\n" + //
                "     # lines\n" + //
                "  value\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testBlankLine() throws Exception {
        String data = "" + //
                "\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testBlankLineComments() throws Exception {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testBlockScalar() throws Exception {
        String data = "" + //
                "abc: | # Comment\n" + //
                "  def\n" + //
                "  hij\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testDirectiveLineEndComment() throws Exception {
        String data = "%YAML 1.1 #Comment\n";

        String result = runEmitterWithCommentsEnabled(data);

        // We currently strip Directive comments
        assertEquals("", result);
    }

    @Test
    public void testSequence() throws Exception {
        String data = "" + //
                "# Comment\n" + //
                "list: # InlineComment1\n" + //
                "  # Block Comment\n" + //
                "  - item # InlineComment2\n" + //
                "# Comment\n";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testAllComments1() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "key: # Inline Comment1a\n" + //
                "     # Inline Comment1b\n" + //
                "  # Block Comment3a\n" + //
                "  # Block Comment3b\n" + //
                "  value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "list: # InlineComment3a\n" + //
                "      # InlineComment3b\n" + //
                "  # Block Comment5\n" + //
                "  - item1 # InlineComment4\n" + //
                "  - item2: [value2a, value2b] # InlineComment5\n" + //
                "  - item3: {key3a: [value3a1, value3a2], key3b: value3b} # InlineComment6\n" + //
                "# Block Comment6\n" + //
                "---\n" + //
                "# Block Comment7\n" + //
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testMultiDoc() throws Exception {
        String data = "" + //
                "key: value\n" + //
                "# Block Comment\n" + //
                "---\n" + //
                "# Block Comment\n" + //
                "key: value\n" + //
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testAllComments2() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "- item1 # Inline Comment1a\n" + //
                "        # Inline Comment1b\n" + //
                "# Block Comment3a\n" + //
                "# Block Comment3b\n" + //
                "- item2: value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testAllComments3() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "[item1, {item2: value2}, {item3: value3}] # Inline Comment1\n" + //
                "# Block Comment2\n" + //
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testKeepingNewLineInsideSequence() throws Exception {
        String data = "" +
                "\n" +
                "key:\n" +
                "  \n" +
                "  - item1\n" +
                //"\n" + // Per Spec this is part of plain scalar above
                "  - item2\n" +
                //"\n" + // Per Spec this is part of plain scalar above
                "  - item3\n" +
                "\n" +
                "key2: value2\n" +
                "\n" +
                "key3: value3\n" +
                "\n" +
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    @Test
    public void testKeepingNewLineInsideSequence2() throws Exception {
        String data = "" +
                "apiVersion: kustomize.config.k8s.io/v1beta1\n" +
                "kind: Kustomization\n" +
                "\n" +
                "namePrefix: acquisition-gateway-\n" +
                "\n" +
                "bases:\n" +
                "  \n" +
                "#- https://github.intuit.com/dev-patterns/intuit-kustomize/intuit-service-appd-noingress-base?ref=v3.1.2\n" +
                "# Add the following base and HPA-patch.yaml, fill in correct minReplicas and maxReplcias in Hpa-patch.yaml\n" +
                "#- https://github.intuit.com/dev-patterns/intuit-kustomize//intuit-service-hpa-base?ref=v3.1.2\n" +
                "  - https://github.intuit.com/dev-patterns/intuit-kustomize//intuit-service-canary-appd-noingress-base?ref=v3.2.0\n" +
                "  - https://github.intuit.com/dev-patterns/intuit-kustomize//intuit-service-rollout-hpa-base?ref=v3.2.0\n" +
                "# resources:\n" +
                "# - Nginx-ConfigMap.yaml\n" +
                "\n" +
                "resources:\n" +
                "- ConfigMap-v1-splunk-sidecar-config.yaml\n" +
                "- CronJob-patch.yaml\n" +
                "\n" +
                "patchesStrategicMerge:\n" +
                "- app-rollout-patch.yaml\n" +
                "- Service-patch.yaml\n" +
                "- Service-metrics-patch.yaml\n" +
                // "\n" +
                "- Hpa-patch.yaml\n" +
                "#- SignalSciences-patch.yaml\n" +
                "\n" +
                "# Uncomment HPA-patch when you need to enable HPA\n" +
                "#- Hpa-patch.yaml\n" +
                "# Uncomment SignalSciences-patch when you need to enable Signal Sciences\n" +
                "#- SignalSciences-patch.yaml\n" +
                "";

        String result = runEmitterWithCommentsEnabled(data);

        assertEquals(data, result);
    }

    class MyWriter extends StringWriter implements StreamDataWriter {
    }
}

