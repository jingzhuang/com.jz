package General;


public class DataKeeper
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
    static DataKeeper instance;
    
    /* this class is a holder for all the data of the problem */
    public DataKeeper( int noFares_ , int noTimePeriods_ , int capacity_ , int[] holds_ , double[] fares_ , double[] fees_ , double[] lockProbs_ , double[] purchProbs_ , double[][] arrivalProbs_ )
    {
        noFares = noFares_;
        noTimePeriods = noTimePeriods_;
        capacity = capacity_;
        holds = holds_;
        fares = fares_;
        fees = fees_;
        lockProbs = lockProbs_;
        purchProbs = purchProbs_;
        arrivalProbs = arrivalProbs_;
        instance = this;
    }
    
    
    public int getNoFares()
    {
        return noFares;
    }
    
    
    public int getNoTimePeriods()
    {
        return noTimePeriods;
    }
    
    
    public int getCapacity()
    {
        return capacity;
    }
    
    
    public int getHold( int fc )
    {
        return holds[ fc ];
    }
    
    
    public double getFare ( int fc )
    {
        return fares[ fc ];
    }
    
    
    public double getFee ( int fc )
    {
        return fees[ fc ];
    }
    
    
    public double getRevenue ( int fc )
    {
        return ( 1 - lockProbs[ fc ] ) * fares[ fc ] + lockProbs[ fc ] * ( fees[ fc ] + purchProbs[ fc ] * fares[ fc ] );
    }
    
    
    public double getLockProb ( int fc )
    {
        return lockProbs[ fc ];
    }
    
    
    public double getPurchProb ( int fc )
    {
        return purchProbs[ fc ];
    }
    
    
    public double getArrivalProb ( int t , int fc )
    {
        return arrivalProbs[ t ][ fc ];
    }
    
    
    public static DataKeeper instance()
    {
        return instance;
    }
}