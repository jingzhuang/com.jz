package General;

public class StateVariable 
{
    int cap;
    int[][] locks;
    boolean[][] ultpr;
    
    /* this class is a holder that keeps of the current capacity on the flight and the fares locked in the past */
    /* it essentially keeps the current state of the system and it has functionality to update the state of the system */
    public StateVariable()
    {
        locks = new int[ DataKeeper.instance().getNoFares() ][];
        ultpr = new boolean[ DataKeeper.instance().getNoFares() ][];
        for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
        {
            locks[ f ] = new int[ DataKeeper.instance().getHold( f ) ];
            ultpr[ f ] = new boolean[ DataKeeper.instance().getHold( f ) ];
        }
    }
    
    
    public void updateCapacity ( int inc )
    {
        cap += inc;
    }
    
    
    public void shiftLockedFares()
    {
        for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
        {
            for ( int l = DataKeeper.instance().getHold( f ) - 1 ; l >= 1 ; l -- )
            {
                locks[ f ][ l ] = locks[ f ][ l - 1 ];
                ultpr[ f ][ l ] = ultpr[ f ][ l - 1 ];
            }
            locks[ f ][ 0 ] = 0;
            ultpr[ f ][ 0 ] = false;
        }
    }
    
    
    public void addLockedfare ( int fare , boolean ultPurchase )
    {
        locks[ fare ][ 0 ] = 1;
        ultpr[ fare ][ 0 ] = ultPurchase;
        
    }
    
    public void updateLockedFares ( int lockedFare , boolean ultpurchase )
    {
        for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
        {
            for ( int l = DataKeeper.instance().getHold( f ) - 1 ; l >= 1 ; l -- )
            {
                locks[ f ][ l ] = locks[ f ][ l - 1 ];
                ultpr[ f ][ l ] = ultpr[ f ][ l - 1 ];
            }
            locks[ f ][ 0 ] = 0;
            ultpr[ f ][ 0 ] = false;
        }
        if ( lockedFare != - 1)
        {
            locks[ lockedFare ][ 0 ] = 1;
            ultpr[ lockedFare ][ 0 ] = ultpurchase; 
        }
    }
    
    
    public void initialize()
    {
        cap = DataKeeper.instance().getCapacity();
        for ( int f = 0 ; f < DataKeeper.instance().getNoFares() ; f ++ )
        {
            for ( int l = 0 ; l < DataKeeper.instance().getHold( f ) ; l ++ )
            {
                locks[ f ][ l ] = 0;
                ultpr[ f ][ l ] = false;
            }
        }
    }
    
    
    public int[][] getLockedFares()
    {
        return locks;
    }
    
    
    public boolean[][] getUltimatePurchase()
    {
        return ultpr;
    }
    
    
    public int getCapacity()
    {
        return cap;
    }
}
