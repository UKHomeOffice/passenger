package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import java.io.Serializable;

public class Tuple<T, V> implements Serializable{
    private T _1;
    private V _2;


    public Tuple(T t, V v) {
        _1 = t;
        _2 = v;
    }

    public static <T, V> Tuple<T, V> tpl(T _1, V _2) {
        return new Tuple<>(_1, _2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (_1 != null ? !_1.equals(tuple._1) : tuple._1 != null) return false;
        return _2 != null ? _2.equals(tuple._2) : tuple._2 == null;
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                '}';
    }

    public T get_1() {
        return _1;
    }

    public V get_2() {
        return _2;
    }
}
