package brubank

class UrlMappings {

    static mappings = {
        "/correlation/$user"(controller: "correlation") {
            action = [GET: "getCorrelation"]
        }

        "/"(controller: "correlation") {
            action = [GET: "index"]
        }

        "/cache/$cache"(controller: "cache") {
            action = [GET: "content"]
        }

        "/cache/$cache/flush"(controller: "cache") {
            action = [GET: "flush"]
        }

        "404"(view: '/notFound')
    }
}
