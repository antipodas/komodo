/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.vdb;

import static org.komodo.relational.commands.vdb.VdbCommandMessages.General.NAME_TYPE_DISPLAY;
import static org.komodo.relational.commands.vdb.VdbCommandMessages.ShowDataRolesCommand.DATA_ROLES_HEADER;
import static org.komodo.relational.commands.vdb.VdbCommandMessages.ShowDataRolesCommand.NO_DATA_ROLES;
import static org.komodo.relational.commands.vdb.VdbCommandMessages.ShowDataRolesCommand.SHOW_DATA_ROLES_ERROR;
import static org.komodo.shell.CompletionConstants.MESSAGE_INDENT;
import org.komodo.relational.vdb.DataRole;
import org.komodo.relational.vdb.Vdb;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.spi.repository.Repository.UnitOfWork;

/**
 * A shell command to show all data roles in a VDB.
 */
public final class ShowDataRolesCommand extends VdbShellCommand {

    static final String NAME = "show-data-roles"; //$NON-NLS-1$

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public ShowDataRolesCommand( final WorkspaceStatus status ) {
        super( NAME, status );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#doExecute()
     */
    @Override
    protected CommandResult doExecute() {
        CommandResult result = null;

        try {
            final UnitOfWork uow = getTransaction();
            final Vdb vdb = getVdb();
            final DataRole[] dataRoles = vdb.getDataRoles( uow );

            if ( dataRoles.length == 0 ) {
                result = new CommandResultImpl( getMessage( NO_DATA_ROLES, vdb.getName( uow ) ) );
            } else {
                print( MESSAGE_INDENT, getMessage( DATA_ROLES_HEADER, vdb.getName( uow ) ) );

                for ( final DataRole role : dataRoles ) {
                    print( MESSAGE_INDENT, getMessage( NAME_TYPE_DISPLAY, role.getName( uow ), role.getTypeDisplayName() ) );
                }

                result = CommandResult.SUCCESS;
            }
        } catch ( final Exception e ) {
            result = new CommandResultImpl( false, getMessage( SHOW_DATA_ROLES_ERROR ), e );
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#getMaxArgCount()
     */
    @Override
    protected int getMaxArgCount() {
        return 0;
    }

}
