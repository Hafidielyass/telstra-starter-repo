package au.com.telstra.simcardactivator.api;

/**
 * Response payload returned to callers after attempting activation.
 */
public class ActivationResult {
    private boolean success;

    public ActivationResult() {
    }

    public ActivationResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
