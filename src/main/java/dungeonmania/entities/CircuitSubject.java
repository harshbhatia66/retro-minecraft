package dungeonmania.entities;

public interface CircuitSubject extends CircuitComponent {
    public void subscribe(CircuitObserver observer);

    public void unsubscribe(CircuitObserver observer);
}
