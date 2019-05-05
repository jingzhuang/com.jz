package BidPriceLP;

import BidPriceLP.Simulator;
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
            Simulator.instance().simulate();
            String directory = "/Users/ht88/huseyin/research/JavaCode/FareLocking/Data/";
            String label = "lp" + noSolves + "_" + holdPeriod + "_" + holdFee + "_" + lowLock + "_" + purchProb + "_" + load + ".txt";
            File outFile = new File ( directory + label );
            PrintWriter pw = new PrintWriter( new BufferedWriter ( new OutputStreamWriter ( new FileOutputStream ( outFile ) ) ) );
            pw.println( Simulator.instance().getRevenue() );
            double[] revenues = Simulator.instance().getRevenues();
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
    public static void main ( String[] args )
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
            String directory = "/Users/ht88/huseyin/research/JavaCode/FareLocking/Data/";
            String label = "lp" + noSolves + "_" + holdPeriod + "_";
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
                new Simulator( noSolves , noSamples );
                Simulator.instance().simulate();
                pw.println( Simulator.instance().getRevenue() );
                double[] revenues = Simulator.instance().getRevenues();
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
        }
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
    
    
    public static void mainSimple ( String[] args )
    {
        new DataInitializer( 25 , 70 , false , 0.4 , 1.0 );
        new DemandSampler();
        new Simulator( 1 , 100 );
        Simulator.instance().simulate();
        System.out.println( Simulator.instance().getRevenue() );
    }
}
