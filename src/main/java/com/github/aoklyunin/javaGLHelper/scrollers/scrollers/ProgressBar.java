package com.github.aoklyunin.javaGLHelper.scrollers.scrollers;

import com.github.aoklyunin.javaGLHelper.GLAlgorithms;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Простой скроллер
 */
public class ProgressBar extends SimpleScroller {

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

        @Override
        ProgressBar build() {
            return new ProgressBar(this);
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
    protected ProgressBar(@NotNull AbstractBuilder<?> builder) {
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
    public static ProgressBar of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<Long> getSize,
            @NotNull Consumer<Long> processChangedScrollerPos
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams))
                .getSize(Objects.requireNonNull(getSize))
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
    public static ProgressBar of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<Long> getSize, @NotNull Supplier<Long> getPos
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams))
                .getSize(Objects.requireNonNull(getSize))
                .getPos(Objects.requireNonNull(getPos)).build();
    }

    /**
     * Рисование горизонтального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    @Override
    protected void renderHorizontalScroller(GL2 gl2, long pos) {
        GLAlgorithms.renderLineQuad(
                gl2, getParams().getRenderOffset(), getParams().getRenderPosMin(), getParams().getRenderSize(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin()
        );
        GLAlgorithms.renderFilledQuad(
                gl2, getParams().getRenderOffset(), getParams().getRenderPosMin(), getParams().getRenderSize() /
                        (getSize() - 1) * pos,
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
    protected void renderVerticalScroller(GL2 gl2, long pos) {
        GLAlgorithms.renderLineQuad(
                gl2, getParams().getRenderPosMin(), getParams().getRenderOffset(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin(), getParams().getRenderSize()
        );
        GLAlgorithms.renderFilledQuad(
                gl2, getParams().getRenderPosMin(), getParams().getRenderOffset(),
                getParams().getRenderPosMax() - getParams().getRenderPosMin(), getParams().getRenderSize() /
                        (getSize() - 1) * pos
        );
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "ProgressBar{getString()}"
     */
    @Override
    public String toString() {
        return "ProgressBar{" + getString() + '}';
    }

}
