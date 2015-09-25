package org.komodo.shell.commands;

import org.komodo.shell.BuiltInShellCommand;
import org.komodo.shell.CommandResultImpl;
import org.komodo.shell.Messages;
import org.komodo.shell.Messages.SHELL;
import org.komodo.shell.ShellCommandFactory;
import org.komodo.shell.ShellCommandReader;
import org.komodo.shell.ShellCommandReaderFactory;
import org.komodo.shell.api.CommandResult;
import org.komodo.shell.api.ShellCommand;
import org.komodo.shell.api.WorkspaceStatus;

/**
 * This command executes (plays) a saved komodo CLI script on your local file system. It executes all commands as one transaction.
 */
public class PlayCommand  extends BuiltInShellCommand {

    /**
     * The command name.
     */
    public static final String NAME = "play"; //$NON-NLS-1$

    /**
     * @param wsStatus
     *        the workspace status (cannot be <code>null</code>)
     */
    public PlayCommand( final WorkspaceStatus wsStatus ) {
        super( wsStatus, NAME );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.api.ShellCommand#isValidForCurrentContext()
     */
    @Override
    public boolean isValidForCurrentContext() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#doExecute()
     */
    @Override
    protected CommandResult doExecute() {
        try {
            String fileNameArg = requiredArgument(0, Messages.getString(Messages.PlayCommand.InvalidArgMsg_FileName));

            // Validate the supplied path
            boolean validFile = validateReadableFileArg(fileNameArg);
            if(!validFile) {
                return new CommandResultImpl( false,
                                              Messages.getString( Messages.SHELL.FileNotAccessible, fileNameArg ),
                                              null );
            }

            try {
                final CommandResult result = playFile( fileNameArg );

                if ( result.isOk() ) {
                    return new CommandResultImpl( Messages.getString( Messages.PlayCommand.fileExecuted, fileNameArg ) );
                }

                return new CommandResultImpl( false,
                                              Messages.getString( Messages.PlayCommand.Failure, fileNameArg ),
                                              result.getError() );
            } catch (Exception e) {
                return new CommandResultImpl( false, Messages.getString( Messages.PlayCommand.Failure, fileNameArg ), e );
            }
        } catch ( final Exception e ) {
            return new CommandResultImpl( false, Messages.getString( SHELL.CommandFailure, NAME ), e );
        }
	}

    /**
     * {@inheritDoc}
     *
     * @see org.komodo.shell.BuiltInShellCommand#getMaxArgCount()
     */
    @Override
    protected int getMaxArgCount() {
        return 1;
    }

	private CommandResult playFile(String commandFile ) throws Exception {
		WorkspaceStatus wsStatus = getWorkspaceStatus();
		String[] args = new String[]{"-f", commandFile}; //$NON-NLS-1$

		ShellCommandFactory factory = new ShellCommandFactory();
        factory.registerCommands(wsStatus);
		ShellCommandReader reader = ShellCommandReaderFactory.createCommandReader(args, factory, wsStatus);
        reader.open();

        return runCommands(reader);
	}

	private CommandResult runCommands(ShellCommandReader reader) throws Exception {
        while ( true ) {
			final ShellCommand command = reader.read();

            if ( ( command == null ) || ( command instanceof ExitCommand ) ) {
                break;
            }

            command.setWriter( getWriter() );
            final CommandResult result = command.execute();

            if ( !result.isOk() ) {
                return result;
            }
		}

        return CommandResult.SUCCESS;
	}

}
