/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.vdb;

import java.util.List;
import org.komodo.relational.commands.workspace.WorkspaceCommandsI18n;
import org.komodo.relational.vdb.Vdb;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.Arguments;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.shell.commands.SetPropertyCommand;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.StringUtils;
import org.komodo.utils.i18n.I18n;

/**
 * A shell command to set VDB properties
 */
public final class SetVdbPropertyCommand extends VdbShellCommand {

    static final String NAME = SetPropertyCommand.NAME;

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public SetVdbPropertyCommand( final WorkspaceStatus status ) {
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

            final Vdb vdb = getVdb();
            final UnitOfWork transaction = getTransaction();
            String errorMsg = null;

            switch ( name ) {
                case ALLOWED_LANGUAGES:
                    vdb.setAllowedLanguages( transaction, value );
                    break;
                case AUTHENTICATION_TYPE:
                    vdb.setAuthenticationType( transaction, value );
                    break;
                case CONNECTION_TYPE:
                    vdb.setConnectionType( transaction, value );
                    break;
                case DESCRIPTION:
                    vdb.setDescription( transaction, value );
                    break;
                case GSS_PATTERN:
                    vdb.setGssPattern( transaction, value );
                    break;
                case ORIGINAL_FILE_PATH:
                    vdb.setOriginalFilePath( transaction, value );
                    break;
                case PASSWORD_PATTERN:
                    vdb.setPasswordPattern( transaction, value );
                    break;
                case PREVIEW:
                    if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                        vdb.setPreview( transaction, Boolean.parseBoolean( value ) );
                    } else {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, PREVIEW );
                    }

                    break;
                case QUERY_TIMEOUT:
                    try {
                        final int timeout = Integer.parseInt( value );
                        vdb.setQueryTimeout( transaction, timeout );
                    } catch ( final NumberFormatException e ) {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidIntegerPropertyValue, QUERY_TIMEOUT );
                    }

                    break;
                case SECURITY_DOMAIN:
                    vdb.setSecurityDomain( transaction, value );
                    break;
                case VERSION:
                    try {
                        final int version = Integer.parseInt( value );
                        vdb.setVersion( transaction, version );
                    } catch ( final NumberFormatException e ) {
                        errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidIntegerPropertyValue, VERSION );
                    }

                    break;
                default:
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidPropertyName, name, Vdb.class.getSimpleName() );
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
        print( indent, I18n.bind( VdbCommandsI18n.setVdbPropertyHelp, getName() ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpExamples(int)
     */
    @Override
    protected void printHelpExamples( final int indent ) {
        print( indent, I18n.bind( VdbCommandsI18n.setVdbPropertyExamples ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpUsage(int)
     */
    @Override
    protected void printHelpUsage( final int indent ) {
        print( indent, I18n.bind( VdbCommandsI18n.setVdbPropertyUsage ) );
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

        if ( ( args.size() == 1 ) && PREVIEW.equals( getArguments().get( 0 ) ) ) {
            updateCandidatesForBooleanProperty( lastArgument, candidates );
            return ( candidates.isEmpty() ? -1 : ( StringUtils.isBlank( lastArgument ) ? 0 : ( toString().length() + 1 ) ) );
        }

        // no tab completion
        return -1;
    }

}
