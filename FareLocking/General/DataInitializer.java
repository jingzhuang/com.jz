package General;


import General.DataKeeper;

import java.util.Random;

public class DataInitializer
{
    int noFares;
    int noTimePeriods;
    int capacity;
    int[] holds;
    double[] fares;
    double[] fees;
    double[] lockProbs;
    double[] purchProbs;
    double[][] arrivalProbs;
    Random random;
    static DataKeeper instance;
    
    
    /* this class sets up the base problem instances as described in Section 8.2 of the paper */
    public DataInitializer ( int holdPeriod , double holdFee , boolean lowLock , double purchaseProb , double load )
    {
        noFares = 4;
        noTimePeriods = 120;
        holds = new int[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            holds[ f ] = holdPeriod;
        }
        
        fares = new double[ noFares ];
        fares[ 0 ] = 800;
        fares[ 1 ] = 600;
        fares[ 2 ] = 400;
        fares[ 3 ] = 200;
        
        fees = new double[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            fees[ f ] = holdFee;
        }
        
        lockProbs = new double[ noFares ];
        if ( lowLock )
        {
            lockProbs[ 0 ] = 0.1;
            lockProbs[ 1 ] = 0.15;
            lockProbs[ 2 ] = 0.2;
            lockProbs[ 3 ] = 0.25;
        }
        else
        {
            lockProbs[ 0 ] = 0.4;
            lockProbs[ 1 ] = 0.45;
            lockProbs[ 2 ] = 0.5;
            lockProbs[ 3 ] = 0.55;
        }
               
        purchProbs = new double[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            purchProbs[ f ] = purchaseProb;
        }
        arrivalProbs = new double[ noTimePeriods ][ noFares ];
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            if ( t < noTimePeriods / 3 )
            {
                arrivalProbs[ t ][ 0 ] = 0.05;
                arrivalProbs[ t ][ 1 ] = 0.1;
                arrivalProbs[ t ][ 2 ] = 0.15;
                arrivalProbs[ t ][ 3 ] = 0.2;
            }
            else if ( t < 2 * noTimePeriods / 3 )
            {
                arrivalProbs[ t ][ 0 ] = 0.125;
                arrivalProbs[ t ][ 1 ] = 0.125;
                arrivalProbs[ t ][ 2 ] = 0.125;
                arrivalProbs[ t ][ 3 ] = 0.125;
            }
            else
            {
                arrivalProbs[ t ][ 0 ] = 0.2;
                arrivalProbs[ t ][ 1 ] = 0.15;
                arrivalProbs[ t ][ 2 ] = 0.1;
                arrivalProbs[ t ][ 3 ] = 0.05;
            }
        }
        
