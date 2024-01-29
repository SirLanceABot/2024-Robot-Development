package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;

import frc.robot.Constants;

/** Represents a CTRE CANdle that controls the leds on our robot. */
public class Candle4237 extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    public enum LedStatus
    {
        kPurple, kYellow, kRed, kGreen, kWhite, kOff;
    }

    private class PeriodicData
    {
        // INPUTS

        // OUTPUTS
        // private LedStatus ledStatus = LedStatus.kOff;
        // private boolean isBlinking = true;
    }

    private final PeriodicData periodicData = new PeriodicData();
    private final CANdle candle = new CANdle(Constants.Candle.PORT, Constants.Candle.CAN_BUS);    
    private Animation animation;
    private double blinkSpeed = 0.4;
    public static final int LED_COUNT = 68;
    public static final int INITIAL_LED = 0;

    /** 
     * Creates a new Candle4237. 
     */
    public Candle4237()
    {
        super("Candle4237");
        System.out.println("  Constructor Started:  " + fullClassName);

        
        System.out.println("  Constructor Finished: " + fullClassName);
    }
    
    public void setPurple(boolean shouldBlink)
    {
        stop();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(255, 0, 255, 255, blinkSpeed, LED_COUNT));
        }
        else
        {
            candle.setLEDs(255, 0, 255, 255, INITIAL_LED, LED_COUNT);
        }
    }

    
    public void setRed(boolean shouldBlink)
    {
        stop();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(255, 0, 0, 255, blinkSpeed, LED_COUNT));
        }
        else
        {
            candle.setLEDs(255, 0, 0, 255, INITIAL_LED, LED_COUNT);
        }
    }

    public void setGreen(boolean shouldBlink)
    {
        stop();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(0, 255, 0, 255, blinkSpeed, LED_COUNT));
        }
        else
        {
            candle.setLEDs(0, 255, 0, 255, INITIAL_LED, LED_COUNT);
        }
    }

    public void setBlue(boolean shouldBlink)
    {
        stop();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(0, 0, 255, 255, blinkSpeed, LED_COUNT));
        }
        else
        {
            candle.setLEDs(0, 0, 255, 255, INITIAL_LED, LED_COUNT);
        }
    }

    
    public void stop()
    {
        candle.animate(null, 0);
        candle.setLEDs(0, 0, 0, 0, INITIAL_LED, LED_COUNT);
    }

    
    public void setRGBFade()
    {
        stop();

        candle.animate(new RgbFadeAnimation(1, 0.65, LED_COUNT));
    }

    public void setColorFlow()
    {
        stop();

        candle.animate(new ColorFlowAnimation(255, 0, 0, 255, 0.3, LED_COUNT, Direction.Forward));
    }

    public void setRainbow()
    {
        stop();

        candle.animate(new RainbowAnimation(1, 0.8, LED_COUNT));
    }

    public void setLarson()
    {
        stop();

        candle.animate(new LarsonAnimation(255, 0, 0, 255, 0.5, LED_COUNT, BounceMode.Center, 7));
    }

    // public void signalPurple()
    // {
    //     periodicData.ledStatus = LedStatus.kPurple;
    // }

    // public void blinkPurple()
    // {
    //     periodicData.ledStatus = LedStatus.kPurple;
    //     periodicData.isBlinking = true;
    // }

    // public void signalRed()
    // {
    //     periodicData.ledStatus = LedStatus.kRed;
    // }

    // public void signalOff()
    // {
    //     periodicData.ledStatus = LedStatus.kOff;
    //     candle.animate(null, 0);
    // }

    // private void setColor(int startLed, int ledCount, LedStatus status)
    // {
    //     switch (status)
    //     {
    //         case kPurple: 
    //             candle.setLEDs(255, 0, 255, 255, startLed, ledCount);
    //             break;
    //         case kYellow: 
    //             candle.setLEDs(255, 185, 0, 255, startLed, ledCount);
    //             break; 
    //         case kRed: 
    //             candle.setLEDs(255, 0, 0, 255, startLed, ledCount);
    //             break;
    //         case kGreen:
    //             candle.setLEDs(0, 255, 0, 255, startLed, ledCount);
    //             break;
    //         case kWhite:
    //             candle.setLEDs(255, 255, 255, 255, startLed, ledCount);
    //             break;
    //         case kOff: 
    //             candle.setLEDs(0, 0, 0, 0, startLed, ledCount);
    //             break;
    //         default:
    //             candle.setLEDs(0, 0, 0, 0, startLed, ledCount);
    //             break;
    //     }
    // }

    // private void setBlink()
    // {
    //     switch (periodicData.ledStatus)
    //     {
    //         case kPurple:
    //             animation = new StrobeAnimation(255, 0, 255, 255, 0.1, LED_COUNT);
    //     }
    //     candle.animate(animation, 0);
    // }

    @Override
    public void readPeriodicInputs()
    {

    }

    @Override
    public void writePeriodicOutputs()
    {
        // if(periodicData.ledStatus == LedStatus.kOff)
        // {
        //     signalOff();
        // }
        
        // else if(periodicData.isBlinking)
        // {
        //     setBlink();
        // }
        // else
        // {
        // setColor(startLed, ledCount, periodicData.ledStatus);
        // }
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
    
    @Override
    public String toString()
    {
        return "Led Count: " + LED_COUNT;
    }
}
