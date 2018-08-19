package clients.Exception

class BadResponse extends RuntimeException {
    def status

    BadResponse(String message, Integer status) {
        super(message)
        this.status = status
    }
}
