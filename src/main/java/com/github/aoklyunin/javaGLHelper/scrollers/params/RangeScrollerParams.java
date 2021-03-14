package com.github.aoklyunin.javaGLHelper.scrollers.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import java.util.Objects;

/**
 * Параметры скроллера с диапазоном отображения
 */
public class RangeScrollerParams extends SimpleScrollerParams {
    /**
     * начальное кол-во пунктов скроллера для отображения
     */
    private final int initDisplayRange;
    /**
     * минимальное кол-во пунктов скроллера для отображения
     */
    private final int minDisplayRange;


    /**
     * Конструктор параметров скроллера
     *
     * @param type             коэффициент деления скроллера
     * @param renderPosMin     верхняя граница скроллера
     * @param renderPosMax     нижняя граница скроллера
     * @param divideCoeff      коэффициент деления скроллера
     * @param renderOffset     смещение при рисовании скроллера
     * @param renderSize       размер скроллера при рисовании
     * @param accuracy         точность
     * @param initDisplayRange начальное кол-во пунктов скроллера для отображения
     * @param minDisplayRange  минимальное кол-во пунктов скроллера для отображения
     */
    @JsonCreator
    public RangeScrollerParams(
            @NotNull @JsonProperty("type") ScrollerType type, @JsonProperty("renderPosMin") double renderPosMin,
            @JsonProperty("renderPosMax") double renderPosMax, @JsonProperty("divideCoeff") int divideCoeff,
            @JsonProperty("renderOffset") double renderOffset, @JsonProperty("renderSize") double renderSize,
            @JsonProperty("accuracy") double accuracy, @JsonProperty("initDisplayRange") int initDisplayRange,
            @JsonProperty("minDisplayRange") int minDisplayRange
    ) {
        super(Objects.requireNonNull(type), renderPosMin, renderPosMax, divideCoeff, renderOffset, renderSize, accuracy);
        this.initDisplayRange = initDisplayRange;
        this.minDisplayRange = minDisplayRange;
    }

    /**
     * Конструктор парамтеров скроллера с диапазоном отображения
     *
     * @param rangeScrollerParams новые параметры скроллера с диапазоном отображения
     */
    public RangeScrollerParams(RangeScrollerParams rangeScrollerParams) {
        super(Objects.requireNonNull(rangeScrollerParams));
        this.initDisplayRange = rangeScrollerParams.initDisplayRange;
        this.minDisplayRange = rangeScrollerParams.minDisplayRange;
    }

    /**
     * Получить начальное кол-во пунктов скроллера для отображения
     *
     * @return начальное кол-во пунктов скроллера для отображения
     */
    public int getInitDisplayRange() {
        return initDisplayRange;
    }

    /**
     * Получить минимальное кол-во пунктов скроллера для отображения
     *
     * @return минимальное кол-во пунктов скроллера для отображения
     */
    public int getMinDisplayRange() {
        return minDisplayRange;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RangeScrollerParams{getString()}"
     */
    @Override
    public String toString() {
        return "RangeScrollerParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "minDisplayRange, initDisplayRange, super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return minDisplayRange + ", " + initDisplayRange + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RangeScrollerParams that = (RangeScrollerParams) o;

        if (initDisplayRange != that.initDisplayRange) return false;
        return minDisplayRange == that.minDisplayRange;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + initDisplayRange;
        result = 31 * result + minDisplayRange;
        return result;
    }

}
