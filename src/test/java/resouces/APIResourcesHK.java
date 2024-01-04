package resouces;

public enum APIResourcesHK {

    AddBookRequest("/booking"),
    GetAllBooks("/booking"),
    UpdateBook("/booking/{id}"),
    DeleteBook("/booking/{id}");

    private String resource;

    APIResourcesHK(String resource) {
        this.resource = resource;

    }

    public String getResource() {
        return resource;
    }
}
