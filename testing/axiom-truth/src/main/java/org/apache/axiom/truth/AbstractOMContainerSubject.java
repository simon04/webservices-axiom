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
package org.apache.axiom.truth;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMNode;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;

public abstract class AbstractOMContainerSubject<S extends AbstractOMContainerSubject<S,T>,T extends OMContainer> extends Subject<S,T> {
    public AbstractOMContainerSubject(FailureMetadata failureMetadata, T subject) {
        super(failureMetadata, subject);
    }

    public final void hasNumberOfChildren(int expected) {
        OMNode child = actual().getFirstOMChild();
        int actual = 0;
        while (child != null) {
            actual++;
            child = child.getNextOMSibling();
        }
        if (actual != expected) {
            failWithRawMessage("number of children is %s instead of %s", actual, expected);
        }
    }
}
