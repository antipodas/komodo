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
package org.komodo.relational.commands.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.komodo.relational.commands.AbstractCommandTest;
import org.komodo.relational.model.Model;
import org.komodo.relational.model.View;
import org.komodo.relational.vdb.Vdb;
import org.komodo.relational.workspace.WorkspaceManager;
import org.komodo.shell.api.CommandResult;

/**
 * Test Class to test {@link DeleteViewCommand}.
 */
@SuppressWarnings( {"javadoc", "nls"} )
public final class DeleteViewCommandTest extends AbstractCommandTest {

    @Test
    public void testDelete1() throws Exception {
        final String[] commands = {
            "create-vdb myVdb vdbPath",
            "cd myVdb",
            "add-model myModel",
            "cd myModel",
            "add-view myView1",
            "add-view myView2",
            "delete-view myView1" };
        final CommandResult result = execute( commands );
        assertCommandResultOk(result);

        WorkspaceManager wkspMgr = WorkspaceManager.getInstance(_repo);
        Vdb[] vdbs = wkspMgr.findVdbs(getTransaction());

        assertEquals(1, vdbs.length);

        Model[] models = vdbs[0].getModels(getTransaction());
        assertEquals(1, models.length);
        assertEquals("myModel", models[0].getName(getTransaction())); //$NON-NLS-1$

        View[] views = models[0].getViews(getTransaction());
        assertEquals(1, views.length);
        assertEquals("myView2", views[0].getName(getTransaction())); //$NON-NLS-1$
    }

    @Test
    public void testTabCompleter()throws Exception{
    	ArrayList<CharSequence> candidates=new ArrayList<>();
    	setup("commandFiles","addViews.cmd");

    	candidates.add("myView1");
    	candidates.add("myView2");
    	assertTabCompletion("delete-view myV", candidates);

    	candidates.add("MyView3");
    	assertTabCompletion("delete-view ", candidates);
    }
}
