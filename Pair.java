public class Pair<K, V> {

    private K k; private V v;

    public Pair(K _k, V _v) {
        this.k = _k; this.v = _v;
    }

    public V getV() {
        return this.v;
    }

    public K getK() {
        return this.k;
    }

}