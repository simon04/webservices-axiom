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
package org.apache.axiom.attachments;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.lifecycle.DataHandlerExt;
import org.apache.axiom.blob.Blobs;
import org.apache.axiom.blob.WritableBlob;
import org.apache.axiom.blob.WritableBlobFactory;
import org.apache.axiom.mime.ContentType;
import org.apache.axiom.mime.Header;
import org.apache.axiom.om.OMException;
import org.apache.axiom.util.UIDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.mime4j.stream.MimeTokenStream;
import org.apache.james.mime4j.stream.RecursionMode;

/**
 * {@link AttachmentsDelegate} implementation that represents a MIME multipart message read from a
 * stream.
 */
class MIMEMessage {
    private static final Log log = LogFactory.getLog(MIMEMessage.class);
    
    private static final WritableBlobFactory rootPartBlobFactory = new WritableBlobFactory() {
        public WritableBlob createBlob() {
            return Blobs.createMemoryBlob();
        }
    };

    /** <code>ContentType</code> of the MIME message */
    private final ContentType contentType;
    
    private final MimeTokenStream parser;
    
    /**
     * Stores the Data Handlers of the already parsed Mime Body Parts in the order that the attachments
     * occur in the message. This map is keyed using the content-ID's.
     */
    private final Map<String,DataHandler> attachmentsMap = new LinkedHashMap<String,DataHandler>();

    /**
     * The MIME part currently being processed.
     */
    private PartImpl currentPart;
    
    /** Container to hold streams for direct access */
    private IncomingAttachmentStreams streams;

    /** <code>boolean</code> Indicating if any streams have been directly requested */
    private boolean streamsRequested;

    /** <code>boolean</code> Indicating if any data handlers have been directly requested */
    private boolean partsRequested;

    private String firstPartId;

    private final WritableBlobFactory attachmentBlobFactory;
    
    MIMEMessage(InputStream inStream, String contentTypeString,
            WritableBlobFactory attachmentBlobFactory) throws OMException {
        this.attachmentBlobFactory = attachmentBlobFactory;
        try {
            contentType = new ContentType(contentTypeString);
        } catch (ParseException e) {
            throw new OMException(
                    "Invalid Content Type Field in the Mime Message"
                    , e);
        }

        MimeConfig config = new MimeConfig();
        config.setStrictParsing(true);
        parser = new MimeTokenStream(config);
        parser.setRecursionMode(RecursionMode.M_NO_RECURSE);
        parser.parseHeadless(inStream, contentTypeString);
        
        // Move the parser to the beginning of the first part
        while (parser.getState() != EntityState.T_START_BODYPART) {
            try {
                parser.next();
            } catch (IOException ex) {
                throw new OMException(ex);
            } catch (MimeException ex) {
                throw new OMException(ex);
            }
        }

        // Read the root part and cache it
        getDataHandler(getRootPartContentID());

        // Now reset partsRequested. The root part is a special case which is always 
        // read beforehand, regardless of request.
        partsRequested = false;
    }

    ContentType getContentType() {
        return contentType;
    }

    DataHandler getDataHandler(String contentID) {
        do {
            DataHandler dataHandler = attachmentsMap.get(contentID);
            if (dataHandler != null) {
                return dataHandler;
            }
        } while (getNextPartDataHandler() != null);
        return null;
    }

    InputStream getRootPartInputStream(boolean preserve) throws OMException {
        DataHandler dh;
        try {
            dh = getDataHandler(getRootPartContentID());
            if (dh == null) {
                throw new OMException(
                        "Mandatory root MIME part is missing");
            }
            if (!preserve && dh instanceof DataHandlerExt) {
                return ((DataHandlerExt)dh).readOnce();
            } else {
                return dh.getInputStream();
            }
        } catch (IOException e) {
            throw new OMException(
                    "Problem with DataHandler of the Root Mime Part. ", e);
        }
    }

    String getRootPartContentID() {
        String rootContentID = contentType.getParameter("start");
        if (log.isDebugEnabled()) {
            log.debug("getRootPartContentID rootContentID=" + rootContentID);
        }

        // to handle the Start parameter not mentioned situation
        if (rootContentID == null) {
            if (attachmentsMap.isEmpty()) {
                getNextPartDataHandler();
            }
            rootContentID = firstPartId;
        } else {
            rootContentID = rootContentID.trim();

            if ((rootContentID.indexOf("<") > -1)
                    & (rootContentID.indexOf(">") > -1)) {
                rootContentID = rootContentID.substring(1, (rootContentID
                        .length() - 1));
            }
        }
        // Strips off the "cid:" part from content-id
        if (rootContentID.length() > 4
                && "cid:".equalsIgnoreCase(rootContentID.substring(0, 4))) {
            rootContentID = rootContentID.substring(4);
        }
        return rootContentID;
    }
    
