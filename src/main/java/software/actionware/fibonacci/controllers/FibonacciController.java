package software.actionware.fibonacci.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import software.actionware.fibonacci.domain.FibonacciCalc;
import software.actionware.fibonacci.model.FibonacciResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@RestController
public class FibonacciController {

    public static final int TIMEOUT_CALC_SECONDS = 60;

    public static final String MSG_ERROR_PARAMETER_NULL = "La posición no puede ser nula";
    public static final String MSG_ERROR_PARAMETER_NEG = "La posición no puede ser menor a 0. [%d]";

  
    @GetMapping(
            path = {"/api/fibonacci/{posicion}"},
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<FibonacciResult> getFibonacciPosition(HttpServletRequest request, @PathVariable Optional<Integer> posicion) {
        BigInteger result = BigInteger.ZERO;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<BigInteger> future = null;
        try {
            if (!posicion.isPresent()){
                throw new IllegalArgumentException(MSG_ERROR_PARAMETER_NULL);
            }
            if (posicion.get() < 0) {
                throw new IllegalArgumentException(String.format(MSG_ERROR_PARAMETER_NEG, posicion.get()));
            }

            future = executor.submit(new FibonacciCalc(posicion.get()));
            result = future.get(TIMEOUT_CALC_SECONDS, TimeUnit.SECONDS);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity
                    .badRequest()
                    .body(new FibonacciResult("/errors/bad-parameters", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), iae.getMessage(),  result));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FibonacciResult("/errors/server-error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), result));
        } finally {
            executor.shutdownNow();
        }

        return ResponseEntity
                .ok(new FibonacciResult("", HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(), "",  result));
    }

}
