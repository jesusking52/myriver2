package com.riverauction.riverauction.feature.common;


import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Denotes that an integer parameter, field or method return value is expected
 * to represent a dimension.
 */
@Documented
@Retention(CLASS)
@Target({METHOD,PARAMETER,FIELD,LOCAL_VARIABLE,ANNOTATION_TYPE})
public @interface Dimension {
    @Unit
    int unit() default PX;

    int DP = 0;
    int PX = 1;
    int SP = 2;

    @IntDef({PX, DP, SP})
    @Retention(RetentionPolicy.SOURCE)
    @interface Unit {}
}