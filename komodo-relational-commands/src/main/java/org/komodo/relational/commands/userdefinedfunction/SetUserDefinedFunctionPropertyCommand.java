/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.komodo.relational.commands.userdefinedfunction;

import java.util.List;
import org.komodo.relational.commands.workspace.WorkspaceCommandsI18n;
import org.komodo.relational.model.Function;
import org.komodo.relational.model.SchemaElement;
import org.komodo.relational.model.UserDefinedFunction;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.api.Arguments;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.TabCompletionModifier;
import org.komodo.shell.api.WorkspaceStatus;
import org.komodo.shell.commands.SetPropertyCommand;
import org.komodo.spi.repository.Repository.UnitOfWork;
import org.komodo.utils.StringUtils;
import org.komodo.utils.i18n.I18n;

/**
 * A shell command to set UserDefinedFunction properties
 */
public final class SetUserDefinedFunctionPropertyCommand extends UserDefinedFunctionShellCommand {

    static final String NAME = SetPropertyCommand.NAME;

    /**
     * @param status
     *        the shell's workspace status (cannot be <code>null</code>)
     */
    public SetUserDefinedFunctionPropertyCommand( final WorkspaceStatus status ) {
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

            final UserDefinedFunction func = getUserDefinedFunction();
            final UnitOfWork transaction = getTransaction();
            String errorMsg = null;

            if ( AGGREGATE.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setAggregate( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, AGGREGATE );
                }
            } else if ( ALLOWS_DISTINCT.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setAllowsDistinct( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, ALLOWS_DISTINCT );
                }
            } else if ( ALLOWS_ORDERBY.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setAllowsOrderBy( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, ALLOWS_ORDERBY );
                }
            } else if ( ANALYTIC.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setAnalytic( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, ANALYTIC );
                }
            } else if ( CATEGORY.equals( name ) ) {
                func.setCategory( getTransaction(), value );
            } else if ( DECOMPOSABLE.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setDecomposable( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, DECOMPOSABLE );
                }
            } else if ( DESCRIPTION.equals( name ) ) {
                func.setDescription( getTransaction(), value );
            } else if ( DETERMINISM.equals( name ) ) {
                if ( Function.Determinism.COMMAND_DETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.COMMAND_DETERMINISTIC );
                } else if ( Function.Determinism.DETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.DETERMINISTIC );
                } else if ( Function.Determinism.NONDETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.NONDETERMINISTIC );
                } else if ( Function.Determinism.SESSION_DETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.SESSION_DETERMINISTIC );
                } else if ( Function.Determinism.USER_DETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.USER_DETERMINISTIC );
                } else if ( Function.Determinism.VDB_DETERMINISTIC.name().equals( value ) ) {
                    func.setDeterminism( transaction, Function.Determinism.VDB_DETERMINISTIC );
                } else {
                    errorMsg = I18n.bind( UserDefinedFunctionCommandsI18n.invalidDeterministicPropertyValue, DETERMINISM );
                }
            } else if ( JAVA_CLASS.equals( name ) ) {
                func.setJavaClass( getTransaction(), value );
            } else if ( JAVA_METHOD.equals( name ) ) {
                func.setJavaMethod( getTransaction(), value );
            } else if ( NAME_IN_SOURCE.equals( name ) ) {
                func.setNameInSource( getTransaction(), value );
            } else if ( NULL_ON_NULL.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setNullOnNull( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, NULL_ON_NULL );
                }
            } else if ( SCHEMA_ELEMENT_TYPE.equals( name ) ) {
                if ( SchemaElement.SchemaElementType.FOREIGN.name().equals( value ) ) {
                    func.setSchemaElementType( transaction, SchemaElement.SchemaElementType.FOREIGN );
                } else if ( SchemaElement.SchemaElementType.VIRTUAL.name().equals( value ) ) {
                    func.setSchemaElementType( transaction, SchemaElement.SchemaElementType.VIRTUAL );
                } else {
                    errorMsg = I18n.bind( UserDefinedFunctionCommandsI18n.invalidSchemaElementTypePropertyValue,
                                          SCHEMA_ELEMENT_TYPE );
                }
            } else if ( UPDATE_COUNT.equals( name ) ) {
                try {
                    final long count = Long.parseLong( value );
                    func.setUpdateCount( transaction, count );
                } catch ( final NumberFormatException e ) {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidIntegerPropertyValue, UPDATE_COUNT );
                }
            } else if ( USES_DISTINCT_ROWS.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setUsesDistinctRows( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, USES_DISTINCT_ROWS );
                }
            } else if ( UUID.equals( name ) ) {
                func.setUuid( getTransaction(), value );
            } else if ( VAR_ARGS.equals( name ) ) {
                if ( Boolean.TRUE.toString().equals( value ) || Boolean.FALSE.toString().equals( value ) ) {
                    func.setVarArgs( transaction, Boolean.parseBoolean( value ) );
                } else {
                    errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidBooleanPropertyValue, VAR_ARGS );
                }
            } else {
                errorMsg = I18n.bind( WorkspaceCommandsI18n.invalidPropertyName,
                                      name,
                                      UserDefinedFunction.class.getSimpleName() );
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
        print( indent, I18n.bind( UserDefinedFunctionCommandsI18n.setUserDefinedFunctionPropertyHelp, getName() ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpExamples(int)
     */
    @Override
    protected void printHelpExamples( final int indent ) {
        print( indent, I18n.bind( UserDefinedFunctionCommandsI18n.setUserDefinedFunctionPropertyExamples ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#printHelpUsage(int)
     */
    @Override
    protected void printHelpUsage( final int indent ) {
        print( indent, I18n.bind( UserDefinedFunctionCommandsI18n.setUserDefinedFunctionPropertyUsage ) );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#tabCompletion(java.lang.String, java.util.List)
     */
    @Override
    public TabCompletionModifier tabCompletion( final String lastArgument,
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
        }

        if ( ( args.size() == 1 ) ) {
            String theArg = getArguments().get(0);
            if( AGGREGATE.equals(theArg) || ALLOWS_DISTINCT.equals(theArg) || ALLOWS_ORDERBY.equals(theArg) || ANALYTIC.equals(theArg)
                || DECOMPOSABLE.equals(theArg) || NULL_ON_NULL.equals(theArg) || USES_DISTINCT_ROWS.equals(theArg) || VAR_ARGS.equals(theArg)) {
                updateCandidatesForBooleanProperty( lastArgument, candidates );
            } else if( DETERMINISM.equals(theArg) ) {
                candidates.add( Function.Determinism.COMMAND_DETERMINISTIC.name() );
                candidates.add( Function.Determinism.DETERMINISTIC.name() );
                candidates.add( Function.Determinism.NONDETERMINISTIC.name() );
                candidates.add( Function.Determinism.SESSION_DETERMINISTIC.name() );
                candidates.add( Function.Determinism.USER_DETERMINISTIC.name() );
                candidates.add( Function.Determinism.VDB_DETERMINISTIC.name() );
            } else if( SCHEMA_ELEMENT_TYPE.equals(theArg)) {
                candidates.add( SchemaElement.SchemaElementType.FOREIGN.name() );
                candidates.add( SchemaElement.SchemaElementType.VIRTUAL.name() );
            }
        }
        return TabCompletionModifier.AUTO;
    }
}
