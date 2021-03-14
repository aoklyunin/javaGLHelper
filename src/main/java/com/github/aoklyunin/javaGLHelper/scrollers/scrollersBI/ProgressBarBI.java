package com.github.aoklyunin.javaGLHelper.scrollers.scrollersBI;

import com.github.aoklyunin.javaGLHelper.GLAlgorithms;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Простой скроллер
 */
public class ProgressBarBI extends SimpleScrollerBI {

    /**
     * Строитель простых скроллеров
     */
    static class Builder extends AbstractBuilder<Builder> {
        /**
         * Конструктор строителя
         *
         * @param scrollerParams параметры скроллера
         */
        public Builder(@NotNull SimpleScrollerParams scrollerParams) {
            super(Objects.requireNonNull(scrollerParams));
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */
        @NotNull
        @Override
        ProgressBarBI build() {
            return new ProgressBarBI(this);
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
     * Конструктор простого скроллера
     *
     * @param builder строитель
     */
    protected ProgressBarBI(@NotNull AbstractBuilder<?> builder) {
        super(Objects.requireNonNull(builder));
    }

    /**
     * Получить скроллер
     *
     * @param scrollerParams            параметры скроллера
     * @param getSize                   метод, возвращающий размер скроллера
     * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
     * @return скроллер
     */
    public static ProgressBarBI of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<BigInteger> getSize,
            @NotNull Consumer<BigInteger> processChangedScrollerPos
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams)).getSize(Objects.requireNonNull(getSize))
                .processChangedScrollerPos(Objects.requireNonNull(processChangedScrollerPos)).build();
    }

    /**
     * Получить скроллер
     *
     * @param scrollerParams параметры скроллера
     * @param getSize        метод, возвращающий размер скроллера
     * @param getPos         метод, возвращающий положение скроллера
     * @return скроллер
     */
    public static ProgressBarBI of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<BigInteger> getPos,
            @NotNull Supplier<BigInteger> getSize
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams)).getSize(Objects.requireNonNull(getSize))
                .getPos(Objects.requireNonNull(getPos)).build();
    }


    /**
     * Рисование горизонтального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    @Override
    protected void renderHorizontalScroller(GL2 gl2, @NotNull BigInteger pos) {
        GLAlgorithms.renderLineQuad(
                gl2, getParams().getRenderOffset(), getParams().getRenderPosMin(), getParams().getRenderSize(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin()
        );
        GLAlgorithms.renderFilledQuad(
                gl2, getParams().getRenderOffset(), getParams().getRenderPosMin(),
                BigDecimal.valueOf(getParams().getRenderSize()).divide(
                        new BigDecimal(getSize().subtract(BigInteger.ONE)), 6, RoundingMode.CEILING
                ).multiply(new BigDecimal(pos)).doubleValue(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin()
        );
    }

    /**
     * Рисование вертикального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    @Override
    protected void renderVerticalScroller(GL2 gl2, @NotNull BigInteger pos) {
        GLAlgorithms.renderLineQuad(
                gl2, getParams().getRenderPosMin(), getParams().getRenderOffset(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin(), getParams().getRenderSize()
        );
        GLAlgorithms.renderFilledQuad(
                gl2, getParams().getRenderPosMin(), getParams().getRenderOffset(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin(),
                BigDecimal.valueOf(getParams().getRenderSize()).divide(
                        new BigDecimal(getSize().subtract(BigInteger.ONE)), 6, RoundingMode.CEILING
                ).multiply(new BigDecimal(pos)).doubleValue()
        );
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "ProgressBarBI{getString()}"
     */
    @Override
    public String toString() {
        return "ProgressBarBI{" + getString() + '}';
    }
}
