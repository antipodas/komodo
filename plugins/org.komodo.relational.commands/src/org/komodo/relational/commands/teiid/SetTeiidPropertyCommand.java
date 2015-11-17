/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.teiid;

import java.util.List;
import org.komodo.relational.commands.mask.MaskCommandsI18n;
import org.komodo.relational.commands.workspace.WorkspaceCommandsI18n;
import org.komodo.relational.teiid.Teiid;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.Arguments;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.shell.commands.SetPropertyCommand;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.StringUtils;
import org.komodo.utils.i18n.I18n;

/**
 * A shell command to set Teiid properties
 */
public final class SetTeiidPropertyCommand extends TeiidShellCommand {

    static final String NAME = SetPropertyCommand.NAME;

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public SetTeiidPropertyCommand( final WorkspaceStatus status ) {
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
            final String name = requiredArgument( 0, I18n.bind( WorkspaceCommandsI18n.missingPropertyNameValue ) );
            final String value = requiredArgument( 1, I18n.bind( WorkspaceCommandsI18n.missingPropertyNameValue ) );

            final Teiid teiid = getTeiid();
            final UnitOfWork transaction = getTransaction();
            String errorMsg = null;

            switch ( name ) {
                case ADMIN_PORT:
                    try {
                        final int port = Integer.parseInt( value );
                        teiid.setAdminPort( transaction, port );
                    } catch ( final NumberFormatException e ) {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidIntegerPropertyValue, ADMIN_PORT );
                    }

                    break;
                case ADMIN_PASSWORD:
                    teiid.setAdminPassword( transaction, value );
                    break;
                case ADMIN_SECURE:
                    if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                        teiid.setAdminSecure( transaction, Boolean.parseBoolean( value ) );
                    } else {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, ADMIN_SECURE );
                    }

                    break;
                case ADMIN_USER:
                    teiid.setAdminUser( transaction, value );
                    break;
                case JDBC_PORT:
                    try {
                        final int port = Integer.parseInt( value );
                        teiid.setJdbcPort( transaction, port );
                    } catch ( final NumberFormatException e ) {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidIntegerPropertyValue, JDBC_PORT );
                    }

                    break;
                case JDBC_PASSWORD:
                    teiid.setJdbcPassword( transaction, value );
                    break;
                case JDBC_SECURE:
                    if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                        teiid.setJdbcSecure( transaction, Boolean.parseBoolean( value ) );
                    } else {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, JDBC_SECURE );
                    }

                    break;
                case JDBC_USER:
                    teiid.setJdbcUsername( transaction, value );
                    break;
                case HOST:
                    teiid.setHost( transaction, value );
                    break;
                default:
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidPropertyName, name, Teiid.class.getSimpleName() );
                    break;
            }

            if ( StringUtils.isBlank( errorMsg ) ) {
                result = new CommandResultImpl( I18n.bind( WorkspaceCommandsI18n.setPropertySuccess, name ) );
            } else {
                result = new CommandResultImpl( false, errorMsg, null );
            }
        } catch ( final Exception e ) {
            result = new CommandResultImpl( e );
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
        return 2;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpDescription(int)
     */
    @Override
    protected void printHelpDescription( final int indent ) {
        print( indent, I18n.bind( TeiidCommandsI18n.setTeiidPropertyHelp, getName() ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpExamples(int)
     */
    @Override
    protected void printHelpExamples( final int indent ) {
        print( indent, I18n.bind( TeiidCommandsI18n.setTeiidPropertyExamples ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpUsage(int)
     */
    @Override
    protected void printHelpUsage( final int indent ) {
        print( indent, I18n.bind( MaskCommandsI18n.setMaskPropertyUsage ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#tabCompletion(java.lang.String, java.util.List)
     */
    @Override
    public int tabCompletion( final String lastArgument,
                              final List< CharSequence > candidates ) throws Exception {
        final Arguments args = getArguments();

        if ( args.isEmpty() ) {
            if ( lastArgument == null ) {
                candidates.addAll( ALL_PROPS );
            } else {
                for ( final String item : ALL_PROPS ) {
                    if ( item.toUpperCase().startsWith( lastArgument.toUpperCase() ) ) {
                        candidates.add( item );
                    }
                }
            }

            return 0;
        }

        if ( (args.size() == 1) && ( ADMIN_SECURE.equals(getArguments().get(0)) || JDBC_SECURE.equals(getArguments().get(0)) ) ) {
            updateCandidatesForBooleanProperty( lastArgument, candidates );
            return ( candidates.isEmpty() ? -1 : ( StringUtils.isBlank( lastArgument ) ? 0 : ( toString().length() + 1 ) ) );
        }

        // no tab completion
        return -1;
    }

}
