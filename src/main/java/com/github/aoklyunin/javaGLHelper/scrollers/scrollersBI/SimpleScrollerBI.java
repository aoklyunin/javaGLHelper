package com.github.aoklyunin.javaGLHelper.scrollers.scrollersBI;

import com.github.aoklyunin.javaGLHelper.GLAlgorithms;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import jMath.aoklyunin.github.com.vector.Vector2d;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jogamp.opengl.GL.GL_LINES;
import static java.lang.Math.round;

/**
 * Простой скроллер
 */
public class SimpleScrollerBI {
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
        protected Supplier<BigInteger> getSize;
        /**
         * метод, возвращающий положение скроллера
         */
        @Nullable
        protected Supplier<BigInteger> getPos;
        /**
         * метод, обрабатывающий изменившееся положение скроллера
         */
        @Nullable
        protected Consumer<BigInteger> processChangedScrollerPos;
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
        public T getSize(@NotNull Supplier<BigInteger> getSize) {
            this.getSize = Objects.requireNonNull(getSize);
            return self();
        }

        /**
         * Задать  метод, возвращающий положение скроллера
         *
         * @param getPos метод, возвращающий положение скроллера
         * @return ссылка на самого себя
         */
        public T getPos(@NotNull Supplier<BigInteger> getPos) {
            this.getPos = Objects.requireNonNull(getPos);
            return self();
        }

        /**
         * Задать  метод, обрабатывающий изменившееся положение скроллера
         *
         * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
         * @return ссылка на самого себя
         */
        public T processChangedScrollerPos(@NotNull Consumer<BigInteger> processChangedScrollerPos) {
            this.processChangedScrollerPos = Objects.requireNonNull(processChangedScrollerPos);
            return self();
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */
        abstract SimpleScrollerBI build();

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
            super(scrollerParams);
        }

        /**
         * Построить скроллер
         *
         * @return скроллер
         */