    String getRootPartContentType() {
        String rootPartContentID = getRootPartContentID();
        if (rootPartContentID == null) {
            throw new OMException("Unable to determine the content ID of the root part");
        }
        DataHandler rootPart = getDataHandler(rootPartContentID);
        if (rootPart == null) {
            throw new OMException("Unable to locate the root part; content ID was " + rootPartContentID);
        }
        return rootPart.getContentType();
    }
    
    IncomingAttachmentStreams getIncomingAttachmentStreams() {
        if (partsRequested) {
            throw new IllegalStateException(
                    "The attachments stream can only be accessed once; either by using the IncomingAttachmentStreams class or by getting a " +
                            "collection of AttachmentPart objects. They cannot both be called within the life time of the same service request.");
        }
        
        streamsRequested = true;
        
        if (this.streams == null) {
            this.streams = new MultipartAttachmentStreams(parser);
        }
        
        return this.streams;
    }

    /**
     * Force reading of all attachments.
     */
    void fetchAllParts() {
        while (getNextPartDataHandler() != null) {
            // Just loop until getNextPartDataHandler returns null
        }
    }

    Set<String> getContentIDs(boolean fetchAll) {
        if (fetchAll) {
            fetchAllParts();
        }
        return attachmentsMap.keySet();
    }
    
    Map<String,DataHandler> getMap() {
        fetchAllParts();
        return Collections.unmodifiableMap(attachmentsMap);
    }
    
    private DataHandler getNextPartDataHandler() throws OMException {
        if (streamsRequested) {
            throw new IllegalStateException("The attachments stream can only be accessed once; either by using the IncomingAttachmentStreams class or by getting a collection of AttachmentPart objects. They cannot both be called within the life time of the same service request.");
        }
        partsRequested = true;
        
        if (currentPart != null) {
            currentPart.fetch();
            currentPart = null;
        }
        if (parser.getState() == EntityState.T_END_MULTIPART) {
            return null;
        } else {
            boolean isRootPart = attachmentsMap.isEmpty();

            try {
                checkParserState(parser.next(), EntityState.T_START_HEADER);
                
                List<Header> headers = new ArrayList<Header>();
                while (parser.next() == EntityState.T_FIELD) {
                    Field field = parser.getField();
                    String name = field.getName();
                    String value = field.getBody();
                    
                    if (log.isDebugEnabled()){
                        log.debug("addHeader: (" + name + ") value=(" + value +")");
                    }
                    headers.add(new Header(name, value));
                }
                
                checkParserState(parser.next(), EntityState.T_BODY);
                
                currentPart = new PartImpl(isRootPart ? rootPartBlobFactory : attachmentBlobFactory, headers, parser);
            } catch (IOException ex) {
                throw new OMException(ex);
            } catch (MimeException ex) {
                throw new OMException(ex);
            }

            String partContentID = currentPart.getContentID();
            if (partContentID == null & attachmentsMap.isEmpty()) {
                String id = "firstPart_" + UIDGenerator.generateContentId();
                firstPartId = id;
                DataHandler dataHandler = currentPart.getDataHandler();
                attachmentsMap.put(id, dataHandler);
                return dataHandler;
            }
            if (partContentID == null) {
                throw new OMException(
                        "Part content ID cannot be blank for non root MIME parts");
            }
            if ((partContentID.indexOf("<") > -1)
                    & (partContentID.indexOf(">") > -1)) {
                partContentID = partContentID.substring(1, (partContentID
                        .length() - 1));

            }
            if (attachmentsMap.isEmpty()) {
                firstPartId = partContentID;
            }
            if (attachmentsMap.containsKey(partContentID)) {
                throw new OMException(
                        "Two MIME parts with the same Content-ID not allowed.");
            }
            DataHandler dataHandler = currentPart.getDataHandler();
            attachmentsMap.put(partContentID, dataHandler);
            return dataHandler;
        }
    }

    private static void checkParserState(EntityState state, EntityState expected) throws IllegalStateException {
        if (expected != state) {
            throw new IllegalStateException("Internal error: expected parser to be in state "
                    + expected + ", but got " + state);
        }
    }
}
