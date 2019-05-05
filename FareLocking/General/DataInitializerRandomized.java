package General;

import java.util.*;

public class DataInitializerRandomized
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
    int holdPeriod;
    boolean wideFare;
    boolean wideLock;
    boolean cheapEarly;
    double load;
    Random random;
    static DataInitializerRandomized instance;
    
    
    /* this class randomly generates the problem instances as described in Section 8.3 of the paper */
    public DataInitializerRandomized ( int holdPeriod_ , boolean wideFare_ , boolean wideLock_ , boolean cheapEarly_ , double load_ )
    {
        random = new Random( 1L );
        noFares = 4;
        noTimePeriods = 120;
        holds = new int[ noFares ];
        fares = new double[ noFares ];
        fees = new double[ noFares ];
        lockProbs = new double[ noFares ];
        purchProbs = new double[ noFares ];
        arrivalProbs = new double[ noTimePeriods ][ noFares ];
        holdPeriod = holdPeriod_;
        wideFare = wideFare_;
        wideLock = wideLock_;
        cheapEarly = cheapEarly_;
        load = load_;
        instance = this;
    }
    
    
    public void initialize ( long seed )
    {
        random.setSeed( seed );
        for ( int i = 0 ; i < noFares ; i ++ )
        {
            holds[ i ] = holdPeriod;
            if ( wideFare )
            {
                fares[ i ] = random.nextDouble() * 1000 + 400;
            }
            else
            {
                fares[ i ] = random.nextDouble() * 400 + 700;
            }
            fees[ i ]  = random.nextDouble() * 40 + 40;
            if ( wideLock )
            {
                lockProbs[ i ] = random.nextDouble() * 0.6 + 0.1;
            }
            else
            {
                lockProbs[ i ] = random.nextDouble() * 0.2 + 0.3;
            }
        }
        for ( int i = 0 ; i < noFares ; i ++ )
        {
            purchProbs[ i ] = random.nextDouble() * 0.4 + 0.3;
        }
        for ( int i = 0 ; i < noFares ; i ++ )
        {
            double first = random.nextDouble();
            double second = random.nextDouble();
            double third = random.nextDouble();
            for ( int t = 0 ; t < noTimePeriods ; t ++ )
            {
                if ( t < noTimePeriods / 3 )
                {
                    arrivalProbs[ t ][ i ] = first;
                }
                else if ( t < 2 * noTimePeriods / 3 )
                {
                    arrivalProbs[ t ][ i ] = second;                    
                }
                else
                {
                    arrivalProbs[ t ][ i ] = third;
                }
            }
        }
        
        /* ensure that last fare is cheapest */
        for ( int i = 0 ; i < noFares ; i ++ )
        {
            fares[ i ] = - fares[ i ];
        }
        Arrays.sort( fares );
        for ( int i = 0 ; i < noFares ; i ++ )
        {
            fares[ i ] = - fares[ i ];
        }
        
        if ( cheapEarly )
        {
            for ( int t = 0 ; t < noTimePeriods / 3 ; t ++ )
            {
                Arrays.sort( arrivalProbs[ t ] );
            }
        }
        
        /* normalize arrival probabilities */
        for ( int t = 0 ; t < noTimePeriods ; t ++ )
        {
            double sum = 0;
            for ( int i = 0 ; i < noFares ; i ++ )
            {
                sum += arrivalProbs[ t ][ i ];
            }
            for ( int i = 0 ; i < noFares ; i ++ )
            {
                arrivalProbs[ t ][ i ]  = arrivalProbs[ t ][ i ] / ( 2 * sum );
            }
        }
        
        /* find capacity to match load */
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
    
    
    public static DataInitializerRandomized instance()
    {
        return instance;
    }
}
