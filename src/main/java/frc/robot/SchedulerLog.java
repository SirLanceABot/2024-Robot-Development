package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class SchedulerLog 
{

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public SchedulerLog()
    {
        configureSchedulerLog();
    }

    /////////////////////////////////////////
    // Command Event Loggers
    /////////////////////////////////////////
    void configureSchedulerLog()
    {
        boolean useDataLog = true;
        boolean useShuffleBoardLog = true;
        StringLogEntry commandLogEntry = null;

        // if(useShuffleBoardLog || useDataLog)
        {
        // Set the scheduler to log events for command initialize, interrupt,
        // finish, execute
        // Log to the ShuffleBoard and the WPILib data log tool.
        // If ShuffleBoard is recording these events are added to the recording. Convert
        // recording to csv and they show nicely in Excel. 
        // If using data log tool, the recording is automatic so run that tool to retrieve and convert the log.
        //_________________________________________________________________________________

        CommandScheduler.getInstance()
            .onCommandInitialize(
                command ->
                {
                    if(useDataLog)
                        commandLogEntry.append(command.getClass() + " " + command.getName() + " initialized");
                        
                    if(useShuffleBoardLog)
                    {
                        Shuffleboard.addEventMarker("Command initialized", command.getName(), EventImportance.kNormal);
                        System.out.println("Command initialized " + command.getName());
                    }
                }
            );
        //_________________________________________________________________________________

        CommandScheduler.getInstance()
            .onCommandInterrupt(
                command ->
                {
                    if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " interrupted");
                    if(useShuffleBoardLog)
                    {
                        Shuffleboard.addEventMarker("Command interrupted", command.getName(), EventImportance.kNormal);
                        System.out.println("Command interrupted " + command.getName());
                    }
                }
            );
        //_________________________________________________________________________________

        CommandScheduler.getInstance()
            .onCommandFinish(
                command ->
                {
                    if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " finished");
                    if(useShuffleBoardLog)
                    {
                        Shuffleboard.addEventMarker("Command finished", command.getName(), EventImportance.kNormal);
                        System.out.println("Command finished " + command.getName());
                    }
                }
            );
        //_________________________________________________________________________________

        CommandScheduler.getInstance()
            .onCommandExecute( // this can generate a lot of events
                command ->
                {
                    if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " executed");
                    if(useShuffleBoardLog)
                    {
                        Shuffleboard.addEventMarker("Command executed", command.getName(), EventImportance.kNormal);
                        // System.out.println("Command executed " + command.getName());
                    }
                }
            );
        //_________________________________________________________________________________
        }
    }
}
