package com.github.aoklyunin.javaGLHelper;

import com.sun.istack.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс консоли OpenGL
 */
public class GLConsole {
    /**
     * массив строк консоли
     */
    @NotNull
    private final String[] linesArr;
    /**
     * массив прозрачности строк консоли
     */
    @NotNull
    private final double[] lineOpacities;
    /**
     * сколько тактов строка ещё должна быть отображена
     */
    @NotNull
    private final int[] renderLineCnts;
    /**
     * контроллер текста
     */
    @NotNull
    private final GLTextController textController;
    /**
     * изначальная прозрачность строки
     */
    private final double startOpacity;
    /**
     * изначальное количество тактов, которые строка должна быть отображена
     */
    private final int renderStartCnt;
    /**
     * максимальное количество линий
     */
    private final int maxLineCnt;

    /**
     * Конструктор консоли
     *
     * @param glConsole консоль
     */
    public GLConsole(@NotNull com.github.aoklyunin.javaGLHelper.GLConsole glConsole) {
        this.linesArr = glConsole.linesArr.clone();
        this.lineOpacities = glConsole.lineOpacities.clone();
        this.renderLineCnts = glConsole.renderLineCnts.clone();
        this.textController = glConsole.textController;
        this.startOpacity = glConsole.startOpacity;
        this.renderStartCnt = glConsole.renderStartCnt;
        this.maxLineCnt = glConsole.maxLineCnt;
    }

    /**
     * Конструктор консоли
     *
     * @param textController контроллер текста
     * @param renderStartCnt значальное количество тактов, которые строка должна быть отображена
     * @param maxLineCnt     максимальное количество линий
     */
    public GLConsole(@NotNull GLTextController textController, int renderStartCnt, int maxLineCnt) {
        this.renderStartCnt = renderStartCnt;
        this.maxLineCnt = maxLineCnt;
        this.textController = Objects.requireNonNull(textController);

        lineOpacities = new double[maxLineCnt];
        renderLineCnts = new int[maxLineCnt];
        startOpacity = textController.getCaptionParams().getColor().w;

        linesArr = new String[maxLineCnt];
        for (int i = 0; i < maxLineCnt; i++)
            linesArr[i] = "";
    }

    /**
     * Нарисовать консоль
     */
    public void render() {
        GLAlgorithms.renderVerticalVector(
                textController,
                linesArr,
                lineOpacities
        );
        for (int i = 0; i < maxLineCnt; i++) {
            renderLineCnts[i]--;
            if (renderLineCnts[i] < renderStartCnt / 2)
                lineOpacities[i] = startOpacity * renderLineCnts[i] / renderStartCnt * 2;

        }
    }

    /**
     * Добавить строку в консоль
     *
     * @param line строка
     */
    public void addLine(@NotNull String line) {
        for (int i = linesArr.length - 1; i > 0; i--) {
            lineOpacities[i] = lineOpacities[i - 1];
            renderLineCnts[i] = renderLineCnts[i - 1];
            linesArr[i] = linesArr[i - 1];
        }

        linesArr[0] = Objects.requireNonNull(line);
        renderLineCnts[0] = renderStartCnt;
        lineOpacities[0] = startOpacity;
    }

    /**
     * Получить изначальная прозрачность строки
     *
     * @return изначальная прозрачность строки
     */
    public double getStartOpacity() {
        return startOpacity;
    }

    /**
     * Получить изначальное количество тактов, которые строка должна быть отображена
     *
     * @return изначальное количество тактов, которые строка должна быть отображена
     */
    public int getRenderStartCnt() {
        return renderStartCnt;
    }

    /**
     * Получить  максимальное количество линий
     *
     * @return максимальное количество линий
     */
    public int getMaxLineCnt() {
        return maxLineCnt;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GLConsole{getString()}"
     */

    @Override
    public String toString() {
        return "GLConsole{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "startOpacity, renderStartCnt, maxLineCnt"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return startOpacity + ", " + renderStartCnt + ", " + maxLineCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.github.aoklyunin.javaGLHelper.GLConsole glConsole = (com.github.aoklyunin.javaGLHelper.GLConsole) o;

        if (Double.compare(glConsole.startOpacity, startOpacity) != 0) return false;
        if (renderStartCnt != glConsole.renderStartCnt) return false;
        if (maxLineCnt != glConsole.maxLineCnt) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(linesArr, glConsole.linesArr)) return false;
        if (!Arrays.equals(lineOpacities, glConsole.lineOpacities)) return false;
        if (!Arrays.equals(renderLineCnts, glConsole.renderLineCnts)) return false;
        return Objects.equals(textController, glConsole.textController);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = Arrays.hashCode(linesArr);
        result = 31 * result + Arrays.hashCode(lineOpacities);
        result = 31 * result + Arrays.hashCode(renderLineCnts);
        result = 31 * result + (textController != null ? textController.hashCode() : 0);
        temp = Double.doubleToLongBits(startOpacity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + renderStartCnt;
        result = 31 * result + maxLineCnt;
        return result;
    }
}
