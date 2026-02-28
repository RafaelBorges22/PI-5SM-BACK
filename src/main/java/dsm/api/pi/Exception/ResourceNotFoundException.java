package dsm.api.pi.Exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " não encontrado(a) com id: " + id);
    }

    public ResourceNotFoundException(String resource, String nome) {
        super(resource + " não encontrado(a) com nome: " + nome);
    }
}