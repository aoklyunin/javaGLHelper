package com.github.aoklyunin.javaGLHelper.scrollers.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * Параметры скроллера
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class SimpleScrollerParams {
    /**
     * тип скроллера
     */
    public enum ScrollerType {
        /**
         * Горизонтальный скроллер
         */
        HORIZONTAL,
        /**
         * Вертикальный скроллер
         */
        VERTICAL
    }

    /**
     * минимальное положение для рисования скроллера
     */
    private final double renderPosMin;
    /**
     * максимальное положение для рисования скроллера
     */
    private final double renderPosMax;
    /**
     * коэффициент разделения скроллера
     * (через каждые divideCoeff палочек рисуется палочка бОльшего размера)
     */
    private final int divideCoeff;
    /**
     * Тип скроллера
     */
    @NotNull
    private final ScrollerType type;
    /**
     * смещение скроллера при рисовании
     */
    private final double renderOffset;
    /**
     * размер скроллера при рисовании
     */
    private final double renderSize;
    /**
     * Точность
     */
    private final double accuracy;

    /**
     * Конструктор параметров скроллера
     *
     * @param type         коэффициент деления скроллера
     * @param renderPosMin верхняя граница скроллера
     * @param renderPosMax нижняя граница скроллера
     * @param divideCoeff  коэффициент деления скроллера
     * @param renderOffset смещение при рисовании скроллера
     * @param renderSize   размер скроллера при рисовании
     * @param accuracy     точность
     */
    @JsonCreator
    public SimpleScrollerParams(
            @NotNull @JsonProperty("type") ScrollerType type, @JsonProperty("renderPosMin") double renderPosMin,
            @JsonProperty("renderPosMax") double renderPosMax, @JsonProperty("divideCoeff") int divideCoeff,
            @JsonProperty("renderOffset") double renderOffset, @JsonProperty("renderSize") double renderSize,
            @JsonProperty("accuracy") double accuracy
    ) {
        this.renderPosMin = renderPosMin;
        this.renderPosMax = renderPosMax;
        this.divideCoeff = divideCoeff;
        this.type = Objects.requireNonNull(type);
        this.renderOffset = renderOffset;
        this.renderSize = renderSize;
        this.accuracy = accuracy;
    }

    /**
     * Конструктор параметров скроллера
     *
     * @param simpleScrollerParams новые парамтеры скроллера
     */
    public SimpleScrollerParams(@NotNull SimpleScrollerParams simpleScrollerParams) {
        this(
                simpleScrollerParams.type,
                simpleScrollerParams.renderPosMin,
                simpleScrollerParams.renderPosMax,
                simpleScrollerParams.divideCoeff,
                simpleScrollerParams.renderOffset,
                simpleScrollerParams.renderSize,
                simpleScrollerParams.accuracy
        );
    }

    /**
     * Получить минимальное положение для рисования скроллера
     *
     * @return минимальное положение для рисования скроллера
     */
    public double getRenderPosMin() {
        return renderPosMin;
    }

    /**
     * Получить максимальное положение для рисования скроллера
     *
     * @return максимальное положение для рисования скроллера
     */
    public double getRenderPosMax() {
        return renderPosMax;
    }

    /**
     * Получить  коэффициент разделения скроллера
     *
     * @return коэффициент разделения скроллера
     */
    public int getDivideCoeff() {
        return divideCoeff;
    }

    /**
     * Получить Тип скроллера
     *
     * @return Тип скроллера
     */
    public ScrollerType getType() {
        return type;
    }

    /**
     * Получить смещение скроллера при рисовании
     *
     * @return смещение скроллера при рисовании
     */
    public double getRenderOffset() {
        return renderOffset;
    }

    /**
     * Получить  размер скроллера при рисовании
     *
     * @return размер скроллера при рисовании
     */
    public double getRenderSize() {
        return renderSize;
    }

    /**
     * Получить Точность
     *
     * @return Точность
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "SimpleScrollerParams{getString()}"
     */
    @Override
    public String toString() {
        return "SimpleScrollerParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "type, renderPosMin, renderPosMax, renderOffset, renderSize, divideCoeff"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return type + ", " + String.format("%.3f", renderPosMin) + ", " + String.format("%.3f", renderPosMax) + ", " +
                String.format("%.3f", renderOffset) + ", " + String.format("%.3f", renderSize) + ", " + divideCoeff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleScrollerParams that = (SimpleScrollerParams) o;

        if (Double.compare(that.renderPosMin, renderPosMin) != 0) return false;
        if (Double.compare(that.renderPosMax, renderPosMax) != 0) return false;
        if (divideCoeff != that.divideCoeff) return false;
        if (Double.compare(that.renderOffset, renderOffset) != 0) return false;
        if (Double.compare(that.renderSize, renderSize) != 0) return false;
        if (Double.compare(that.accuracy, accuracy) != 0) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(renderPosMin);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(renderPosMax);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + divideCoeff;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        temp = Double.doubleToLongBits(renderOffset);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(renderSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(accuracy);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    /**
     * Преобразовать к параметрам 3D мозга
     *
     * @return параметры 3D мозга
     */
    @JsonIgnore
    public RangeScrollerParams getRangeScrollerParams() {
        if (this instanceof RangeScrollerParams)
            return (RangeScrollerParams) this;
        throw new AssertionError();
    }
}
