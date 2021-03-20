package com.github.aoklyunin.javaGLHelper.scrollers.scrollers;

import com.github.aoklyunin.javaGLHelper.GLAlgorithms;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import jMath.aoklyunin.github.com.vector.Vector2d;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jogamp.opengl.GL.GL_LINES;
import static java.lang.Math.round;

/**
 * Простой скроллер
 */
public class SimpleScroller {
    /**
     * Абстрактный строитель всех скроллеров является обобщенным типом с
     * рекурсивным параметром типа (раздел 5.5). Это, наряду с абстрактным методом self,
     * обеспечивает корректную работу цепочек методов в подклассах
     * без необходимости приведения типов. Этот обходной путь для того факта, что
     * в Java нет “типа самого себя” (или “собственного типа”), известен как идиома
     * имитации собственного типа.
     *
     * @param <T> тип строителя
     */
    static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        /**
         * метод, возвращающий размер скроллера
         */
        @Nullable
        protected Supplier<Long> getSize;
        /**
         * метод, возвращающий положение скроллера
         */
        @Nullable
        protected Supplier<Long> getPos;
        /**
         * метод, обрабатывающий изменившееся положение скроллера
         */
        @Nullable
        protected Consumer<Long> processChangedScrollerPos;
        /**
         * параметры скроллера
         */
        @NotNull
        protected SimpleScrollerParams params;

        /**
         * Конструктор строителя
         *
         * @param scrollerParams параметры скроллера
         */
        public AbstractBuilder(@NotNull SimpleScrollerParams scrollerParams) {
            this.params = Objects.requireNonNull(scrollerParams);
        }

        /**
         * Задать метод, возвращающий размер скроллера
         *
         * @param getSize метод, возвращающий размер скроллера
         * @return ссылка на самого себя
         */
        public T getSize(@NotNull Supplier<Long> getSize) {
            this.getSize = Objects.requireNonNull(getSize);
            return self();
        }

        /**
         * Задать  метод, возвращающий положение скроллера
         *
         * @param getPos метод, возвращающий положение скроллера
         * @return ссылка на самого себя
         */
        public T getPos(@NotNull Supplier<Long> getPos) {
            this.getPos = Objects.requireNonNull(getPos);
            return self();
        }

