/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.komodo.relational.commands.permission;

import static org.komodo.shell.CompletionConstants.MESSAGE_INDENT;

import org.komodo.relational.commands.workspace.WorkspaceCommandsI18n;
import org.komodo.relational.vdb.Mask;
import org.komodo.relational.vdb.Permission;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.utils.i18n.I18n;

/**
 * A shell command to show all the {@link Mask masks} of a {@link Permission}.
 */
public final class ShowMasksCommand extends PermissionShellCommand {

    static final String NAME = "show-masks"; //$NON-NLS-1$

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public ShowMasksCommand( final WorkspaceStatus status ) {
        super( NAME, status );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#doExecute()
     */
    @Override
    protected CommandResult doExecute() {
        try {
            final String[] namePatterns = processOptionalArguments( 0 );
            final boolean hasPatterns = ( namePatterns.length != 0 );
            final Permission permission = getPermission();
            final Mask[] masks = permission.getMasks( getTransaction(), namePatterns );

            if ( masks.length == 0 ) {
                if ( hasPatterns ) {
                    print( MESSAGE_INDENT,
                           I18n.bind( PermissionCommandsI18n.noMatchedMasks, permission.getName( getTransaction() ) ) );
                } else {
                    print( MESSAGE_INDENT, I18n.bind( PermissionCommandsI18n.noMasks, permission.getName( getTransaction() ) ) );
                }
            } else {
                if ( hasPatterns ) {
                    print( MESSAGE_INDENT,
                           I18n.bind( PermissionCommandsI18n.matchedMasksHeader, permission.getName( getTransaction() ) ) );
                } else {
                    print( MESSAGE_INDENT,
                           I18n.bind( PermissionCommandsI18n.masksHeader, permission.getName( getTransaction() ) ) );
                }

                final int indent = (MESSAGE_INDENT * 2);

                for ( final Mask mask : masks ) {
                    print( indent,
                           I18n.bind( WorkspaceCommandsI18n.printRelationalObject,
                                                mask.getName( getTransaction() ),
                                                getWorkspaceStatus().getTypeDisplay(mask, null)) );
                }
            }

            return CommandResult.SUCCESS;
        } catch ( final Exception e ) {
            return new CommandResultImpl( e );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#getMaxArgCount()
     */
    @Override
    protected int getMaxArgCount() {
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpDescription(int)
     */
    @Override
    protected void printHelpDescription( final int indent ) {
        print( indent, I18n.bind( PermissionCommandsI18n.showMasksHelp, getName() ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpExamples(int)
     */
    @Override
    protected void printHelpExamples( final int indent ) {
        print( indent, I18n.bind( PermissionCommandsI18n.showMasksExamples ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpUsage(int)
     */
    @Override
    protected void printHelpUsage( final int indent ) {
        print( indent, I18n.bind( PermissionCommandsI18n.showMasksUsage ) );
    }

}
