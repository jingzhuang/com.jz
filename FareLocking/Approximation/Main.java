package Approximation;

import General.DataInitializer;
import General.DataInitializerRandomized;
import General.DemandSampler;


import java.io.*;

public class Main 
{
    /* run this main routine to carry out numerical experiments for the base test problems */
    public static void mainOneFromFile ( String[] args )
    {
        try
        {
            int holdPeriod = Integer.parseInt( args [ 0 ] );
            double holdFee = Double.parseDouble( args[ 1 ] );
            boolean lowLock = Boolean.parseBoolean( args[ 2 ] );
            double purchProb = Double.parseDouble( args[ 3 ] );
            double load = Double.parseDouble( args[ 4 ] );
            int noSolves = Integer.parseInt( args[ 5 ] );
            
            int noSamples = 1000;
            new DataInitializer( holdPeriod , holdFee , lowLock , purchProb , load );
            new DemandSampler();
            new Simulator( noSolves , noSamples );
            Approximation.Simulator.instance().simulate();
            String directory = "E:\\Program Files\\java\\zyy-project\\FareLocking\\outFile\\";
            String label = "app" + noSolves + "_" + holdPeriod + "_" + holdFee + "_" + lowLock + "_" + purchProb + "_" + load + ".txt";
            File outFile = new File ( directory + label );
            PrintWriter pw = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( new FileOutputStream ( outFile ) ) ) );
            pw.println( Approximation.Simulator.instance().getUpperBound()
                    + " " + Approximation.Simulator.instance().getRevenue() + " "
                    + Approximation.Simulator.instance().getLPTime() + " " +
                    Approximation.Simulator.instance().getDPTime() );
            double[] revenues = Approximation.Simulator.instance().getRevenues();
            for ( int s = 0 ; s < noSamples ; s ++ )
            {
                pw.print( revenues[ s ] + " " );
            }
            pw.println();
            pw.flush();
        }
        catch ( Exception e )
        {
        }
    }
    
    /* run this main routine to carry out numerical experiments for randomly generated test problems */
    //randomly
    public static void randomly ( String[] args )
    {
        try
        {
            int holdPeriod = Integer.parseInt( args [ 0 ] );
            boolean wideFare = Boolean.parseBoolean( args[ 1 ] );
            boolean wideLock = Boolean.parseBoolean( args[ 2 ] );
            boolean cheapEarly = Boolean.parseBoolean( args[ 3 ] );
            double load = Double.parseDouble( args[ 4 ] );
            int noSolves = Integer.parseInt( args[ 5 ] );

            int noSamples = 1000;
            new DataInitializerRandomized( holdPeriod , wideFare , wideLock , cheapEarly , load );
            String directory = "E:\\Program Files\\java\\zyy-project\\FareLocking\\outFile\\";
            String label = "app" + noSolves + "_" + holdPeriod + "_";
            if ( wideFare )
            {
                label += "wfar_";
            }
            else
            {
                label += "tfar_";
            }
            if ( wideLock )
            {
                label += "wloc_";
            }
            else
            {
                label += "tloc_";
            }
            if ( cheapEarly )
            {
                label += "cear_";
            }
            else
            {
                label += "cmix_";
            }
            label += load + ".txt";
            File outFile = new File ( directory + label );
            PrintWriter pw = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( new FileOutputStream ( outFile ) ) ) );

            for ( int p = 0 ; p < 100 ; p ++ )
            {
                long seed = 141 * p + 546 + holdPeriod + 2 * val( wideFare ) + 4 * val( wideLock ) + 8 * val( cheapEarly ) + ( (int) ( 10 * load ) );
                DataInitializerRandomized.instance().initialize( seed );
                new DemandSampler();
                new Approximation.Simulator( noSolves , noSamples );
                Approximation.Simulator.instance().simulate();
                pw.println( Approximation.Simulator.instance().getUpperBound()
                        + " " + Approximation.Simulator.instance().getRevenue() +
                        " " + Approximation.Simulator.instance().getLPTime() + " " +
                        Approximation.Simulator.instance().getDPTime() );
                double[] revenues = Approximation.Simulator.instance().getRevenues();
                for ( int s = 0 ; s < noSamples ; s ++ )
                {
                    pw.print( revenues[ s ] + " " );
                }
                pw.println();
                pw.flush();
            }
        }
        catch ( Exception e )
        {
            System.out.println("zyy"+ e);
        }
    }
    
    //mainSimple
    public static void mainSimple ( String[] args )
    {
        new DataInitializer( 25 , 70 , true , 0.5 , 1.2 );
        new DemandSampler();
        new Approximation.Simulator( 1 , 100 );
        Approximation.Simulator.instance().simulate();
        System.out.println( Approximation.Simulator.instance().getUpperBound()
                + " " + Approximation.Simulator.instance().getRevenue());
    }

    public static void main(String[] args) {
        String path = System.getProperty("java.library.path");
        System.out.println("Java path: " + path);
    }
    
    
    public static int val ( boolean var )
    {
        if ( var )
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    
}
