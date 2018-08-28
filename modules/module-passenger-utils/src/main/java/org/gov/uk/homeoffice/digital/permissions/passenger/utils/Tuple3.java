package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import java.util.Objects;

public class Tuple3<T,V, Z> {
    public final T _1;
    public final V _2;
    public final Z _3;


    public Tuple3(T t, V v, Z z) {
        _1 = t;
        _2 = v;
        _3 = z;
    }

    public static <T,V,Z> Tuple3<T,V,Z> tpl(T _1, V _2, Z _3){
        return new Tuple3<>(_1, _2, _3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(_1, tuple3._1) &&
                Objects.equals(_2, tuple3._2) &&
                Objects.equals(_3, tuple3._3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(_1, _2, _3);
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                ", _3=" + _3 +
                '}';
    }
}
