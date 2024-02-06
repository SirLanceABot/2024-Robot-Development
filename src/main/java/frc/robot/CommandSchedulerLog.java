package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

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

    private final HashMap<String, Integer> currentCommands = new HashMap<String, Integer>();
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
                String key = command.getClass().getSimpleName() + "/" + command.getName();

                if(useConsole)
                    System.out.println("Command initialized : " + key);
                if(useDataLog)
                    commandLogEntry.append("Command initialized : " + key);  
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command initialized", key, EventImportance.kNormal);
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
                String key = command.getClass().getSimpleName() + "/" + command.getName();

                if(useConsole)
                    System.out.println("Command interrupted : " + key + " after " + currentCommands.get(key) + " runs");
                if(useDataLog) 
                    commandLogEntry.append("Command interrupted : " + key + " after " + currentCommands.get(key) + " runs");
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command interrupted", key, EventImportance.kNormal);

                currentCommands.put(key, 0);
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
                String key = command.getClass().getSimpleName() + "/" + command.getName();

                if(useConsole)
                    System.out.println("Command finished : " + key + " after " + currentCommands.get(key) + " runs");
                if(useDataLog) 
                    commandLogEntry.append("Command finished : " + key + " after " + currentCommands.get(key) + " runs");
                if(useShuffleBoardLog)
                    Shuffleboard.addEventMarker("Command finished", key, EventImportance.kNormal);

                currentCommands.put(key, 0);
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
                String key = command.getClass().getSimpleName() + "/" + command.getName();

                if(currentCommands.containsKey(key))
                {
                    if(useConsole)
                        System.out.println("Command executed : " + key);
                    if(useDataLog) 
                        commandLogEntry.append("Command executed : " + key);
                    if(useShuffleBoardLog)
                        Shuffleboard.addEventMarker("Command executed", key, EventImportance.kNormal);

                    currentCommands.put(key, 1);
                }
                else
                    currentCommands.put(key, currentCommands.get(key) + 1);
            }
        );
    }
}
