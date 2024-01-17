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
        private LedStatus ledStatus = LedStatus.kOff;
        private boolean isBlinking = true;
    }

    private PeriodicData periodicData = new PeriodicData();
    // private final CANdle candle = new CANdle(Constants.Candle.CANDLE_PORT, "rio");
    private final CANdle candle = new CANdle(1, "rio");    
    private int startLed = 0;
    private int ledCount = 68;
    private Animation animation;
    private double blinkSpeed = 0.4;

    /** 
     * Creates a new Candle4237. 
     */
    public Candle4237()
    {
        super("Candle4237");
        System.out.println("  Constructor Started:  " + fullClassName);

        
        System.out.println("  Constructor Finished: " + fullClassName);
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

    
    public void setPurple(boolean shouldBlink)
    {
        setOff();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(255, 0, 255, 255, blinkSpeed, ledCount));
        }
        else
        {
            candle.setLEDs(255, 0, 255, 255, startLed, ledCount);
        }
    }

    
    public void setRed(boolean shouldBlink)
    {
        setOff();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(255, 0, 0, 255, blinkSpeed, ledCount));
        }
        else
        {
            candle.setLEDs(255, 0, 0, 255, startLed, ledCount);
        }
    }

    public void setGreen(boolean shouldBlink)
    {
        setOff();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(0, 255, 0, 255, blinkSpeed, ledCount));
        }
        else
        {
            candle.setLEDs(0, 255, 0, 255, startLed, ledCount);
        }
    }

    public void setBlue(boolean shouldBlink)
    {
        setOff();

        if(shouldBlink)
        {
            candle.animate(new StrobeAnimation(0, 0, 255, 255, blinkSpeed, ledCount));
        }
        else
        {
            candle.setLEDs(0, 0, 255, 255, startLed, ledCount);
        }
    }

    
    public void setOff()
    {
        candle.animate(null, 0);
        candle.setLEDs(0, 0, 0, 0, startLed, ledCount);
    }

    
    public void setRGBFade()
    {
        setOff();

        candle.animate(new RgbFadeAnimation(1, 0.65, ledCount));
    }

    public void setColorFlow()
    {
        setOff();

        candle.animate(new ColorFlowAnimation(255, 0, 0, 255, 0.3, ledCount, Direction.Forward));
    }

    public void setRainbow()
    {
        setOff();

        candle.animate(new RainbowAnimation(1, 0.8, ledCount));
    }

    public void setLarson()
    {
        setOff();

        candle.animate(new LarsonAnimation(255, 0, 0, 255, 0.5, ledCount,BounceMode.Center, 7));
    }

    // public void signalRed()
    // {
    //     periodicData.ledStatus = LedStatus.kRed;
    // }

    // public void signalOff()
    // {
    //     periodicData.ledStatus = LedStatus.kOff;
    //     candle.animate(null, 0);
    // }

    private void setColor(int startLed, int ledCount, LedStatus status)
    {
        switch (status)
        {
            case kPurple: 
                candle.setLEDs(255, 0, 255, 255, startLed, ledCount);
                break;
            case kYellow: 
                candle.setLEDs(255, 185, 0, 255, startLed, ledCount);
                break; 
            case kRed: 
                candle.setLEDs(255, 0, 0, 255, startLed, ledCount);
                break;
            case kGreen:
                candle.setLEDs(0, 255, 0, 255, startLed, ledCount);
                break;
            case kWhite:
                candle.setLEDs(255, 255, 255, 255, startLed, ledCount);
                break;
            case kOff: 
                candle.setLEDs(0, 0, 0, 0, startLed, ledCount);
                break;
            default:
                candle.setLEDs(0, 0, 0, 0, startLed, ledCount);
                break;
        }
    }

    private void setBlink()
    {
        switch (periodicData.ledStatus)
        {
            case kPurple:
                animation = new StrobeAnimation(255, 0, 255, 255, 0.1, ledCount);
        }
        candle.animate(animation, 0);
    }

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
}
