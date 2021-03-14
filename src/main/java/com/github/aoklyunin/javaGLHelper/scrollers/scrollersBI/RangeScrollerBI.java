package com.github.aoklyunin.javaGLHelper.scrollers.scrollersBI;

import com.github.aoklyunin.javaGLHelper.scrollers.params.RangeScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Скроллер с диапазоном выбранных значений. Перемещается не выбранное значение, а диапазон
 */
public class RangeScrollerBI extends SimpleScrollerBI {
    /**
     * Строитель простых скроллеров
     */
    static class Builder extends AbstractBuilder<Builder> {
        /**
         * Диапазон
         */
        long range;

        /**
         * Конструктор строителя
         *
         * @param scrollerParams параметры скроллера
         */
        public Builder(@NotNull RangeScrollerParams scrollerParams) {
            super(Objects.requireNonNull(scrollerParams));
        }

        /**
         * Задать диапазон
         *
         * @param range диапазон
         * @return строитель
         */
        Builder range(long range) {
            this.range = range;
            return self();
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */
        @NotNull
        @Override
        RangeScrollerBI build() {
            return new RangeScrollerBI(this);
        }

        /**
         * Подклассы должны перекрывать этот метод, возвращая "себя"
         */
        @Override
        protected Builder self() {
            return this;
        }
    }

    /**
     * диапазон отображения скроллера
     */
    private long range;

    /**
     * Конструктор простого скроллера
     *
     * @param builder строитель
     */
    public RangeScrollerBI(@NotNull AbstractBuilder<?> builder) {
        super(Objects.requireNonNull(builder));
        this.range = ((Builder) builder).range;
    }

    /**
     * Получить скроллер
     *
     * @param rangeScrollerParams параметры скроллера
     * @param getSize             метод, возвращающий размер скроллера
     * @return скроллер
     */
    public static RangeScrollerBI of(
            @NotNull RangeScrollerParams rangeScrollerParams, @NotNull Supplier<BigInteger> getSize
    ) {
        return new Builder(Objects.requireNonNull(rangeScrollerParams))
                .getSize(Objects.requireNonNull(getSize)).build();
    }

    /**
     * Получить размер скроллера
     *
     * @return размер скроллера
     */
    @NotNull
    @Override
    protected BigInteger getSize() {
        return getSize.get().subtract(BigInteger.valueOf(range - 1));
    }

    /**
     * Задать положение курсора скроллера
     *
     * @param mouseGLPos координаты клика мышьью
     * @return флаг, получилось ли изменить положение скроллера(попал ли клик в область его рисования)
     */
    @Override
    public boolean setScrollerCursorPosByClick(@NotNull Vector2d mouseGLPos) {
        if (getSize.get().compareTo(BigInteger.valueOf(range)) <= 0)
            return false;
        return super.setScrollerCursorPosByClick(Objects.requireNonNull(mouseGLPos));
    }

    /**
     * Рисование скроллера
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    public void renderScroller(GL2 gl2) {
        if (getSize.get().compareTo(BigInteger.valueOf(range)) > 0)
            super.renderScroller(gl2);
    }

    /**
     * Передвинуть курсор скроллера, если рассматриваемое
     * новое положение находится вне текуего диапазона отображения
     *
     * @param newPos новое положение скроллера
     */
    public void truncate(@NotNull BigInteger newPos) {
        if (getSize.get().compareTo(BigInteger.valueOf(range)) >= 0) {
            if (cursorPos.compareTo(newPos.subtract(BigInteger.valueOf(range - 1))) < 0)
                cursorPos = newPos.subtract(BigInteger.valueOf(range - 1));
            if (cursorPos.compareTo(newPos) > 0)
                cursorPos = newPos;
        }
    }

    /**
     * Получить минимальное отображаемое положение скроллера
     *
     * @return минимальное отображаемое положение скроллера
     */
    @NotNull
    public BigInteger getMin() {
        if (getSize.get().compareTo(BigInteger.valueOf(range)) <= 0) {
            return BigInteger.ZERO;
        } else {
            return cursorPos;
        }
    }

    /**
     * Получить максимальное отображаемое положение скроллера
     *
     * @return максимальное отображаемое положение скроллера
     */
    @NotNull
    public BigInteger getMax() {
        if (getSize.get().compareTo(BigInteger.valueOf(range)) <= 0) {
            return getSize.get().subtract(BigInteger.ONE);
        } else {
            return cursorPos.add(BigInteger.valueOf(range - 1));
        }
    }

    /**
     * Изменить диапазон отображения скроллера
     *
     * @param delta изменение диапазона
     */
    public void changeRange(int delta) {
        range += delta;
        if (range < getParams().getRangeScrollerParams().getMinDisplayRange())
            range = getParams().getRangeScrollerParams().getMinDisplayRange();
        if (getSize.get().compareTo(BigInteger.valueOf(range)) > 0 &&
                cursorPos.add(BigInteger.valueOf(range)).compareTo(getSize.get().subtract(BigInteger.ONE)) > 0)
            cursorPos = getSize.get().subtract(BigInteger.valueOf(range));
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "RangeScrollerBI{getString()}"
     */
    @Override
    public String toString() {
        return "RangeScrollerBI{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "range, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return range + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RangeScrollerBI that = (RangeScrollerBI) o;

        return range == that.range;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (range ^ (range >>> 32));
        return result;
    }
}
