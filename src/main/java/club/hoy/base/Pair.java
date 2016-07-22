package club.hoy.base;

public class Pair<K, V> {
    private K key;
    private V value;

    public K getKey() {
        return (K) key;
    }

    public V getValue() {
        return (V) value;
    }

    public Pair(K paramK, V paramV) {
        key = paramK;
        value = paramV;
    }

    public String toString() {
        return key + "=" + value;
    }

    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject instanceof Pair)) {
            Pair localPair = (Pair) paramObject;
            if (key != null ? !key.equals(key) : key != null) {
                return false;
            }
            return value != null ? value.equals(value) : value == null;
        }
        return false;
    }
}