/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
@Inherited
public @interface Property {
    public Type type();

    public String name();

    public String parent() default "";

    public String note() default "";

    public boolean warning() default false;

    public boolean beta() default false;

    public int step() default 1;

    public String prefix() default "";

    public String suffix() default "";

    public int min() default 0;

    public int max() default 0x7FFFFFFF;

    public String[] options() default {};

    public static enum Type {
        BOOLEAN,
        FOLDER,
        NUMBER,
        SELECT,
        CHECKBOX;

    }
}

