package us.exequt.ecommerce.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import us.exequt.ecommerce.cart.CartItemNotFoundException;
import us.exequt.ecommerce.cart.CartLockedException;
import us.exequt.ecommerce.cart.CartNotFoundException;
import us.exequt.ecommerce.cart.IllegalCartStateException;
import us.exequt.ecommerce.mock.MockPaymentAttemptNotFoundException;
import us.exequt.ecommerce.order.IllegalOrderStateException;
import us.exequt.ecommerce.order.OrderAlreadyCanceledException;
import us.exequt.ecommerce.order.OrderNotFoundException;
import us.exequt.ecommerce.payment.IllegalPaymentAttemptStateException;
import us.exequt.ecommerce.payment.PaymentAttemptNotFoundException;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleCartItemNotFoundException(CartItemNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), "Cart item not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleCartNotFoundException(CartNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), "Cart not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(CartLockedException.class)
    public ResponseEntity<RestResponse<Void>> handleCartLockedException(CartLockedException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Cart is locked for checkout", List.of(ex.getMessage())));
    }

    @ExceptionHandler(IllegalCartStateException.class)
    public ResponseEntity<RestResponse<Void>> handleIllegalCartStateException(IllegalCartStateException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Illegal cart state", List.of(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Void>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Validation failed", errors));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleOrderNotFoundException(OrderNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), "Order not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(OrderAlreadyCanceledException.class)
    public ResponseEntity<RestResponse<Void>> handleOrderAlreadyCanceledException(OrderAlreadyCanceledException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Order already canceled", List.of(ex.getMessage())));
    }

    @ExceptionHandler(IllegalOrderStateException.class)
    public ResponseEntity<RestResponse<Void>> handleIllegalOrderStateException(IllegalOrderStateException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Illegal order state", List.of(ex.getMessage())));
    }

    @ExceptionHandler(PaymentAttemptNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handlePaymentAttemptNotFoundException(PaymentAttemptNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), "Payment attempt not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(IllegalPaymentAttemptStateException.class)
    public ResponseEntity<RestResponse<Void>> handleIllegalPaymentAttemptStateException(IllegalPaymentAttemptStateException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Illegal payment attempt state", List.of(ex.getMessage())));
    }

    @ExceptionHandler(MockPaymentAttemptNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleMockPaymentAttemptNotFoundException(MockPaymentAttemptNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), "Mock payment attempt not found", List.of(ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI(), "An unexpected error occurred", List.of(ex.getMessage())));
    }
}