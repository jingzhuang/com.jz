package Approximation;

import General.DataKeeper;
import General.DemandSampler;
import General.StateVariable;
import gurobi.*;

public class Simulator 
{
    int noSolves;
    int noSamples;
    int solvePeriod;
    DeterministicLP[] lps;
    Approximation.SeatDP[] dps;
    StateVariable state;
    double averageRevenue;
    double lpTime;
    double dpTime;
    double[] revenues;
    static Simulator instance;
    
    public Simulator ( int noSolves_ , int noSamples_ )
    {
        noSolves = noSolves_;
        noSamples = noSamples_;
        solvePeriod = (int) Math.ceil( ( (double) DataKeeper.instance().getNoTimePeriods() ) / noSolves );
        lps = new DeterministicLP[ noSolves ];
        dps = new Approximation.SeatDP[ noSolves ];
        for ( int s = 0 ; s < noSolves ; s ++ )
        {
            lps[ s ] = new DeterministicLP( s * solvePeriod );
            dps[ s ] = new Approximation.SeatDP( s * solvePeriod );
        }
        state = new StateVariable();
        revenues = new double[ noSamples ];
        instance = this;
    }
    
    /* simulates the performance of the approximate policy with a certain number of recomputations of the approximate policy and for a certain number of sample paths */
    public void simulate ()
    {
        double totalRevenue = 0;
        for ( int s = 0 ; s < noSamples ; s ++ )
        {
            double revenue = 0;
            double[] values = null;
            state.initialize();
            int solveTime = 0;
            int solveNumber = 0;
            /* check if we need to recompute the approximate policy */
            for ( int t = 0 ; t < DataKeeper.instance().getNoTimePeriods() ; t ++ )
            {
                if ( t == solveTime )
                {
                    if ( ( t != 0 ) || ( s == 0 ) )
                    {
                        long start = System.currentTimeMillis();
                        lps[ solveNumber ].update( state );
                        lps[ solveNumber ].solve();
                        GRBVar[][] vars = lps[ solveNumber ].getSaleVars();
                        if ( ( t == 0 ) && ( s == 0 ) )
                        {
                            lpTime = ( System.currentTimeMillis() - start );
                        }
                        start = System.currentTimeMillis();
                        dps[ solveNumber ].updateArrivalProbs( state , vars );
                        dps[ solveNumber ].solve();
                        if ( ( t == 0 ) && ( s == 0 ) )
                        {
                            dpTime = ( System.currentTimeMillis() - start );
                        }
                    }
                    solveTime += solvePeriod;
                    solveNumber ++;
                }
                /* sample the demand and see whether we accept the request */
                values = dps[ solveNumber - 1 ].getValues();
                DemandSampler.instance().sample();
                int fc = DemandSampler.instance().getDemand( t );
                int capacity = state.getCapacity();
                boolean locked = false;
                if ( ( capacity > 0 ) && ( fc != - 1 ) )
                {
                    double val = DataKeeper.instance().getRevenue( fc );
                    if ( t + 1 < DataKeeper.instance().getNoTimePeriods() )
                    {
                        val -= values[ t + 1 ];
                    }
                    int hold = DataKeeper.instance().getHold( fc );
                    if ( t + hold + 1 < DataKeeper.instance().getNoTimePeriods() )
                    {
                        val += ( DataKeeper.instance().getLockProb( fc ) * ( 1 - DataKeeper.instance().getPurchProb( fc ) ) * values[ t + hold + 1 ] );
                    }
                    /* we have a sale or a lock, update the capacity */
                    if ( val >= 0 )
                    {
                        revenue += DataKeeper.instance().getRevenue( fc );
                        locked = DemandSampler.instance().getLock( fc );
                        state.updateCapacity( -1 );
                    }
                }
                
                /* since time elapses, "shift" the locked fares by one time period */
                int[][] lockedFares = state.getLockedFares();
                boolean[][] ultPurchase = state.getUltimatePurchase();
                for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
                {
                    int hold = DataKeeper.instance().getHold( f );
                    if ( ( lockedFares[ f ][ hold - 1 ] != 0 ) && ( ultPurchase[ f ][ hold - 1 ] == false ) )
                    {
                        state.updateCapacity( +1 );
                    }
                }
                
                state.shiftLockedFares();
                
                if ( locked )
                {
                   boolean up = DemandSampler.instance().getPurch( fc );
                   state.addLockedfare( fc , up );
                }
            }
            totalRevenue += revenue;
            revenues[ s ] =revenue;
        }
        averageRevenue = totalRevenue / noSamples;
    }
    
    public double getRevenue()
    {
        return averageRevenue;
    }
    
    
    public double[] getRevenues()
    {
        return revenues;
    }
    
    public double getUpperBound()
    {
        return lps[ 0 ].getObjective();
    }
    
    public double getLPTime()
    {
        return lpTime;
    }
    
    
    public double getDPTime()
    {
        return dpTime;
    }
    
    public static Simulator instance()
    {
        return instance;
    }
}