        @Override
        SimpleScrollerBI build() {
            return new SimpleScrollerBI(this);
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
    protected Supplier<BigInteger> getSize;
    /**
     * метод, возвращающий положение скроллера
     */
    @Nullable
    private final Supplier<BigInteger> getPos;
    /**
     * метод, обрабатывающий изменившееся положение скроллера
     */
    @Nullable
    private final Consumer<BigInteger> processChangedScrollerPos;

    /**
     * положение курсора скроллера
     */
    @NotNull
    protected BigInteger cursorPos;
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
    public SimpleScrollerBI(@NotNull AbstractBuilder<?> builder) {
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
    public SimpleScrollerBI(
            @Nullable Supplier<BigInteger> getSize, @Nullable Supplier<BigInteger> getPos,
            @Nullable Consumer<BigInteger> processChangedScrollerPos,
            @NotNull SimpleScrollerParams params
    ) {
        this.getSize = getSize;
        this.getPos = getPos;
        this.processChangedScrollerPos = processChangedScrollerPos;
        this.params = Objects.requireNonNull(params);
    }


    /**
     * Получить скроллер
     *
     * @param scrollerParams            параметры скроллера
     * @param getPos                    метод, возвращающий размер скроллера
     * @param processChangedScrollerPos метод, обрабатывающий изменившееся положение скроллера
     * @return скроллер
     */
    public static SimpleScrollerBI of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<BigInteger> getPos,
            @NotNull Consumer<BigInteger> processChangedScrollerPos
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
    public static SimpleScrollerBI of(
            @NotNull SimpleScrollerParams scrollerParams, @NotNull Supplier<BigInteger> getPos,
            @NotNull Supplier<BigInteger> getSize, @NotNull Consumer<BigInteger> processChangedScrollerPos
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
    protected BigInteger getSize() {
        return getSize.get();
    }

    /**
     * Увеличить положение курсора скроллера
     */
    public void incCursorPos() {
        cursorPos = cursorPos.add(BigInteger.ONE);
        BigInteger size = getSize();
        if (cursorPos.compareTo(size) >= 0)
            cursorPos = size.subtract(BigInteger.ONE);
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Уменьшить положение курсора скроллера
     */
    public void decCursorPos() {
        cursorPos = cursorPos.subtract(BigInteger.ONE);
        if (cursorPos.compareTo(BigInteger.ZERO) < 0)
            cursorPos = BigInteger.ONE;
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Задать положение курсора скроллера
     *
     * @param cursorPos новое положение скроллера курсора
     */
    public void setCursorPos(BigInteger cursorPos) {
        this.cursorPos = cursorPos;
        if (cursorPos.compareTo(BigInteger.ZERO) < 0)
            cursorPos = BigInteger.ONE;
        BigInteger size = getSize();
        if (cursorPos.compareTo(size) >= 0)
            cursorPos = size.subtract(BigInteger.ONE);
        if (processChangedScrollerPos != null)
            processChangedScrollerPos.accept(cursorPos);
    }

    /**
     * Изменить положение курсора скроллера
     *
     * @param delta изменение положения курсора скроллера
     */
    public void changeCursorPos(long delta) {
        setCursorPos(cursorPos.add(BigInteger.valueOf(delta)));
    }


    /**
     * Задать положение курсора скроллера по клику мышью
     *
     * @param mouseGLPos координаты клика мышьью
     * @return флаг, получилось ли изменить положение скроллера(попал ли клик в область его рисования)
     */
    public boolean setScrollerCursorPosByClick(Vector2d mouseGLPos) {
//        int newPos = switch (type) {
//            case HORIZONTAL_SCROLLER -> getHorizontalScrollerCursorPos(mouseGLPos);
//            case VERTICAL_SCROLLER -> getVerticalScrollerPos(mouseGLPos);
//            default -> -1;
//        };
        BigInteger newPos = BigInteger.valueOf(-1);
        switch (params.getType()) {
            case HORIZONTAL:
                newPos = getHorizontalScrollerCursorPos(mouseGLPos);
                break;
            case VERTICAL:
                newPos = getVerticalScrollerPos(mouseGLPos);
                break;
        }
        if (!newPos.equals(BigInteger.valueOf(-1))) {
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
    private BigInteger getHorizontalScrollerCursorPos(Vector2d mouseGLPos) {
        BigInteger scrollerSize = getSize();
        if (mouseGLPos.y > params.getRenderPosMin() && mouseGLPos.y < params.getRenderPosMax()) {
            double kScrollerPos = 1.0f / scrollerSize.doubleValue();
            BigInteger storyPos = BigInteger.valueOf(
                    round((mouseGLPos.x - params.getRenderOffset()) / (params.getRenderSize() * kScrollerPos))
            );
            if (storyPos.compareTo(scrollerSize.subtract(BigInteger.ONE)) > 0)
                storyPos = scrollerSize.subtract(BigInteger.ONE);
            if (storyPos.compareTo(BigInteger.ZERO) < 0)
                storyPos = BigInteger.ZERO;
            return storyPos;
        }
        return BigInteger.valueOf(-1);
    }

    /**
     * Получить положение курсора вертикального скроллера  по клику мышью
     *
     * @param mouseGLPos координаты клика мышьью
     * @return новое положение курсора
     */
    private BigInteger getVerticalScrollerPos(Vector2d mouseGLPos) {
        BigInteger scrollerSize = getSize();
        if (mouseGLPos.x > params.getRenderPosMin() && mouseGLPos.x < params.getRenderPosMax()) {
            double kScrollerPos = 1.0f / scrollerSize.doubleValue();
            BigInteger storyPos = BigInteger.valueOf(
                    round((mouseGLPos.y - params.getRenderOffset()) / (params.getRenderSize() * kScrollerPos))
            );
            if (storyPos.compareTo(scrollerSize.subtract(BigInteger.ONE)) > 0)
                storyPos = scrollerSize.subtract(BigInteger.ONE);
            if (storyPos.compareTo(BigInteger.ZERO) < 0)
                storyPos = BigInteger.ZERO;
            return storyPos;
        }
        return BigInteger.valueOf(-1);
    }

    public void renderScroller(GL2 gl2) {
        renderScroller(gl2, getPos == null ? cursorPos : getPos.get());
    }

    /**
     * Рисование скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    public void renderScroller(GL2 gl2, BigInteger pos) {
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
    protected void renderHorizontalScroller(GL2 gl2, BigInteger pos) {
        BigInteger size = getSize();
        gl2.glColor3d(0.9, 0.9, 0.9);
        // ширина одного шага скроллера
        double kScrollerPos = params.getRenderSize() / size.doubleValue();
        // получаем кол-во шагов скроллера между большими палочками
        BigInteger divide = size.divide(BigInteger.valueOf(params.getDivideCoeff()));
        // если кол-во шагов больше ноля
        if (divide.compareTo(BigInteger.ZERO) > 0) {
            gl2.glBegin(GL_LINES);
            for (BigInteger i = BigInteger.ONE; i.compareTo(size) < 0; i = i.add(BigInteger.ONE)) {
                if (i.mod(divide).equals(BigInteger.valueOf(0))) {
                    gl2.glVertex2d(params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMin());
                    gl2.glVertex2d(params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMax());
                } else {
                    gl2.glVertex2d(
                            params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMin() + 0.01
                    );
                    gl2.glVertex2d(
                            params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMax() - 0.01
                    );
                }
            }
            gl2.glEnd();
        } else {
            gl2.glBegin(GL_LINES);
            for (BigInteger i = BigInteger.ONE; i.compareTo(size) < 0; i = i.add(BigInteger.ONE)) {
                gl2.glVertex2d(params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMin());
                gl2.glVertex2d(params.getRenderOffset() + i.doubleValue() * kScrollerPos, params.getRenderPosMax());
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
                        params.getRenderOffset() + params.getRenderSize() * pos.divide(size).doubleValue() -
                                cursorSize.x / 2, params.getRenderPosMin()
                ),
                cursorSize,
                0.005
        );
    }

    /**
     * Рисование вертикального скроллера
     *
     * @param gl2 переменная OpenGL
     * @param pos положение скроллера
     */
    protected void renderVerticalScroller(GL2 gl2, BigInteger pos) {
        BigInteger size = getSize();
        gl2.glColor3d(0.9, 0.9, 0.9);
        // ширина одного шага скроллера
        double kScrollerPos = params.getRenderSize() / size.doubleValue();
        // получаем кол-во шагов скроллера между большими палочками
        BigInteger divide = size.divide(BigInteger.valueOf(params.getDivideCoeff()));
        // если кол-во шагов больше ноля
        if (divide.compareTo(BigInteger.ZERO) > 0) {
            gl2.glBegin(GL_LINES);
            for (BigInteger i = BigInteger.ONE; i.compareTo(size) < 0; i = i.add(BigInteger.ONE)) {
                if (i.mod(divide).equals(BigInteger.valueOf(0))) {
                    gl2.glVertex2d(params.getRenderPosMin(), params.getRenderOffset() + i.doubleValue() * kScrollerPos);
                    gl2.glVertex2d(params.getRenderPosMax(), params.getRenderOffset() + i.doubleValue() * kScrollerPos);
                } else {
                    gl2.glVertex2d(params.getRenderPosMin() + 0.01, params.getRenderOffset() + i.doubleValue() * kScrollerPos);
                    gl2.glVertex2d(params.getRenderPosMax() - 0.01, params.getRenderOffset() + i.doubleValue() * kScrollerPos);
                }
            }
            gl2.glEnd();
        } else {
            gl2.glBegin(GL_LINES);
            for (BigInteger i = BigInteger.ONE; i.compareTo(size) < 0; i = i.add(BigInteger.ONE)) {
                gl2.glVertex2d(params.getRenderPosMin(), params.getRenderOffset() + i.doubleValue() * kScrollerPos);
                gl2.glVertex2d(params.getRenderPosMax(), params.getRenderOffset() + i.doubleValue() * kScrollerPos);
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
                        params.getRenderOffset() + pos.divide(size).doubleValue() - cursorSize.y / 2
                ),
                cursorSize,
                0.005
        );
    }

    /**
     * Получить положение курсора скроллера
     *
     * @return положение курсора скроллера
     */
    public BigInteger getCursorPos() {
        return cursorPos;
    }

    /**
     * Получить  параметры скроллера
     *
     * @return параметры скроллера
     */
    public SimpleScrollerParams getParams() {
        return params;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "SimpleScrollerBI{getString()}"
     */
    @Override
    public String toString() {
        return "SimpleScrollerBI{" + getString() + '}';
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

        SimpleScrollerBI that = (SimpleScrollerBI) o;

        if (!Objects.equals(getSize, that.getSize)) return false;
        if (!Objects.equals(getPos, that.getPos)) return false;
        if (!Objects.equals(processChangedScrollerPos, that.processChangedScrollerPos))
            return false;
        if (!Objects.equals(cursorPos, that.cursorPos)) return false;
        if (!Objects.equals(onChangePosByMouseClick, that.onChangePosByMouseClick))
            return false;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = getSize != null ? getSize.hashCode() : 0;
        result = 31 * result + (getPos != null ? getPos.hashCode() : 0);
        result = 31 * result + (processChangedScrollerPos != null ? processChangedScrollerPos.hashCode() : 0);
        result = 31 * result + (cursorPos != null ? cursorPos.hashCode() : 0);
        result = 31 * result + (onChangePosByMouseClick != null ? onChangePosByMouseClick.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }
}
