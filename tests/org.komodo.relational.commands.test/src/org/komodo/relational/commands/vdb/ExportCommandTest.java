/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.relational.commands.vdb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;
import org.komodo.relational.commands.AbstractCommandTest;
import org.komodo.shell.api.CommandResult;
import org.komodo.spi.KException;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.test.utils.TestUtilities;
import org.w3c.dom.Document;

/**
 * Test Class to test VDB ExportCommand
 *
 */
@SuppressWarnings( {"javadoc", "nls"} )
public class ExportCommandTest extends AbstractCommandTest {

    private static final String TWEET_VDB = "./resources/tweet-example-vdb.xml";  //$NON-NLS-1$
    private static final String ALL_ELEMENTS_VDB = "./resources/teiid-vdb-all-elements.xml";  //$NON-NLS-1$

    private KomodoObject addTweetVdbExample() throws KException, Exception {
        KomodoObject kWorkspace = _repo.komodoWorkspace(getTransaction());
        KomodoObject tweet = TestUtilities.createTweetExampleNode(getTransaction(), kWorkspace);

        assertNotNull(tweet);

        traverse(getTransaction(), tweet.getAbsolutePath());

        return tweet;
    }

    private KomodoObject addAllElementsVdbExample() throws Exception {
        KomodoObject kWorkspace = _repo.komodoWorkspace(getTransaction());
        KomodoObject tweet = TestUtilities.createAllElementsExampleNode(getTransaction(), kWorkspace);

        assertNotNull(tweet);

        traverse(getTransaction(), tweet.getAbsolutePath());

        return tweet;
    }

    @Test
    public void testExportCommandTweetVdb() throws Exception {
        //
        // Create the vdb in the repository
        //
        KomodoObject tweetVdb = addTweetVdbExample();

        //
        // Create the export command instructions file
        //
        File exportCmdFile = File.createTempFile("TestExportCommand", ".txt"); //$NON-NLS-1$  //$NON-NLS-2$
        exportCmdFile.deleteOnExit();

        //
        // Create the export destination file, ensuring it does not already exist
        //
        File exportDest = new File(System.getProperty("java.io.tmpdir") + File.separator + "TestExportDestination.txt"); //$NON-NLS-1$  //$NON-NLS-2$
        exportDest.deleteOnExit();
        if (exportDest.exists())
            exportDest.delete();

        // The test commands
        final String[] commands = { 
            "commit",
            "workspace",
            "cd " + tweetVdb.getName(getTransaction()),
            "export-vdb " + exportDest.getAbsolutePath() };

        setup( commands );

        //
        // Execute the commands
        //
        execute();

        assertTrue(exportDest.exists());

        Document vdbDocument = TestUtilities.createDocument(new FileInputStream(exportDest));
        assertNotNull(vdbDocument);

        InputStream tweetExample = new FileInputStream(new File(TWEET_VDB));
        Document tweetDocument = TestUtilities.createDocument(tweetExample);

        TestUtilities.compareDocuments(tweetDocument, vdbDocument);
    }

    @Test
    public void testExportCommandAllElementsVdb() throws Exception {
        //
        // Create the vdb in the repository
        //
        KomodoObject allElementsVdb = addAllElementsVdbExample();

        //
        // Create the export command instructions file
        //
        File exportCmdFile = File.createTempFile("TestExportCommand", ".txt"); //$NON-NLS-1$  //$NON-NLS-2$
        exportCmdFile.deleteOnExit();

        //
        // Create the export destination file, ensuring it does not already exist
        //
        File exportDest = new File(System.getProperty("java.io.tmpdir") + File.separator + "TestExportDestination.txt"); //$NON-NLS-1$  //$NON-NLS-2$
        exportDest.deleteOnExit();
        if (exportDest.exists())
            exportDest.delete();

        // The test commands
        final String[] commands = { 
            "commit",
            "workspace",
            "cd " + allElementsVdb.getName(getTransaction()),
            "export-vdb " + exportDest.getAbsolutePath() };

        setup( commands );

        //
        // Execute the commands
        //
        CommandResult result = execute();
        assertCommandResultOk(result);

        assertTrue(exportDest.exists());

        Document vdbDocument = TestUtilities.createDocument(new FileInputStream(exportDest));
        assertNotNull(vdbDocument);

        InputStream allElementsExample = new FileInputStream(new File(ALL_ELEMENTS_VDB));
        Document allElementsDocument = TestUtilities.createDocument(allElementsExample);

        TestUtilities.compareDocuments(allElementsDocument, vdbDocument);
    }

}