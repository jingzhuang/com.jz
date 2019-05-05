package Approximation;

import General.DataKeeper;
import General.StateVariable;
import gurobi.*;

/* this class solves the single seat dynamic programs for each seat, which provides the value function approximations for the approximate policy */
public class SeatDP 
{
    int solveTime;
    double[] values;
    double[][] arrivalProbs;
    
    public SeatDP( int solveTime_ )
    {
        solveTime = solveTime_;
        values = new double[ DataKeeper.instance().getNoTimePeriods() ];
        arrivalProbs = new double[ DataKeeper.instance().getNoTimePeriods() ][ DataKeeper.instance().getNoFares() ];
    }
    
    /* solve the DP to compute v_t as discussed in Section 3 of the paper */
    public void solve()
    {
        for ( int t = DataKeeper.instance().getNoTimePeriods() - 1 ; t > solveTime - 1 ; t -- )
        {
            values[ t ] = 0;
            for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
            {
                double val = DataKeeper.instance().getRevenue( f );
                if ( t + 1 < DataKeeper.instance().getNoTimePeriods() )
                {
                    val = val - values[ t + 1 ];
                }
                int hold = DataKeeper.instance().getHold( f );
                if ( t + hold + 1 < DataKeeper.instance().getNoTimePeriods() )
                {
                    val = val + ( DataKeeper.instance().getLockProb( f ) * ( 1 - DataKeeper.instance().getPurchProb( f ) ) * values[ t + hold + 1 ] );
                }
                val = Math.max( val , 0 );
                values[ t ] += arrivalProbs[ t ][ f ] * val; 
            }
            if ( t + 1 < DataKeeper.instance().getNoTimePeriods() )
            {
                values[ t ] += values[ t + 1 ];
            }
        }
    }
    
    /* compute C_t as dicussed in Section 8.1 of the paper when we recompute the approximate policy */
    public void updateArrivalProbs (StateVariable var , GRBVar[][] vars )
    {
        try
        {
            double cap = var.getCapacity();
            int[][] locks = var.getLockedFares();
            for ( int t = solveTime ; t < DataKeeper.instance().getNoTimePeriods() ; t ++ )
            {
                double cc = cap;
                for ( int f = 0 ; f < DataKeeper.instance().getNoFares(); f ++ )
                {
                    int hold = DataKeeper.instance().getHold( f );
                    for ( int l = 0 ; l < hold ; l ++ )
                    {
                        if ( t >= solveTime - ( l + 1 ) + hold )
                        {
                            cc += ( 1 - DataKeeper.instance().getPurchProb( f ) ) * locks[ f ][ l ];
                        }
                    }
                }
                
                for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
                {
                    arrivalProbs[ t ][ f ] = vars[ t ][ f ].get( GRB.DoubleAttr.X ) / cc;
                }
            }
        }
        catch ( Exception e )
        {
            throw new Error ( e.getMessage() + " " + e.getClass() );
        }
    }
    
    
    public double[] getValues()
    {
        return values;
    }
}
