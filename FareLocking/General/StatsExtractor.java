package General;

import java.io.*;
import java.util.*;

/* this class can be ignored, it is a simple text manipulator that is useful for exracting staticstics from output files */
public class StatsExtractor
{
    static String dels = " \t";
    
    public static void mainOneFromFile ( String[] args )
    {
        try
        {
            int noSamples = 1000;
            int noSolves = 5;
            double[][] revs = new double[ 2 ][ noSamples ];
            int[] holds = { 30, 60 };
            double[] fees = { 40 , 80 };
            boolean[] locks = { true , false };
            double[] purchs = { 0.4 , 0.7 };
            double[] loads = { 1.2 };

            for ( int h = 0 ; h < holds.length ; h ++ )
            {
                for ( int f = 0 ; f < fees.length ; f ++ )
                {
                    for ( int l = 0 ; l < locks.length ; l ++ )
                    {
                        for ( int p = 0 ; p < purchs.length ; p ++ )
                        {
                            for ( int w = 0 ; w < loads.length ; w ++ )
                            {
                                String directory = "C:\\Users\\HP\\Desktop\\zyyData2\\";
                                String label = "app" + noSolves + "_" + holds[ h ] + "_" + fees[ f ] + "_" + locks[ l ] + "_" + purchs[ p ] + "_" + loads[ w ] + ".txt";
                                File inFile = new File ( directory + label );
                                BufferedReader br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( inFile ) ) );
                                String out = label + " " ;
                                String line = br.readLine();
                                StringTokenizer stt = new StringTokenizer( line , dels );
                                double ub = Double.parseDouble( stt.nextToken() );
                                stt.nextToken();
                                double lpTime = Double.parseDouble( stt.nextToken() );
                                double dpTime = Double.parseDouble( stt.nextToken() );
                                out += ub + " ";
                                line = br.readLine();
                                stt = new StringTokenizer( line , dels );
                                double apav = 0;
                                for ( int sample = 0 ; sample < noSamples ; sample ++ )
                                {
                                    double rev = Double.parseDouble( stt.nextToken() );
                                    revs[ 0 ][ sample ] = rev;
                                    apav += rev;
                                }
                                apav = apav / noSamples;
                                out += apav + " " + ( 100 * ( ub - apav ) / ub ) + " ";
                                
                                label = "lp" + noSolves + "_" + holds[ h ] + "_" + fees[ f ] + "_" + locks[ l ] + "_" + purchs[ p ] + "_" + loads[ w ] + ".txt";
                                inFile = new File ( directory + label );
                                br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( inFile ) ) );
                                line = br.readLine();
                                line = br.readLine();
                                stt = new StringTokenizer( line , dels );
                                double lpav = 0;
                                for ( int sample = 0 ; sample < noSamples ; sample ++ )
                                {
                                    double rev = Double.parseDouble( stt.nextToken() );
                                    revs[ 1 ][ sample ] = rev;
                                    lpav += rev;
                                }
                                lpav = lpav / noSamples;
                                out += lpav + " " + ( 100 * ( ub - lpav ) / ub ) + " ";
                                
                                double average = 0;
                                for ( int sample = 0 ; sample < noSamples ; sample ++ )
                                {
                                    average += ( revs[ 0 ][ sample ] - revs[ 1 ][ sample ] );
                                }
                                average = average / noSamples;
                                
                                double stdev = 0;
                                for ( int sample = 0 ; sample < noSamples ; sample ++ )
                                {
                                    stdev += ( revs[ 0 ][ sample ] - revs[ 1 ][ sample ] - average ) * ( revs[ 0 ][ sample ] - revs[ 1 ][ sample ] - average );
                                }
                                stdev = Math.sqrt( stdev / ( noSamples - 1 ) );                                
                                out += " " + average + " " + stdev  + " " + ( average / ( stdev / Math.sqrt( noSamples ) ) ) + " " + lpTime + " " + dpTime;
                                System.out.println( out );
                            }
                        }
                    }
                }
            }
        }
        catch ( IOException e )
        {
            throw new Error ( e.getMessage() + " " + e.getClass() );
        }
    }
    

    public static void main ( String[] args )
    {
        try
        {
            int noProbs = 100;
            int noSamples = 1000;
            int noSolves = 5;
            double[] ubs = new double[ noProbs ];
            double[][][] objs = new double[ noProbs ][ 2 ][ noSamples ];
            double[] apgaps = new double[ noProbs ];
            double[] lpgaps = new double[ noProbs ];
            double[] comps = new double[ noProbs ];
            int[] holds = { 30 , 60 };
            String[] fares = { "wfar" , "tfar" };
            String[] probs = { "wloc" , "tloc" };
            String[] arrvs = { "cear" , "cmix" };
            double[] loads = { 1.2 };

            for ( int h = 0 ; h < holds.length ; h ++ )//holds 数组两层循环
            {
                for ( int f = 0 ; f < fares.length ; f ++ )
                {
                    for ( int l = 0 ; l < probs.length ; l ++ )
                    {
                        for ( int p = 0 ; p < arrvs.length ; p ++ )
                        {
                            for ( int w = 0 ; w < loads.length ; w ++ )
                            {
                                String directory = "C:\\Users\\HP\\Desktop\\zyyData2\\";
                                String label = "app" + noSolves + "_" + holds[ h ] + "_" + fares[ f ] + "_" + probs[ l ] + "_" + arrvs[ p ] + "_" + loads[ w ] + ".txt";
                                File inFile = new File ( directory + label );
                                BufferedReader br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( inFile ) ) );
                                for ( int prob = 0 ; prob < noProbs ; prob ++ )
                                {
                                    String line = br.readLine();
                                    StringTokenizer stt = new StringTokenizer( line , dels );
                                    double ub = Double.parseDouble( stt.nextToken() );
                                    ubs[ prob ] = ub;
                                    line = br.readLine();
                                    stt = new StringTokenizer( line , dels );
                                    for ( int s = 0 ; s < noSamples ; s ++ )
                                    {
                                        double ap = Double.parseDouble( stt.nextToken() );
                                        objs[ prob ][ 0 ][ s ] = ap;
                                    }
                                }

                                label = "lp" + noSolves + "_" + holds[ h ] + "_" + fares[ f ] + "_" + probs[ l ] + "_" + arrvs[ p ] + "_" + loads[ w ] + ".txt";
                                inFile = new File ( directory + label );
                                br = new BufferedReader ( new InputStreamReader ( new FileInputStream ( inFile ) ) );
                                for ( int prob = 0 ; prob < noProbs ; prob ++ )
                                {
                                    String line = br.readLine();
                                    line = br.readLine();
                                    StringTokenizer stt = new StringTokenizer( line , dels );
                                    for ( int s = 0 ; s < noSamples ; s ++ )
                                    {
                                        double lp = Double.parseDouble( stt.nextToken() );
                                        objs[ prob ][ 1 ][ s ] = lp;
                                    }
                                }

                                double gapap = 0;
                                double gaplp = 0;
                                int winap = 0;
                                int winlp = 0;
                                for ( int prob = 0 ; prob < noProbs ; prob ++ )
                                {
                                    double ub = ubs[ prob ];
                                    double avap = 0;
                                    double avlp = 0;
                                    double stdf = 0;
                                    for ( int s = 0 ; s < noSamples ; s ++ )
                                    {
                                        avap += objs[ prob ][ 0 ][ s ];
                                        avlp += objs[ prob ][ 1 ][ s ];
                                        double diff = objs[ prob ][ 0 ][ s ] - objs[ prob ][ 1 ][ s ];
                                        stdf += ( diff * diff );
                                    }
                                    avap = avap / noSamples;
                                    avlp = avlp / noSamples;
                                    stdf = ( stdf / ( noSamples - 1 ) ) - ( ( avap - avlp ) * ( avap - avlp ) * noSamples / ( noSamples - 1 ) );
                                    stdf = Math.sqrt( stdf / noSamples );
                                    gapap += ( 100 * ( ub - avap ) / ub );
                                    gaplp += ( 100 * ( ub - avlp ) / ub );
                                    if ( ( avap - avlp ) / stdf > 1.64 )
                                    {
                                        winap ++;
                                    }
                                    else if ( ( avap - avlp ) / stdf < - 1.64 )
                                    {
                                        winlp ++;
                                    }
                                    apgaps[ prob ] = 100 * ( ub - avap ) / ub;
                                    lpgaps[ prob ] = 100 * ( ub - avlp ) / ub;
                                    comps[ prob ] = 100 * ( avap - avlp ) / ub;
                                }
                                gapap = gapap / noProbs;
                                gaplp = gaplp / noProbs;
                                Arrays.sort( apgaps );
                                Arrays.sort( lpgaps );
                                Arrays.sort( comps );

                                //System.out.println( label + " "  + gapap + " " + gaplp + " " + winap  + " " + winlp );

                                double avap = 0;
                                double avlp = 0;
                                double avcomp = 0;
                                for ( int prob = 0 ; prob < noProbs ; prob ++ )
                                {
                                    avap += apgaps[ prob ];
                                    avlp += lpgaps[ prob ];
                                    avcomp += comps[ prob ];
                                }
                                avap = avap / noProbs;
                                double ap5 = apgaps[ (int) (noProbs * 0.1) ];
                                double ap95 = apgaps[ (int) (noProbs * 0.9) ];
                                avlp = avlp / noProbs;
                                double lp5 = lpgaps[ (int) (noProbs * 0.1) ];
                                double lp95 = lpgaps[ (int) (noProbs * 0.9) ];
                                avcomp = avcomp / noProbs;
                                double comp5 = comps[ (int) (noProbs * 0.1) ];
                                double comp95 = comps[ (int) (noProbs * 0.9) ];

                                System.out.println( label + " " + avap +  " " + ap5 + " " + ap95 + " " + avlp + " " + lp5 + " " + lp95 + " " + winap + " " + winlp + " " + avcomp + " " + comp5 + " " + comp95 );

                            }
                        }
                    }
                }
            }
        }
        catch ( IOException e )
        {
            throw new Error ( e.getMessage() + " " + e.getClass() );
        }
    }

}
