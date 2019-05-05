package Approximation;

import General.DataKeeper;
import General.StateVariable;
import gurobi.*;

public class DeterministicLP
{
    GRBEnv env;
    GRBModel lp;
    GRBVar[][] saleVars;
    GRBConstr[] consts;
    double[] rhs;
    int solveTime;
    
    /* this class sets up and solves the deterministic approximation, the number of variables and constraints in the LP depend on the time period at which we solve the LP */
    public DeterministicLP( int solveTime_ )
    {
        try
        {
            solveTime = solveTime_;
            env = new GRBEnv(null);
            /* use dual simplex */
            env.set( GRB.IntParam.Method , 1 );
            env.set( GRB.IntParam.OutputFlag , 0 );
            env.set( GRB.DoubleParam.FeasibilityTol , 1e-9 );
            env.set( GRB.DoubleParam.OptimalityTol , 1e-9 );
            lp = new GRBModel( env );
            /* maximize objective */
            lp.set( GRB.IntAttr.ModelSense , -1 );
            /* although we are creating noTimePeriods x noFares array, we skip some of the initial time periods when we do not solve the LP at first time period */
            saleVars = new GRBVar[ DataKeeper.instance().getNoTimePeriods() ][ DataKeeper.instance().getNoFares() ];
            /* similarly, although we are creating noTimePeriods array, we skip come of hte initial time periods */
            consts = new GRBConstr[ DataKeeper.instance().getNoTimePeriods() ];
            rhs = new double[ DataKeeper.instance().getNoTimePeriods() ];
            construct();
        }
        catch ( Exception e )
        {
            throw new Error ( e.getClass() + " " + e.getMessage() );
        }
    }


    public void construct()
    {
        try
        {
            int noTimePeriods = DataKeeper.instance().getNoTimePeriods();
            int noFares = DataKeeper.instance().getNoFares();
            for ( int t = solveTime ; t < noTimePeriods ; t ++ )
            {
                for ( int f = 0 ; f < noFares ; f ++ )
                {
                    double obj = DataKeeper.instance().getRevenue( f );
                    double ub = DataKeeper.instance().getArrivalProb( t , f );
                    GRBVar saleVar = lp.addVar( 0 , ub , obj , GRB.CONTINUOUS , "z_" + t + "_" + f );
                    saleVars[ t ][ f ] = saleVar;
                }
            }
            lp.update();
            
            for ( int t = solveTime ; t < noTimePeriods ; t ++ )
            {
                GRBLinExpr linexp = new GRBLinExpr();
                for ( int f = 0 ; f < noFares ; f ++ )
                {
                    for ( int k = solveTime ; k <= t ; k ++ )
                    {
                        linexp.addTerm( 1.0 , saleVars[ k ][ f ] );
                    }
                }
                for ( int f = 0 ; f < noFares ; f ++ )
                {
                    int hold = DataKeeper.instance().getHold( f );
                    double lp = DataKeeper.instance().getLockProb( f );
                    double pp = DataKeeper.instance().getPurchProb( f );
                    for ( int k = solveTime ; k <= t - hold ; k ++ )
                    {
                        linexp.addTerm( - lp * ( 1 - pp ) , saleVars[ k ][ f ] );
                    }
                }
                GRBConstr con = lp.addConstr( linexp , GRB.LESS_EQUAL , 0 , "c_" + t );
                consts[ t ] = con;
            }
            lp.update();
            // lp.write( "det.lp" );
        }
        catch ( Exception e )
        {
            throw new Error ( e.getClass() + " " + e.getMessage() );
        }
    }
    
    
    public void update ( StateVariable var )
    {
        try
        {
            int[][] locks = var.getLockedFares();
            for ( int t = solveTime ; t < DataKeeper.instance().getNoTimePeriods() ; t ++ )
            {
                rhs[ t ] = var.getCapacity();
            }
            
            for ( int f = 0 ; f < DataKeeper.instance().getNoFares(); f ++ )
            {
                int hold = DataKeeper.instance().getHold( f );
                for ( int l = 0 ; l < hold ; l ++ )
                {
                    if ( locks[ f ][ l ] != 0 )
                    {
                        for ( int t = solveTime - ( l + 1 ) + hold ; t < DataKeeper.instance().getNoTimePeriods() ; t ++ )
                        {
                            rhs[ t ] += ( 1 - DataKeeper.instance().getPurchProb( f ) );
                        }
                    }
                }
            }
            for ( int t = solveTime ; t < DataKeeper.instance().getNoTimePeriods() ; t ++ )
            {
                consts[ t ].set( GRB.DoubleAttr.RHS, rhs[ t ] );
            }
            lp.update();
            //lp.write( "det.lp" );
        }
        catch ( Exception e )
        {
            throw new Error ( e.getClass() + " " + e.getMessage() );
        }
    }
    
    
    public GRBVar getSaleVar ( int t , int fc )
    {
        return saleVars[ t ][ fc ];
    }
    
    
    public GRBVar[][] getSaleVars()
    {
        return saleVars;
    }
    
    public void solve()
    {
        try
        {
            lp.optimize();
            //System.out.println( "opt obj " + lp.get( GRB.DoubleAttr.ObjVal ) );
        }
        catch ( Exception e )
        {
            throw new Error ( e.getClass() + " " + e.getMessage() );
        }
    }
    
    
    public double getObjective()
    {
        try
        {
            return lp.get( GRB.DoubleAttr.ObjVal );
        }
        catch ( Exception e )
        {
            throw new Error ( e.getClass() + " " + e.getMessage() );
        }
    }
    
    
    public GRBModel getModel()
    {
        return lp;
    }
    
    
    public GRBEnv getEnv()
    {
        return env;
    }
}
