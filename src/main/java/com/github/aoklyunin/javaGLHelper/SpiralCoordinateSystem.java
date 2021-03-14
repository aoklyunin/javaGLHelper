package com.github.aoklyunin.javaGLHelper;


import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2i;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector2i;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Класс спирально системы координат
 */
public class SpiralCoordinateSystem {

    /**
     * Нарисовать спираль, состоящую из квадратов
     *
     * @param gl2            пременна OpenGL
     * @param renderCS       СК рисования
     * @param quadCnt        кол-во квадратов
     * @param paddingPercent процент отступа
     * @param captions       заголовки квадратов
     * @param textController контроллер текста
     * @param colors         цвета квадратов
     */
    public static void renderSpiral(
            GL2 gl2, @NotNull CoordinateSystem2d renderCS, int quadCnt, double paddingPercent,
            @NotNull List<String> captions, @NotNull GLTextController textController, @NotNull List<Vector3d> colors
    ) {
        CoordinateSystem2i spiralCS = getSpiralCS(quadCnt);
        Vector2d step = renderCS.getSimilarity(spiralCS);
        Vector2d quadSize = Vector2d.mul(step, 1 - paddingPercent);

        consumeSpiral(quadCnt, (Vector2i pos) -> {
            int id = getSpiralNumber(quadCnt, pos);
            gl2.glColor3d(colors.get(id).x, colors.get(id).y, colors.get(id).z);
            Vector2d quadPos = renderCS.getCoords(pos, spiralCS);
            GLAlgorithms.renderFilledQuad(gl2, quadPos, quadSize);
            textController.drawText(
                    captions.get(id),
                    Vector2d.sum(
                            quadPos, Vector2d.mul(quadSize, 0.4)
                    )
            );
        });
    }

    /**
     * Клик по нарисованной спирали
     *
     * @param mousePos       место клика мышью
     * @param renderCS       СК рисования
     * @param quadCnt        кол-во квадратов
     * @param paddingPercent процент отступа
     * @return номер выбранного квадрата, если не попали ни по одному, возвращает -1
     */
    public static int clickSpiral(
            @NotNull Vector2d mousePos, @NotNull CoordinateSystem2d renderCS, int quadCnt, double paddingPercent
    ) {
        AtomicInteger res = new AtomicInteger();
        res.set(-1);
        CoordinateSystem2i spiralCS = getSpiralCS(quadCnt);
        Vector2d step = renderCS.getSimilarity(spiralCS);
        Vector2d quadSize = Vector2d.mul(step, 1 - paddingPercent);

        consumeSpiral(quadCnt, (Vector2i pos) -> {
            Vector2d quadPos = renderCS.getCoords(pos, spiralCS);
            if (GLAlgorithms.checkQuadContains(mousePos, quadPos, quadSize))
                res.set(getSpiralNumber(quadCnt, pos));
        });
        return res.get();
    }

    /**
     * Получить систему координат спирали(минимальные и максимальные координаты)
     *
     * @param cnt кол-во элементов спирали
     * @return система координат спирали
     */
    @NotNull
    private static CoordinateSystem2i getSpiralCS(int cnt) {
        Vector2i min = Vector2i.zeros();
        Vector2i max = Vector2i.zeros();
        consumeSpiral(cnt, (Vector2i pos) -> {
            if (min.x > pos.x) min.x = pos.x;
            if (max.x < pos.x) max.x = pos.x;
            if (min.y > pos.y) min.y = pos.y;
            if (max.y < pos.y) max.y = pos.y;
        });
        return new CoordinateSystem2i(min.x, max.x + 1, min.y, max.y + 1);
    }

    /**
     * Перебрать все элементы спирали в порядке следования
     *
     * @param cnt     кол-во элементов спирали
     * @param consume консумер, которому передаётся координата текущего элемента спирали
     */
    private static void consumeSpiral(int cnt, @NotNull Consumer<Vector2i> consume) {
        int size = 1;
        int x = 0;
        int y = 0;
        int direction = 0;
        int pos = 0;
        for (int i = 0; i < cnt; i++) {
            consume.accept(new Vector2i(x, y));
            switch (direction) {
                case 0:
                    x++;
                    pos++;
                    if (pos >= size) {
                        direction++;
                        pos = 0;
                    }
                    break;
                case 1:
                    y++;
                    pos++;
                    if (pos >= size) {
                        direction++;
                        pos = 0;
                        size++;
                    }
                    break;
                case 2:
                    x--;
                    pos++;
                    if (pos >= size) {
                        direction++;
                        pos = 0;
                    }
                    break;
                case 3:
                    y--;
                    pos++;
                    if (pos >= size) {
                        direction = 0;
                        pos = 0;
                        size++;
                    }
                    break;
            }
        }
    }

    /**
     * Получить номер элемента спирали по его координатам
     *
     * @param cnt    кол-во элементов спирали
     * @param coords координаты спирали
     * @return номер элемента спирали
     */
    private static int getSpiralNumber(int cnt, @NotNull Vector2i coords) {
        AtomicReference<Integer> processCnt = new AtomicReference<>();
        processCnt.set(0);
        AtomicReference<Integer> result = new AtomicReference<>();
        result.set(-1);
        consumeSpiral(cnt, (Vector2i spiralPos) -> {
            if (spiralPos.equals(coords))
                result.set(processCnt.get());
            processCnt.set(processCnt.get() + 1);
        });

        return result.get();
    }

}
