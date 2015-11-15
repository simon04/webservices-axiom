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
package org.apache.axiom.datatype.xsd;

final class XSDateTypeImpl extends TemporalType<XSDate> implements XSDateType {
    @Override
    boolean hasDatePart() {
        return true;
    }

    @Override
    boolean hasTimePart() {
        return false;
    }

    @Override
    XSDate createInstance(boolean bc, String aeon, int year, int month, int day, int hour,
            int minute, int second, int nanoSecond, String nanoSecondFraction,
            SimpleTimeZone timeZone) {
        return new XSDateImpl(bc, aeon, year, month, day, timeZone);
    }
}
