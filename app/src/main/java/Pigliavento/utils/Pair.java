package Pigliavento.utils;

public final class Pair<T, S> {
    private T firstElement;
    private S secondElement;

    /**
     * 
     * @param firstElement  the first element of the pair
     * @param secondElement the second element of the pair
     */
    public Pair(final T firstElement, final S secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    /**
     * @return the first element of the pair
     */
    public T getFirst() {
        return this.firstElement;
    }

    /**
     * @return the second element of the pair
     */
    public S getSecond() {
        return this.secondElement;
    }

    /**
     * Sets the first element of the pair.
     * 
     * @param firstElement
     */
    public void setFirstElement(final T firstElement) {
        this.firstElement = firstElement;
    }

    /**
     * Sets the second element of the pair.
     * 
     * @param secondElement
     */
    public void setSecondElement(final S secondElement) {
        this.secondElement = secondElement;
    }

    /**
     * @return a copy of the pair
     */
    public Pair<T, S> copy() {
        return new Pair<T, S>(firstElement, secondElement);
    }

}
