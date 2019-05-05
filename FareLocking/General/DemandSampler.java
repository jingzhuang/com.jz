package General;

import java.util.*;

public class DemandSampler
{
    Random fare;
    Random lock;
    Random purch;
    double fr;
    double lr;
    double pr;
    static DemandSampler instance;
    
    /* this is a simple class that samples the demand, locking decision and purchase decision for each customer in the simulator */
    /* it is useful to do sampling in one class so that we can use common random numbers when simulating different benchmarks */
    public DemandSampler()
    {
        fare = new Random( 1L );
        lock = new Random( 2L );
        purch = new Random( 3L );
        instance = this;
    }
    
    
    /* call sample at every time period so that all is aligned */
    public void sample()
    {
        fr = fare.nextDouble();
        lr = lock.nextDouble();
        pr = lock.nextDouble();
    }
    
    
    public int getDemand ( int time )
    {
        double cdf = 0;
        for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
        {
            cdf += DataKeeper.instance().getArrivalProb( time , f );
            if ( fr < cdf )
            {
                return f;
            }
        }
        return -1;
    }
    
    
    public boolean getLock ( int fc )
    {
        double lp = DataKeeper.instance().getLockProb( fc );
        if ( lr < lp )
        {
            return true;
        }
        return false;
    }
    
    
    public boolean getPurch ( int fc )
    {
        double pp = DataKeeper.instance().getPurchProb( fc );
        if ( pr < pp )
        {
            return true;
        }
        return false;
    }
    
    
    public static DemandSampler instance()
    {
        return instance;
    }
}