        /**
         * Задать  метод, обрабатывающий изменившееся положение скроллера
         *
         * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
         * @return ссылка на самого себя
         */
        public T processChangedScrollerPos(@NotNull Consumer<Long> processChangedScrollerPos) {
            this.processChangedScrollerPos = Objects.requireNonNull(processChangedScrollerPos);
            return self();
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */
        abstract SimpleScroller build();

        /**
         * Подклассы должны перекрывать этот метод, возвращая "себя"
         *
         * @return объект, от которого был совершён вызов
         */
        protected abstract T self();
    }

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
        SimpleScroller build() {
            return new SimpleScroller(this);
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
     * метод, возвращающий размер скроллера
     */
    @Nullable
    protected Supplier<Long> getSize;
    /**
     * метод, возвращающий положение скроллера
     */
    @Nullable
    private final Supplier<Long> getPos;
    /**
     * метод, обрабатывающий изменившееся положение скроллера
     */
    @Nullable
    private final Consumer<Long> processChangedScrollerPos;
    /**
     * положение курсора скроллера
     */
    @Nullable
    protected long cursorPos;
    /**
     * метод, вызывающийся каждый раз, когда меняемтся положение скроллера
     * при помощи клика мыши
     */
    @Nullable
    private Runnable onChangePosByMouseClick;
    /**
     * параметры скроллера
     */
    @NotNull
    private final SimpleScrollerParams params;


    /**
     * Конструктор простого скроллера
     *
     * @param builder строитель
     */
    public SimpleScroller(@NotNull AbstractBuilder<?> builder) {
        this.getSize = builder.getSize;
        this.getPos = builder.getPos;
        this.processChangedScrollerPos = builder.processChangedScrollerPos;
        this.params = builder.params;
    }

    /**
     * Конструктор простого скроллера
     *
     * @param getSize                   метод, возвращающий размер скроллера
     * @param getPos                    метод, возвращающий положение скроллера
     * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
     * @param params                    параметры скроллера
     */
    public SimpleScroller(
            @Nullable Supplier<Long> getSize, @Nullable Supplier<Long> getPos, @Nullable Consumer<Long> processChangedScrollerPos,
            @NotNull SimpleScrollerParams params
    ) {
        this.getSize = getSize;
        this.getPos = getPos;
        this.processChangedScrollerPos = processChangedScrollerPos;
        this.params = params;
    }

    /**
     * Конструктор простого скроллера
     *
     * @param worldStoryScroller скроллер
     */
    public SimpleScroller(@NotNull SimpleScroller worldStoryScroller) {
        this.getSize = worldStoryScroller.getSize;
        this.getPos = worldStoryScroller.getPos;
        this.processChangedScrollerPos = worldStoryScroller.processChangedScrollerPos;
        this.cursorPos = worldStoryScroller.cursorPos;
        this.onChangePosByMouseClick = worldStoryScroller.onChangePosByMouseClick;
        this.params = worldStoryScroller.params;
    }

    /**
     * Получить скроллер
     *
     * @param scrollerParams            параметры скроллера
     * @param getPos                    метод, возвращающий размер скроллера
     * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
     * @return скроллер
     */
    public static SimpleScroller of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<Long> getPos,
            @NotNull Consumer<Long> processChangedScrollerPos
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams)).getPos(Objects.requireNonNull(getPos))
                .processChangedScrollerPos(Objects.requireNonNull(processChangedScrollerPos)).build();
    }

    /**
     * Получить скроллер
     *
     * @param scrollerParams            параметры скроллера
     * @param getPos                    метод, возвращающий положение скроллера
     * @param getSize                   метод, возвращающий размер скроллера
     * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
     * @return скроллер
     */
    public static SimpleScroller of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<Long> getPos,
            @NotNull Supplier<Long> getSize, @NotNull Consumer<Long> processChangedScrollerPos
    ) {
        return new Builder(Objects.requireNonNull(scrollerParams)).getPos(Objects.requireNonNull(getPos))
                .processChangedScrollerPos(Objects.requireNonNull(processChangedScrollerPos))
                .getSize(Objects.requireNonNull(getSize)).build();
    }

    /**
     * Получить размер скроллера
     *
     * @return размер скроллера
     */
    protected long getSize() {
        return getSize.get();
    }

    /**
     * Увеличить положение курсора скроллера
     */
    public void incCursorPos() {
        cursorPos++;
        long size = getSize();
        if (cursorPos >= size)
            cursorPos = size - 1;
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Уменьшить положение курсора скроллера
     */
    public void decCursorPos() {
        cursorPos--;
        if (cursorPos < 0)
            cursorPos = 0;
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Задать положение курсора скроллера
     *
     * @param cursorPos новое положение скроллера курсора
     */
    public void setCursorPos(long cursorPos) {
        this.cursorPos = cursorPos;
        if (this.cursorPos < 0)
            this.cursorPos = 0;
        long size = getSize();
        if (this.cursorPos >= size)
            this.cursorPos = size - 1;
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Изменить положение курсора скроллера
     *
     * @param delta изменение положения курсора скроллера
     */
    public void changeCursorPos(int delta) {
        setCursorPos(cursorPos + delta);
    }


    /**
     * Задать положение курсора скроллера по клику мышью
     *
     * @param mouseGLPos координаты клика мышьью
     * @return флаг, получилось ли изменить положение скроллера(попал ли клик в область его рисования)
     */
    public boolean setScrollerCursorPosByClick(@NotNull Vector2d mouseGLPos) {
//        int newPos = switch (type) {
//            case HORIZONTAL_SCROLLER -> getHorizontalScrollerCursorPos(mouseGLPos);
//            case VERTICAL_SCROLLER -> getVerticalScrollerPos(mouseGLPos);
//            default -> -1;
//        };
        int newPos = -1;
        switch (params.getType()) {
            case HORIZONTAL:
                newPos = getHorizontalScrollerCursorPos(Objects.requireNonNull(mouseGLPos));
                break;
            case VERTICAL:
                newPos = getVerticalScrollerPos(mouseGLPos);
                break;
        }
        if (newPos != -1) {
            cursorPos = newPos;
            if (processChangedScrollerPos != null)
                processChangedScrollerPos.accept(cursorPos);
            if (onChangePosByMouseClick != null)
                onChangePosByMouseClick.run();
            return true;
        }
        return false;
    }

    /**
     * Получить положение курсора горизонтального скроллера  по клику мышью
     *
     * @param mouseGLPos координаты клика мышьью
     * @return новое положение курсора
     */
    private int getHorizontalScrollerCursorPos(@NotNull Vector2d mouseGLPos) {
        long scrollerSize = getSize();
        if (
                mouseGLPos.y > params.getRenderPosMin() - params.getAccuracy() && mouseGLPos.y <
                        params.getRenderPosMax() + params.getAccuracy() &&
                        mouseGLPos.x > params.getRenderOffset() && mouseGLPos.x < params.getRenderOffset() +
                        params.getRenderSize()
        ) {
            double kStoryPos = 1.0f / scrollerSize;
            long kScrollerPos =
                    round((mouseGLPos.x - params.getRenderOffset()) / (params.getRenderSize() * kStoryPos));
            if (kScrollerPos > scrollerSize - 1)
                kScrollerPos = scrollerSize - 1;
            if (kScrollerPos < 0)
                kScrollerPos = 0;
            return (int) kScrollerPos;
        }
        return -1;
    }

    /**
     * Получить положение курсора вертикального скроллера  по клику мышью
     *
     * @param mouseGLPos координаты клика мышьью
     * @return новое положение курсора
     */
    private int getVerticalScrollerPos(@NotNull Vector2d mouseGLPos) {
        long scrollerSize = getSize();
        if (
                mouseGLPos.x > params.getRenderPosMin() - params.getAccuracy() && mouseGLPos.x <
                        params.getRenderPosMax() + params.getAccuracy() &&
                        mouseGLPos.y > params.getRenderOffset() && mouseGLPos.y < params.getRenderOffset() +
                        params.getRenderSize()
        ) {
            double kScrollerPos = 1.0f / scrollerSize;
            long storyPos =
                    round((mouseGLPos.y - params.getRenderOffset()) / (params.getRenderSize() * kScrollerPos));
            if (storyPos > scrollerSize - 1)
                storyPos = scrollerSize - 1;
            if (storyPos < 0)
                storyPos = 0;
            return (int) storyPos;
        }
        return -1;
    }

    /**
     * Рисование скроллера
     *
     * @param gl2 переменная OpenGL
     */
    public void renderScroller(GL2 gl2) {
        // рисуем скроллер для перемещения по истории мира
        gl2.glColor3f(1, 1, 1);
        switch (params.getType()) {
            case HORIZONTAL:
                renderHorizontalScroller(gl2);
                break;
            case VERTICAL:
                renderVerticalScroller(gl2);
                break;
            default:
        }
    }

    /**
     * Рисование скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    public void renderScroller(GL2 gl2, long pos) {
        // рисуем скроллер для перемещения по истории мира
        gl2.glColor3f(1, 1, 1);
        switch (params.getType()) {
            case HORIZONTAL:
                renderHorizontalScroller(gl2, pos);
                break;
            case VERTICAL:
                renderVerticalScroller(gl2, pos);
                break;
            default:
        }
    }

    /**
     * Рисование горизонтального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    protected void renderHorizontalScroller(GL2 gl2, long pos) {
        gl2.glLineWidth(5);
        long size = getSize();
        gl2.glColor3d(0.9, 0.9, 0.9);
        // ширина одного шага скроллера
        double kScrollerPos = params.getRenderSize() / size;
        // получаем кол-во шагов скроллера между большими палочками
        long divide = size / params.getDivideCoeff();
        // если кол-во шагов больше ноля
        if (divide > 0) {
            gl2.glBegin(GL_LINES);
            for (int i = 0; i < size; i++) {
                if (i % divide == 0) {
                    gl2.glVertex2d(params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMin());
                    gl2.glVertex2d(params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMax());
                } else {
                    gl2.glVertex2d(
                            params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMin() + 0.01
                    );
                    gl2.glVertex2d(
                            params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMax() - 0.01
                    );
                }
            }
            gl2.glEnd();
        } else {
            gl2.glBegin(GL_LINES);
            for (int i = 0; i < size; i++) {
                gl2.glVertex2d(params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMin());
                gl2.glVertex2d(params.getRenderOffset() + i * kScrollerPos, params.getRenderPosMax());
            }
            gl2.glEnd();
        }

        // получаем размер курсора
        Vector2d cursorSize = new Vector2d(
                params.getRenderPosMax() - params.getRenderPosMin(),
                params.getRenderPosMax() - params.getRenderPosMin()
        );

        // рисуем курсор
        GLAlgorithms.renderFilledRoundedQuad(
                gl2,
                new Vector2d(
                        params.getRenderOffset() + params.getRenderSize() * pos / size - cursorSize.x / 2,
                        params.getRenderPosMin()
                ),
                cursorSize,
                0.005
        );
        gl2.glLineWidth(1);
    }

    /**
     * Рисование горизонтального скроллера
     *
     * @param gl2 переменная OpenGL
     */
    private void renderHorizontalScroller(GL2 gl2) {
        renderHorizontalScroller(gl2, getPos == null ? cursorPos : getPos.get());
    }


    /**
     * Рисование вертикального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    protected void renderVerticalScroller(GL2 gl2, long pos) {
        gl2.glLineWidth(5);

        long size = getSize();
        gl2.glColor3d(0.9, 0.9, 0.9);
        // ширина одного шага скроллера
        double kScrollerPos = params.getRenderSize() / size;
        // получаем кол-во шагов скроллера между большими палочками
        long divide = size / params.getDivideCoeff();
        // если кол-во шагов больше ноля
        if (divide > 0) {
            gl2.glBegin(GL_LINES);
            for (int i = 0; i < size; i++) {
                if (i % divide == 0) {
                    gl2.glVertex2d(params.getRenderPosMin(), params.getRenderOffset() + i * kScrollerPos);
                    gl2.glVertex2d(params.getRenderPosMax(), params.getRenderOffset() + i * kScrollerPos);
                } else {
                    gl2.glVertex2d(params.getRenderPosMin() + 0.01, params.getRenderOffset() + i * kScrollerPos);
                    gl2.glVertex2d(params.getRenderPosMax() - 0.01, params.getRenderOffset() + i * kScrollerPos);
                }
            }
            gl2.glEnd();
        } else {
            gl2.glBegin(GL_LINES);
            for (int i = 0; i < size; i++) {
                gl2.glVertex2d(params.getRenderPosMin(), params.getRenderOffset() + i * kScrollerPos);
                gl2.glVertex2d(params.getRenderPosMax(), params.getRenderOffset() + i * kScrollerPos);
            }
            gl2.glEnd();
        }

        // получаем размер курсора
        Vector2d cursorSize = new Vector2d(
                params.getRenderPosMax() - params.getRenderPosMin(),
                params.getRenderPosMax() - params.getRenderPosMin()
        );

        // рисуем курсор
        GLAlgorithms.renderFilledRoundedQuad(
                gl2,
                new Vector2d(
                        params.getRenderPosMin(),
                        params.getRenderOffset() + params.getRenderSize() * pos / size - cursorSize.y / 2
                ),
                cursorSize,
                0.005
        );
        gl2.glLineWidth(1);
    }

    /**
     * Рисование вертикального скроллера
     *
     * @param gl2 переменная OpenGL
     */
    private void renderVerticalScroller(GL2 gl2) {
        renderVerticalScroller(gl2, getPos == null ? cursorPos : getPos.get());
    }

    /**
     * Получить положение курсора скроллера
     *
     * @return положение курсора скроллера
     */
    public long getCursorPos() {
        return cursorPos;
    }

    /**
     * Получить  параметры скроллера
     *
     * @return параметры скроллера
     */
    @NotNull
    public SimpleScrollerParams getParams() {
        return params;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "SimpleScroller{getString()}"
     */
    @Override
    public String toString() {
        return "SimpleScroller{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "params, cursorPos"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return params + ", " + cursorPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleScroller that = (SimpleScroller) o;

        if (cursorPos != that.cursorPos) return false;
        if (!Objects.equals(getSize, that.getSize)) return false;
        if (!Objects.equals(getPos, that.getPos)) return false;
        if (!Objects.equals(processChangedScrollerPos, that.processChangedScrollerPos))
            return false;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = getSize != null ? getSize.hashCode() : 0;
        result = 31 * result + (getPos != null ? getPos.hashCode() : 0);
        result = 31 * result + (processChangedScrollerPos != null ? processChangedScrollerPos.hashCode() : 0);
        result = 31 * result + (int) (cursorPos ^ (cursorPos >>> 32));
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }
}
