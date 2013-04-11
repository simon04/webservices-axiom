/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axiom.ts.dimension.serialization;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.testutils.suite.MatrixTestCase;

/**
 * Serializes an {@link OMContainer} using {@link OMContainer#serialize(OutputStream)} or
 * {@link OMContainer#serializeAndConsume(OutputStream)}.
 */
public class SerializeToOutputStream implements SerializationStrategy {
    private final boolean cache;
    
    public SerializeToOutputStream(boolean cache) {
        this.cache = cache;
    }

    public void addTestParameters(MatrixTestCase testCase) {
        testCase.addTestParameter("serializationStrategy", "OutputStream");
        testCase.addTestParameter("cache", String.valueOf(cache));
    }

    public XML serialize(OMContainer container) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (cache) {
            container.serialize(baos);
        } else {
            container.serializeAndConsume(baos);
        }
        return new XMLAsByteArray(baos.toByteArray());
    }

    public boolean isPush() {
        return true;
    }

    public boolean isCaching() {
        return cache;
    }

    public boolean supportsInternalSubset() {
        return true;
    }
}