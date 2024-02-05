package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class CommandSchedulerLog 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final ArrayList<String> currentlyRunningCommands = new ArrayList<String>();
    private final DataLog log;
    private final StringLogEntry commandLogEntry;
    private boolean useConsole = false;
    private boolean useDataLog = false;
    private boolean useShuffleBoardLog = false;

    /**
     * Command Event Loggers
     * <p>Set the scheduler to log events for command initialize, interrupt, finish, and execute.
     * Log to the ShuffleBoard and the WPILib data log tool.
     * If ShuffleBoard is recording, these events are added to the recording.
     * Convert recording to csv and they show nicely in Excel.
     * If using data log tool, the recording is automatic so run that tool to retrieve and convert the log.
     */ 
    CommandSchedulerLog(boolean useConsole, boolean useDataLog, boolean useShuffleBoardLog)
    {
        this.useConsole = useConsole;
        this.useDataLog = useDataLog;
        this.useShuffleBoardLog = useShuffleBoardLog;

        log = DataLogManager.getLog();
        commandLogEntry = new StringLogEntry(log, "/Commands/events", "Event");
    }

    /**
     * Log commands that run the initialize method.
     */
    public void logCommandInitialize()
    {
        CommandScheduler.getInstance().onCommandInitialize(
            (command) ->
            {
                if(useConsole)
                    System.out.println("Command initialized " + command.getName());
                if(useDataLog)
                    commandLogEntry.append(command.getClass() + " " + command.getName() + " initialized");  
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command initialized", command.getName(), EventImportance.kNormal);
            }
        );
    }

    /**
     * Log commands that have been interrupted.
     */
    public void logCommandInterrupt()
    {
        CommandScheduler.getInstance().onCommandInterrupt(
            (command) ->
            {
                currentlyRunningCommands.remove(command.getName());
                if(useConsole)
                    System.out.println("Command interrupted " + command.getName());
                if(useDataLog) 
                    commandLogEntry.append(command.getClass() + " " + command.getName() + " interrupted");
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command interrupted", command.getName(), EventImportance.kNormal);
            }
        );
    }

    /**
     * Log commands that run the finish method.
     */
    public void logCommandFinish()
    {
        CommandScheduler.getInstance().onCommandFinish(
            (command) ->
            {
                currentlyRunningCommands.remove(command.getName());
                if(useConsole)
                    System.out.println("Command finished " + command.getName());
                if(useDataLog) 
                    commandLogEntry.append(command.getClass() + " " + command.getName() + " finished");
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command finished", command.getName(), EventImportance.kNormal);
            }
        );
    }

    /**
     * Log commands that run the execute method. This can generate a lot of events.
     */
    public void logCommandExecute()
    {
        CommandScheduler.getInstance().onCommandExecute(
            (command) ->
            {
                if(currentlyRunningCommands.indexOf(command.getClass() + "/" + command.getName()) < 0)
                {
                    currentlyRunningCommands.add(command.getClass() + "/" + command.getName());
                    if(useConsole)
                        System.out.println("Command executed " + command.getName());
                    if(useDataLog) 
                        commandLogEntry.append(command.getClass() + " " + command.getName() + " executed");
                    if(useShuffleBoardLog)
                        Shuffleboard.addEventMarker("Command executed", command.getName(), EventImportance.kNormal);
                }
            }
        );
    }
}
