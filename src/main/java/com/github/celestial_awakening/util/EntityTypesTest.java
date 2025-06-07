package com.github.celestial_awakening.util;

import javax.annotation.Nullable;

//a variation of EntityTypeTest that checks if the given entity is one of various classes
public interface EntityTypesTest<B, T extends B> {
    static <B, T extends B> EntityTypesTest<B, T> forClass(final Class<T> p_156917_) {
        return new EntityTypesTest<B, T>() {
            @Nullable
            public T tryCast(B p_156924_) {
                return (T)(p_156917_.isInstance(p_156924_) ? p_156924_ : null);
            }

            public Class<? extends B> getBaseClass() {
                return p_156917_;
            }
        };
    }

    @Nullable
    T tryCast(B p_156918_);

    Class<? extends B> getBaseClass();
}