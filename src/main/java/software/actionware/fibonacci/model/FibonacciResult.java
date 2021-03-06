package software.actionware.fibonacci.model;

import java.math.BigInteger;


public class FibonacciResult {
    public final String type;
    public final String title;
    public final int status;
    public final String detail;
    //public final String instance;
    public final BigInteger result;

    public FibonacciResult(String type,
                           String title,
                           int status,
                           String detail,
                          // String instance,
                           BigInteger result) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        //this.instance = instance;
        this.result = result;
    }

}