        double expDemand = 0;
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            for ( int i = 0 ; i < noFares ; i ++ )
            {
                expDemand += arrivalProbs[ t ][ i ] * ( 1 - lockProbs[ i ] + lockProbs[ i ] * purchProbs[ i ] );
            }
        }
        
        capacity = (int) ( expDemand / load );
        System.out.println( capacity );
        
        new DataKeeper( noFares , noTimePeriods , capacity , holds , fares , fees , lockProbs , purchProbs , arrivalProbs );
    }
    
    /* this version of the data initializer sets up the base problems used in the first version of the paper */
    /* it is irrelevant to the current version of the paper, but we kept it here for archival reasons */
    public void DataInitializerFirstVersion ( int holdPeriod , double holdFee , boolean lowLock , double purchaseProb , double load )
    {
        random = new Random( 1L );
        noFares = 4;
        noTimePeriods = 300;
        capacity = 100;
        holds = new int[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            holds[ f ] = holdPeriod;
        }
        
        fares = new double[ noFares ];
        fares[ 0 ] = 1000;
        fares[ 1 ] = 750;
        fares[ 2 ] = 500;
        fares[ 3 ] = 250;
        
        fees = new double[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            fees[ f ] = holdFee;
        }
        
        lockProbs = new double[ noFares ];
        if ( lowLock )
        {
            lockProbs[ 0 ] = 0.1;
            lockProbs[ 1 ] = 0.15;
            lockProbs[ 2 ] = 0.2;
            lockProbs[ 3 ] = 0.25;
            
            lockProbs[ 0 ] = 0.5;
            lockProbs[ 1 ] = 0.5;
            lockProbs[ 2 ] = 0.5;
            lockProbs[ 3 ] = 0.5;
        }
        else
        {
            lockProbs[ 0 ] = 0.4;
            lockProbs[ 1 ] = 0.45;
            lockProbs[ 2 ] = 0.5;
            lockProbs[ 3 ] = 0.55;
        }
               
        purchProbs = new double[ noFares ];
        for ( int f = 0 ; f < noFares ; f ++ )
        {
            purchProbs[ f ] = purchaseProb;
        }
        arrivalProbs = new double[ noTimePeriods ][ noFares ];
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            if ( t < noTimePeriods / 3 )
            {
                arrivalProbs[ t ][ 0 ] = 2.5;
                arrivalProbs[ t ][ 1 ] = 5.0;
                arrivalProbs[ t ][ 2 ] = 7.5;
                arrivalProbs[ t ][ 3 ] = 10;
                double sum = arrivalProbs[ t ][ 0 ] + arrivalProbs[ t ][ 1 ] + arrivalProbs[ t ][ 2 ] + arrivalProbs[ t ][ 3 ];
                arrivalProbs[ t ][ 0 ] = arrivalProbs[ t ][ 0 ] / sum;
                arrivalProbs[ t ][ 1 ] = arrivalProbs[ t ][ 1 ] / sum;
                arrivalProbs[ t ][ 2 ] = arrivalProbs[ t ][ 2 ] / sum;
                arrivalProbs[ t ][ 3 ] = arrivalProbs[ t ][ 3 ] / sum;
            }
            else if ( t < 2 * noTimePeriods / 3 )
            {
                arrivalProbs[ t ][ 0 ] = 5;
                arrivalProbs[ t ][ 1 ] = 5;
                arrivalProbs[ t ][ 2 ] = 7.5;
                arrivalProbs[ t ][ 3 ] = 7.5;
                double sum = arrivalProbs[ t ][ 0 ] + arrivalProbs[ t ][ 1 ] + arrivalProbs[ t ][ 2 ] + arrivalProbs[ t ][ 3 ];
                arrivalProbs[ t ][ 0 ] = arrivalProbs[ t ][ 0 ] / sum;
                arrivalProbs[ t ][ 1 ] = arrivalProbs[ t ][ 1 ] / sum;
                arrivalProbs[ t ][ 2 ] = arrivalProbs[ t ][ 2 ] / sum;
                arrivalProbs[ t ][ 3 ] = arrivalProbs[ t ][ 3 ] / sum;
            }
            else
            {
                arrivalProbs[ t ][ 0 ] = 10;
                arrivalProbs[ t ][ 1 ] = 7.5;
                arrivalProbs[ t ][ 2 ] = 5;
                arrivalProbs[ t ][ 3 ] = 2.5;
                double sum = arrivalProbs[ t ][ 0 ] + arrivalProbs[ t ][ 1 ] + arrivalProbs[ t ][ 2 ] + arrivalProbs[ t ][ 3 ];
                arrivalProbs[ t ][ 0 ] = arrivalProbs[ t ][ 0 ] / sum;
                arrivalProbs[ t ][ 1 ] = arrivalProbs[ t ][ 1 ] / sum;
                arrivalProbs[ t ][ 2 ] = arrivalProbs[ t ][ 2 ] / sum;
                arrivalProbs[ t ][ 3 ] = arrivalProbs[ t ][ 3 ] / sum;
            }
        }    
        double expDemand = 0;
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            for ( int i = 0 ; i < noFares ; i ++ )
            {
                expDemand += arrivalProbs[ t ][ i ] * ( 1 - lockProbs[ i ] + lockProbs[ i ] * purchProbs[ i ] );
            }
        }
        double mult = load * capacity / expDemand;
        if ( mult > 1 )
        {
            throw new Error();
        }
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            for ( int i = 0 ; i < noFares ; i ++ )
            {
                arrivalProbs[ t ][ i ] = arrivalProbs[ t ][ i ] * mult;
            }
        }
        
        new DataKeeper( noFares , noTimePeriods , capacity , holds , fares , fees , lockProbs , purchProbs , arrivalProbs );
    }
    
    public void DataInitializerSimple()
    {
        noFares = 2;
        noTimePeriods = 10;
        capacity = 2;
        holds = new int[ noFares ];
        holds[ 0 ] = 2;
        holds[ 1 ] = 2;
        fares = new double[ noFares ];
        fares[ 0 ] = 50;
        fares[ 1 ] = 100;
        fees = new double[ noFares ];
        fees[ 0 ] = 10;
        fees[ 1 ] = 20;
        lockProbs = new double[ noFares ];
        lockProbs[ 0 ] = 1.0;
        lockProbs[ 1 ] = 1.0;
        purchProbs = new double[ noFares ];
        purchProbs[ 0 ] = 0.0;
        purchProbs[ 1 ] = 0.0;
        arrivalProbs = new double[ noTimePeriods ][ noFares ];
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            for ( int f = 0 ; f < noFares ; f ++ )
            {
                arrivalProbs[ t ][ f ] = 0.5;
            }
        }       
        new DataKeeper( noFares , noTimePeriods , capacity , holds , fares , fees , lockProbs , purchProbs , arrivalProbs );
    }
}