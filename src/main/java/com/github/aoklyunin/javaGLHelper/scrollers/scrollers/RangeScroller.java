package com.github.aoklyunin.javaGLHelper.scrollers.scrollers;

import com.github.aoklyunin.javaGLHelper.scrollers.params.RangeScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Скроллер с диапазоном выбранных значений. Перемещается не выбранное значение, а диапазон
 */
public class RangeScroller extends SimpleScroller {
    /**
     * Строитель простых скроллеров
     */
    static class Builder extends AbstractBuilder<Builder> {
        /**
         * Конструктор строителя
         *
         * @param scrollerParams параметры скроллера
         */
        Builder(@NotNull RangeScrollerParams scrollerParams) {
            super(Objects.requireNonNull(scrollerParams));
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */
        @Override
        RangeScroller build() {
            return new RangeScroller(this);
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
    private RangeScroller(@NotNull AbstractBuilder<?> builder) {
        super(Objects.requireNonNull(builder));
        this.range = builder.params.getRangeScrollerParams().getInitDisplayRange();
    }

    /**
     * Получить скроллер
     *
     * @param rangeScrollerParams параметры скроллера
     * @param getSize             метод, возвращающий размер скроллера
     * @return скроллер
     */
    public static RangeScroller of(@NotNull RangeScrollerParams rangeScrollerParams, @NotNull Supplier<Long> getSize) {
        return new Builder(Objects.requireNonNull(rangeScrollerParams))
                .getSize(Objects.requireNonNull(getSize)).build();
    }

    /**
     * Получить размер скроллера
     *
     * @return размер скроллера
     */
    @Override
    protected long getSize() {
        return getSize.get() - range + 1;
    }

    /**
     * Задать положение курсора скроллера
     *
     * @param mouseGLPos координаты клика мышьью
     * @return флаг, получилось ли изменить положение скроллера(попал ли клик в область его рисования)
     */
    @Override
    public boolean setScrollerCursorPosByClick(@NotNull Vector2d mouseGLPos) {
        if (getSize.get() <= range)
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
        if (getSize.get() > range)
            super.renderScroller(gl2);
    }

    /**
     * Рисование скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    @Override
    public void renderScroller(GL2 gl2, long pos) {
        if (getSize.get() > range)
            super.renderScroller(gl2);
    }

    /**
     * Передвинуть курсор скроллера, если рассматриваемое
     * новое положение находится вне текуего диапазона отображения
     *
     * @param newPos новое положение скроллера
     */
    public void truncate(int newPos) {
        if (getSize.get() >= range) {
            if (cursorPos < newPos - range + 1)
                cursorPos = newPos - range + 1;
            if (cursorPos > newPos)
                cursorPos = newPos;
        }
    }

    /**
     * Получить минимальное отображаемое положение скроллера
     *
     * @return минимальное отображаемое положение скроллера
     */
    public int getMin() {
        if (getSize.get() <= range) {
            return 0;
        } else {
            return (int) cursorPos;
        }
    }

    /**
     * Получить максимальное отображаемое положение скроллера
     *
     * @return максимальное отображаемое положение скроллера
     */
    public int getMax() {
        if (getSize.get() <= range) {
            return (int) (getSize.get() - 1);
        } else {
            return (int) (cursorPos + range - 1);
        }
    }

    /**
     * Изменить диапазон отображения скроллера
     *
     * @param delta изменение диапазона
     */
    public void changeRange(int delta) {
        setRange(range + delta);
    }

    /**
     * Задать диапазон скроллера
     *
     * @param newRange новый диапазон
     */
    public void setRange(long newRange) {
        range = newRange;
        if (range < getParams().getRangeScrollerParams().getMinDisplayRange())
            range = getParams().getRangeScrollerParams().getMinDisplayRange();
        if (getSize.get() > range && cursorPos + range > getSize.get() - 1) {
            cursorPos = getSize.get() - 1 - range;
        }
        if (range > getSize.get())
            range = getSize.get();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RangeScroller{getString()}"
     */
    @Override
    public String toString() {
        return "RangeScroller{" + getString() + '}';
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

        RangeScroller that = (RangeScroller) o;

        return range == that.range;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (range ^ (range >>> 32));
        return result;
    }
}
