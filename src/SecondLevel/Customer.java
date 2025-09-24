package SecondLevel;

/**
 * The customer API provides the functionality for retrieving information from the screen.
 */
public class Customer {
    /**
     * Returns where in the transaction the customer is null,cancel,pause,cancel,resume,start.
     * @return should be some kind of message, putting object in right now as a placeholder
     */
    public Object getStatus(){
        return null;
    }

    /**
     * Returns which gas type the customer selected, null(for no gas selected),low,mid,high.
     * @return
     */
    public int getGasChoice(){
        return 0;
    }
}
