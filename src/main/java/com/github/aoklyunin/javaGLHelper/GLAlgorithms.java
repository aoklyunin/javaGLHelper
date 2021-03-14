package com.github.aoklyunin.javaGLHelper;

import com.jogamp.opengl.*;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import jMath.aoklyunin.github.com.vector.Vector4d;
import org.joml.Matrix2d;

import java.util.List;
import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.GL2GL3.*;
import static java.lang.Math.*;

/**
 * Вспомогательный класс алгоритмов OpenGL
 */
public class GLAlgorithms {
    /**
     * Нарисовать точку
     *
     * @param gl2 переменная OpenGl для рисования
     * @param pos положение
     */
    public static void renderPoint(GL2 gl2, @NotNull Vector2d pos) {
        gl2.glBegin(GL_POINTS);
        gl2.glPointSize(5);
        gl2.glVertex2d(pos.x, pos.y);
        gl2.glPointSize(1);
        gl2.glEnd();
    }

    /**
     * Вывести вертикальный вектор строк
     *
     * @param textController контроллер текста
     * @param lines          строки
     * @param colors         цвета строк
     */
    public static void renderVerticalVector(
            @NotNull GLTextController textController, @NotNull List<String> lines, @NotNull List<Vector3d> colors
    ) {
        renderVerticalVector(
                Objects.requireNonNull(textController), textController.getCaptionParams().getPos(),
                textController.getCaptionParams().getStep(), lines, Objects.requireNonNull(colors)
        );
    }

    /**
     * Вывести вертикальный вектор строк
     *
     * @param textController контроллер текста
     * @param pos            положение первой строки
     * @param step           шаг между строками
     * @param lines          строки
     * @param colors         цвета строк
     */
    public static void renderVerticalVector(
            @NotNull GLTextController textController, @NotNull Vector2d pos, double step, @NotNull List<String> lines,
            @NotNull List<Vector3d> colors
    ) {
        pos = new Vector2d(Objects.requireNonNull(pos));
        pos.y += step / 3;
        for (int i = 0; i < colors.size(); i++) {
            textController.drawText(lines.get(i), pos, colors.get(i));
            pos.y += step;
        }
    }

    /**
     * Вывести вертикальный вектор строк
     *
     * @param textController еолнтроллер текста
     * @param lines          строки
     * @param opacities      массив прозрачнотей строк
     */
    public static void renderVerticalVector(
            @NotNull GLTextController textController, @NotNull String[] lines, @NotNull double[] opacities
    ) {
        Vector2d pos = new Vector2d(textController.getCaptionParams().getPos());
        pos.y += textController.getCaptionParams().getStep() / 3;
        for (int i = 0; i < lines.length; i++) {
            textController.drawText(lines[i], pos, new Vector4d(
                    textController.getCaptionParams().getColor().x,
                    textController.getCaptionParams().getColor().y,
                    textController.getCaptionParams().getColor().z,
                    opacities[i])
            );
            pos.y += textController.getCaptionParams().getStep();
        }
    }

    /**
     * Вывести вертикальный вектор строк
     *
     * @param textController контроллер текста
     * @param lines          строки
     */
    public static void renderVerticalVector(@NotNull GLTextController textController, @NotNull List<String> lines) {
        Vector2d pos = new Vector2d(textController.getCaptionParams().getPos());
        pos.y += textController.getCaptionParams().getStep() / 3;
        for (String value : lines) {
            textController.drawText(value, pos);
            pos.y += textController.getCaptionParams().getStep();
        }
    }

    /**
     * Нарисовать круг
     *
     * @param gl2          переменная OpenGL
     * @param center       координаты центра
     * @param r            радиус
     * @param num_segments кол-во сегментов
     */
    public static void renderFilledCircle(@NotNull GL2 gl2, @NotNull Vector2d center, double r, int num_segments) {
        renderFilledCircle(gl2, center.x, center.y, r, r, num_segments);
    }

    /**
     * Нарисовать круг
     *
     * @param gl2          переменная OpenGL
     * @param center       координаты центра
     * @param size         размеры круга вдоль осей
     * @param num_segments кол-во сегментов
     */
    public static void renderFilledCircle(
            @NotNull GL2 gl2, @NotNull Vector2d center, @NotNull Vector2d size, int num_segments
    ) {
        renderFilledCircle(gl2, center.x, center.y, size.x, size.y, num_segments);
    }

    /**
     * Нарисовать круг
     *
     * @param gl2          переменная OpenGL
     * @param cx           X координата центра круга
     * @param cy           Y координата центра круга
     * @param rx           размер курга вдоль оси X
     * @param ry           размер круга вдоль оси Y
     * @param num_segments кол-во сегментов
     */
    public static void renderFilledCircle(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry, int num_segments
    ) {
        gl2.glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < num_segments; i++) {
            double theta = 2 * PI * i / num_segments;
            double x = rx * cos(theta);
            double y = ry * sin(theta);
            gl2.glVertex2d(x + cx, y + cy);
        }
        gl2.glEnd();
    }

    /**
     * Нарисовать квадрат
     *
     * @param gl2  переменная OpenGL
     * @param pos  координаты левой нижней вершины
     * @param size размеры квадрата вдоль осей
     */
    public static void renderFilledQuad(@NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size) {
        renderFilledQuad(gl2, pos.x, pos.y, size.x, size.y);
    }

    /**
     * Нарисовать квадрат
     *
     * @param gl2 переменная OpenGL
     * @param cx  X координата левой нижней вершины
     * @param cy  Y координата левой нижней вершины
     * @param rx  размер квадрата вдоль оси X
     * @param ry  размер квадрата вдоль оси Y
     */
    public static void renderFilledQuad(@NotNull GL2 gl2, double cx, double cy, double rx, double ry) {
        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(cx, cy);
        gl2.glVertex2d(cx, cy + ry);
        gl2.glVertex2d(cx + rx, cy + ry);
        gl2.glVertex2d(cx + rx, cy);
        gl2.glEnd();
    }

    /**
     * Нарисовать рамку
     *
     * @param gl2        переменная OpenGL
     * @param pos        координаты левой нижней вершины
     * @param size       размеры квадрата вдоль осей
     * @param borderSize размер рамки
     */
    public static void renderBorderQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, @NotNull Vector2d borderSize
    ) {
        renderBorderQuad(gl2, pos.x, pos.y, size.x, size.y, borderSize.x, borderSize.y);
    }

    /**
     * Нарисовать рамку
     *
     * @param gl2     переменная OpenGL
     * @param cx      X координата левой нижней вершины
     * @param cy      Y координата левой нижней вершины
     * @param rx      размер квадрата вдоль оси X
     * @param ry      размер квадрата вдоль оси Y
     * @param borderx x размер рамки
     * @param bordery y размер рамки
     */
    public static void renderBorderQuad(
            GL2 gl2, double cx, double cy, double rx, double ry, double borderx, double bordery
    ) {
        renderUpBorderQuad(gl2, cx, cy, rx, ry, borderx, bordery);
        renderDownBorderQuad(gl2, cx, cy, rx, ry, borderx, bordery);
        renderLeftBorderQuad(gl2, cx, cy, rx, ry, borderx, bordery);
        renderRightBorderQuad(gl2, cx, cy, rx, ry, borderx, bordery);
    }


    /**
     * Нарисовать верхнюю рамку
     *
     * @param gl2        переменная OpenGL
     * @param pos        координаты левой нижней вершины
     * @param size       размеры квадрата вдоль осей
     * @param borderSize размер рамки
     */
    public static void renderUpBorderQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, @NotNull Vector2d borderSize
    ) {
        renderUpBorderQuad(gl2, pos.x, pos.y, size.x, size.y, borderSize.x, borderSize.y);
    }

    /**
     * Нарисовать верхнюю рамку
     *
     * @param gl2     переменная OpenGL
     * @param cx      X координата левой нижней вершины
     * @param cy      Y координата левой нижней вершины
     * @param rx      размер квадрата вдоль оси X
     * @param ry      размер квадрата вдоль оси Y
     * @param borderx x размер рамки
     * @param bordery y размер рамки
     */
    public static void renderUpBorderQuad(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry, double borderx, double bordery
    ) {
        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(cx, cy + ry);
        gl2.glVertex2d(cx, cy + ry - bordery);
        gl2.glVertex2d(cx + rx, cy + ry - bordery);
        gl2.glVertex2d(cx + rx, cy + ry);
        gl2.glEnd();
    }

    /**
     * Нарисовать нижнюю рамку
     *
     * @param gl2        переменная OpenGL
     * @param pos        координаты левой нижней вершины
     * @param size       размеры квадрата вдоль осей
     * @param borderSize размер рамки
     */
    public static void renderDownBorderQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, @NotNull Vector2d borderSize
    ) {
        renderDownBorderQuad(gl2, pos.x, pos.y, size.x, size.y, borderSize.x, borderSize.y);
    }

    /**
     * Нарисовать нижнюю рамку
     *
     * @param gl2     переменная OpenGL
     * @param cx      X координата левой нижней вершины
     * @param cy      Y координата левой нижней вершины
     * @param rx      размер квадрата вдоль оси X
     * @param ry      размер квадрата вдоль оси Y
     * @param borderx x размер рамки
     * @param bordery y размер рамки
     */
    public static void renderDownBorderQuad(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry, double borderx, double bordery
    ) {
        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(cx, cy);
        gl2.glVertex2d(cx, cy + bordery);
        gl2.glVertex2d(cx + rx, cy + bordery);
        gl2.glVertex2d(cx + rx, cy);
        gl2.glEnd();
    }

    /**
     * Нарисовать левую рамку
     *
     * @param gl2     переменная OpenGL
     * @param cx      X координата левой нижней вершины
     * @param cy      Y координата левой нижней вершины
     * @param rx      размер квадрата вдоль оси X
     * @param ry      размер квадрата вдоль оси Y
     * @param borderx x размер рамки
     * @param bordery y размер рамки
     */
    public static void renderLeftBorderQuad(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry, double borderx, double bordery
    ) {
        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(cx, cy + bordery);
        gl2.glVertex2d(cx, cy + ry - bordery);
        gl2.glVertex2d(cx + borderx, cy + ry - bordery);
        gl2.glVertex2d(cx + borderx, cy + bordery);
        gl2.glEnd();
    }

    /**
     * Нарисовать левую рамку
     *
     * @param gl2        переменная OpenGL
     * @param pos        координаты левой нижней вершины
     * @param size       размеры квадрата вдоль осей
     * @param borderSize размер рамки
     */
    public static void renderLeftBorderQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, @NotNull Vector2d borderSize
    ) {
        renderLeftBorderQuad(gl2, pos.x, pos.y, size.x, size.y, borderSize.x, borderSize.y);
    }

    /**
     * Нарисовать правую рамку
     *
     * @param gl2     переменная OpenGL
     * @param cx      X координата левой нижней вершины
     * @param cy      Y координата левой нижней вершины
     * @param rx      размер квадрата вдоль оси X
     * @param ry      размер квадрата вдоль оси Y
     * @param borderx x размер рамки
     * @param bordery y размер рамки
     */
    public static void renderRightBorderQuad(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry, double borderx, double bordery
    ) {
        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(cx + rx, cy + bordery);
        gl2.glVertex2d(cx + rx, cy + ry - bordery);
        gl2.glVertex2d(cx + rx - borderx, cy + ry - bordery);
        gl2.glVertex2d(cx + rx - borderx, cy + bordery);
        gl2.glEnd();
    }


    /**
     * Нарисовать правую рамку
     *
     * @param gl2        переменная OpenGL
     * @param pos        координаты левой нижней вершины
     * @param size       размеры квадрата вдоль осей
     * @param borderSize размер рамки
     */
    public static void renderRightBorderQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, @NotNull Vector2d borderSize
    ) {
        renderRightBorderQuad(gl2, pos.x, pos.y, size.x, size.y, borderSize.x, borderSize.y);
    }

    /**
     * Нарисовать квадрат линиями
     *
     * @param gl2 переменная OpenGL
     * @param cx  X координата левой нижней вершины
     * @param cy  Y координата левой нижней вершины
     * @param rx  размер квадрата вдоль оси X
     * @param ry  размер квадрата вдоль оси Y
     */
    public static void renderLineQuad(
            @NotNull GL2 gl2, double cx, double cy, double rx, double ry
    ) {
        gl2.glBegin(GL_LINE_STRIP);
        gl2.glVertex2d(cx, cy);
        gl2.glVertex2d(cx, cy + ry);
        gl2.glVertex2d(cx + rx, cy + ry);
        gl2.glVertex2d(cx + rx, cy);
        gl2.glVertex2d(cx, cy);
        gl2.glEnd();
    }

    /**
     * Нарисовать квадрат линиями
     *
     * @param gl2 переменная OpenGL
     * @param c   координата левой нижней вершины
     * @param r   размер квадрата
     */
    public static void renderLineQuad(@NotNull GL2 gl2, @NotNull Vector2d c, @NotNull Vector2d r) {
        gl2.glBegin(GL_LINE_STRIP);
        gl2.glVertex2d(c.x, c.y);
        gl2.glVertex2d(c.x, c.y + r.y);
        gl2.glVertex2d(c.x + r.x, c.y + r.y);
        gl2.glVertex2d(c.x + r.x, c.y);
        gl2.glVertex2d(c.x, c.y);
        gl2.glEnd();
    }

    /**
     * Нарисовать квадрат линиями
     *
     * @param gl2 переменная OpenGL
     * @param c   координата левой нижней вершины
     * @param r   размер квадрата
     */
    public static void renderLineQuad(@NotNull GL2 gl2, @NotNull Vector3d c, @NotNull Vector3d r) {
        gl2.glBegin(GL_LINE_STRIP);
        gl2.glVertex3d(c.x, c.y, c.z);
        gl2.glVertex3d(c.x, c.y + r.y, c.z + r.z);
        gl2.glVertex3d(c.x + r.x, c.y + r.y, c.z + r.z);
        gl2.glVertex3d(c.x + r.x, c.y, c.z);
        gl2.glVertex3d(c.x, c.y, c.z);
        gl2.glEnd();
    }

    /**
     * Нарисовать центрированный квадрат
     *
     * @param gl2  переменная OpenGL
     * @param pos  координаты левой нижней вершины
     * @param size размеры квадрата вдоль осей
     */
    public static void renderCenteredQuad(@NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size) {
        renderFilledQuad(gl2, pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
    }

    /**
     * Нарисовать квадрат линиями
     *
     * @param gl2  переменная OpenGL
     * @param pos  координаты левой нижней вершины
     * @param size размеры квадрата вдоль осей
     */
    public static void renderCenteredLineQuad(@NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size) {
        renderLineQuad(gl2, pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
    }

    /**
     * Проверить, что точка находится внутри квадрата
     *
     * @param point координаты точки
     * @param pos   координаты левой нижней вершины
     * @param size  размеры квадрата вдоль осей
     * @return флаг, находится ли точка внутри квадрата
     */
    public static boolean checkQuadContains(@NotNull Vector2d point, @NotNull Vector2d pos, @NotNull Vector2d size) {
        return (
                point.x >= pos.x && point.x <= pos.x + size.x &&
                        point.y >= pos.y && point.y <= pos.y + size.y
        );
    }

    /**
     * Нарисовать скруглённый квадрат
     *
     * @param gl2  переменная OpenGL
     * @param pos  координаты левой нижней вершины
     * @param size размеры квадрата вдоль осей
     * @param rad  радиус скругления
     */
    public static void renderFilledRoundedQuad(@NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, double rad) {
        renderFilledRoundedQuad(gl2, pos, size, rad, 0);
    }

    /**
     * Нарисовать скруглённый квадрат
     *
     * @param gl2     переменная OpenGL
     * @param pos     координаты левой нижней вершины
     * @param size    размеры квадрата вдоль осей
     * @param rad     радиус скругления
     * @param zOffset смещение по оси Z
     */
    public static void renderFilledRoundedQuad(
            @NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, double rad, double zOffset
    ) {
        gl2.glBegin(GL_TRIANGLE_FAN);
        gl2.glVertex3d(pos.x + size.x / 2, pos.y + size.y / 2, zOffset);
        gl2.glVertex3d(pos.x, pos.y + size.y - rad, zOffset);
        gl2.glVertex3d(pos.x, pos.y + rad, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(pos.x + rad * (1 - cos(i / 20 * PI)), pos.y + rad * (1 - sin(i / 20 * PI)), zOffset);
        }

        gl2.glVertex3d(pos.x + size.x - rad, pos.y, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + size.x + rad * (sin(i / 20 * PI) - 1), pos.y + rad * (1 - cos(i / 20 * PI)), zOffset
            );
        }

        gl2.glVertex3d(pos.x + size.x, pos.y + size.y - rad, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + size.x + rad * (cos(i / 20 * PI) - 1),
                    pos.y + size.y + rad * (sin(i / 20 * PI) - 1),
                    zOffset
            );
        }

        gl2.glVertex3d(pos.x + rad, pos.y + size.y, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + rad * (1 - sin(i / 20 * PI)), pos.y + size.y + rad * (cos(i / 20 * PI) - 1), zOffset
            );
        }

        gl2.glVertex3d(pos.x, pos.y + size.y - rad, zOffset);
        gl2.glEnd();
    }

    /**
     * Нарисовать скруглённый квадрат линиями
     *
     * @param gl2  переменная OpenGL
     * @param pos  координаты левой нижней вершины
     * @param size размеры квадрата вдоль осей
     * @param rad  радиус скругления
     */
    public static void renderLineRoundedQuad(@NotNull GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, double rad) {
        renderLineRoundedQuad(gl2, Objects.requireNonNull(pos), Objects.requireNonNull(size), rad, 0.0);
    }

    /**
     * Нарисовать скруглённый квадрат линиями
     *
     * @param gl2     переменная OpenGL
     * @param pos     координаты левой нижней вершины
     * @param size    размеры квадрата вдоль осей
     * @param rad     радиус скругления
     * @param zOffset смещение по оси Z
     */
    public static void renderLineRoundedQuad(GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d size, double rad, double zOffset) {
        gl2.glBegin(GL_LINE_STRIP);
        gl2.glVertex3d(pos.x, pos.y + size.y - rad, zOffset);
        gl2.glVertex3d(pos.x, pos.y + rad, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(pos.x + rad * (1 - cos(i / 20 * PI)), pos.y + rad * (1 - sin(i / 20 * PI)), zOffset);
        }

        gl2.glVertex3d(pos.x + size.x - rad, pos.y, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + size.x + rad * (sin(i / 20 * PI) - 1), pos.y + rad * (1 - cos(i / 20 * PI)), zOffset
            );
        }

        gl2.glVertex3d(pos.x + size.x, pos.y + size.y - rad, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + size.x + rad * (cos(i / 20 * PI) - 1),
                    pos.y + size.y + rad * (sin(i / 20 * PI) - 1),
                    zOffset
            );
        }

        gl2.glVertex3d(pos.x + rad, pos.y + size.y, zOffset);

        for (double i = 0; i < 10; i++) {
            gl2.glVertex3d(
                    pos.x + rad * (1 - sin(i / 20 * PI)), pos.y + size.y + rad * (cos(i / 20 * PI) - 1), zOffset
            );
        }

        gl2.glVertex3d(pos.x, pos.y + size.y - rad, zOffset);
        gl2.glEnd();
    }

    /**
     * Получить точку пересечения линии и скруглённого квадрата
     *
     * @param posA первая точка, определяющая прямую
     * @param posB вторая точка, определяющая прямую
     * @param size размеры квадрата вдоль осей
     * @param rad  радиус скругления
     * @return точка пересечения линии и скруглённого квадрата
     */
    public static Vector2d getRoundedQuadCrossLinePoint(
            @NotNull Vector2d posA, @NotNull Vector2d posB, @NotNull Vector2d size, double rad
    ) {
        Vector2d delta = Vector2d.subtract(Objects.requireNonNull(posB), Objects.requireNonNull(posA));
        double angle = atan2(delta.y, delta.x);
        if (angle < 0)
            angle += 2 * PI;

        // опорные углы
        double[] reperAngles = new double[]{
                atan2(size.y / 2 - rad, size.x / 2),
                atan2(size.y / 2, size.x / 2 - rad),
                atan2(size.y / 2, -size.x / 2 + rad),
                atan2(size.y / 2 - rad, -size.x / 2),
                2 * PI + atan2(-size.y / 2 + rad, -size.x / 2),
                2 * PI + atan2(-size.y / 2, -size.x / 2 + rad),
                2 * PI + atan2(-size.y / 2, size.x / 2 - rad),
                2 * PI + atan2(-size.y / 2 + rad, size.x / 2),
        };

        Vector2d res = Vector2d.zeros();
        if (angle < reperAngles[0] || angle > reperAngles[7]) {
            res = getLineLineIntersection(
                    Vector2d.zeros(),
                    delta,
                    new Vector2d(size.x / 2, -size.y / 2 + rad),
                    new Vector2d(size.x / 2, size.y / 2 - rad)
            );
        } else if (angle <= reperAngles[1]) {
            res = getCircleVectorIntersction(delta.x, delta.y, size.x / 2 - rad, size.y / 2 - rad, rad);
        } else if (angle <= reperAngles[2]) {
            res = getLineLineIntersection(
                    Vector2d.zeros(),
                    delta,
                    new Vector2d(size.x / 2 - rad, size.y / 2),
                    new Vector2d(-size.x / 2 + rad, size.y / 2)
            );
        } else if (angle <= reperAngles[3]) {
            res = getCircleVectorIntersction(delta.x, delta.y, -size.x / 2 + rad, size.y / 2 - rad, rad);
        } else if (angle <= reperAngles[4]) {
            res = getLineLineIntersection(
                    Vector2d.zeros(),
                    delta,
                    new Vector2d(-size.x / 2, size.y / 2 - rad),
                    new Vector2d(-size.x / 2, -size.y / 2 + rad)
            );
        } else if (angle <= reperAngles[5]) {
            res = getCircleVectorIntersction(delta.x, delta.y, -size.x / 2 + rad, -size.y / 2 + rad, rad);
        } else if (angle <= reperAngles[6]) {
            res = getLineLineIntersection(
                    Vector2d.zeros(),
                    delta,
                    new Vector2d(size.x / 2 - rad, -size.y / 2),
                    new Vector2d(-size.x / 2 + rad, -size.y / 2)
            );
        } else if (angle <= reperAngles[7]) {
            res = getCircleVectorIntersction(delta.x, delta.y, size.x / 2 - rad, -size.y / 2 + rad, rad);
        }

        return Vector2d.sum(res, posA);
    }

    /**
     * Получить точку пересечения двух прямых
     *
     * @param p1 первая точка, определяющая первую прямую
     * @param p2 вторая точка, определяющая первую прямую
     * @param p3 первая точка, определяющая вторую прямую
     * @param p4 вторая точка, определяющая вторую прямую
     * @return точка пересечения двух прямых
     */
    private static Vector2d getLineLineIntersection(
            @NotNull Vector2d p1, @NotNull Vector2d p2, @NotNull Vector2d p3, @NotNull Vector2d p4
    ) {
        double div = new Matrix2d(
                new Matrix2d(p1.x, 1, p2.x, 1).determinant(),
                new Matrix2d(p1.y, 1, p2.y, 1).determinant(),
                new Matrix2d(p3.x, 1, p4.x, 1).determinant(),
                new Matrix2d(p3.y, 1, p4.y, 1).determinant()
        ).determinant();

        return new Vector2d(
                new Matrix2d(
                        new Matrix2d(p1.x, p1.y, p2.x, p2.y).determinant(),
                        new Matrix2d(p1.x, 1, p2.x, 1).determinant(),
                        new Matrix2d(p3.x, p3.y, p4.x, p4.y).determinant(),
                        new Matrix2d(p3.x, 1, p4.x, 1).determinant()
                ).determinant() / div,
                new Matrix2d(
                        new Matrix2d(p1.x, p1.y, p2.x, p2.y).determinant(),
                        new Matrix2d(p1.y, 1, p2.y, 1).determinant(),
                        new Matrix2d(p3.x, p3.y, p4.x, p4.y).determinant(),
                        new Matrix2d(p3.y, 1, p4.y, 1).determinant()
                ).determinant() / div
        );
    }

    /**
     * Получить точку пересечения веткора с кругом
     *
     * @param x0 X координата вектора
     * @param y0 Y координата вектора
     * @param xR X координата цетра круга
     * @param yR Y координата центра круга
     * @param R  радиус круга
     * @return точка пересечения веткора с кругом
     */
    private static Vector2d getCircleVectorIntersction(double x0, double y0, double xR, double yR, double R) {
        double a = x0 * x0 + y0 * y0;
        double b = -2 * xR * x0 - 2 * yR * y0;
        double c = xR * xR + yR * yR - R * R;
        double D = b * b - 4 * a * c;
        double t1 = (-b - sqrt(D)) / (2 * a);
        double t2 = (-b + sqrt(D)) / (2 * a);
        double x;
        if (x0 > 0)
            x = max(x0 * t1, x0 * t2);
        else
            x = min(x0 * t1, x0 * t2);
        double y = y0 / x0 * x;
        return new Vector2d(x, y);
    }

    /**
     * Нарисовать стрелку
     *
     * @param gl2       переменная OpenGL
     * @param lineWidth ширина линии стрелки
     * @param coneSize  размер конуса стрелки
     * @param pos       положение, из которого выходим стрелка
     * @param dir       вектор направления стрелки
     */
    public static void renderArrow(
            GL2 gl2, double lineWidth, double coneSize, @NotNull Vector2d pos, @NotNull Vector2d dir
    ) {
        Vector2d left = new Vector2d(-dir.y, dir.x).norm();
        Vector2d right = new Vector2d(dir.y, -dir.x).norm();

        Vector2d lineLeft = Vector2d.mul(left, lineWidth / 2);
        Vector2d lineRight = Vector2d.mul(right, lineWidth / 2);
        Vector2d coneDir = dir.norm(coneSize);

        gl2.glBegin(GL_QUADS);
        gl2.glVertex2d(pos.x + lineLeft.x, pos.y + lineLeft.y);
        gl2.glVertex2d(dir.x + pos.x + lineLeft.x - coneDir.x, dir.y + pos.y + lineLeft.y - coneDir.y);
        gl2.glVertex2d(dir.x + pos.x + lineRight.x - coneDir.x, dir.y + pos.y + lineRight.y - coneDir.y);
        gl2.glVertex2d(pos.x + lineRight.x, pos.y + lineRight.y);
        gl2.glEnd();

        Vector2d coneLeft = Vector2d.mul(left, coneSize / 2);
        Vector2d coneRight = Vector2d.mul(right, coneSize / 2);


        gl2.glBegin(GL_TRIANGLES);
        gl2.glVertex2d(dir.x + pos.x + coneLeft.x - coneDir.x, dir.y + pos.y + coneLeft.y - coneDir.y);
        gl2.glVertex2d(dir.x + pos.x + coneRight.x - coneDir.x, dir.y + pos.y + coneRight.y - coneDir.y);
        gl2.glVertex2d(dir.x + pos.x, dir.y + pos.y);
        gl2.glEnd();

    }

    /**
     * рисуем равнобедренный треугольник с основанием, проходящим через положение коннектора
     * и высотой, совпадающей с вектором направления
     *
     * @param gl2    переменная OpenGL
     * @param pos    положение, из которого выходит треугольник(проходит его основание
     * @param target положение в котором заканчивается треугольник(находится его вершина)
     */
    public static void renderTriangle(GL2 gl2, @NotNull Vector2d pos, @NotNull Vector2d target) {

        Vector2d dir = Vector2d.subtract(Objects.requireNonNull(target), Objects.requireNonNull(pos));
        Vector2d left = Vector2d.mul(new Vector2d(-dir.y, dir.x), 0.5);
        Vector2d right = Vector2d.mul(new Vector2d(dir.y, -dir.x), 0.5);

        gl2.glBegin(GL_TRIANGLES);
        gl2.glVertex2d(pos.x + left.x, pos.y + left.y);
        gl2.glVertex2d(pos.x + right.x, pos.y + right.y);
        gl2.glVertex2d(target.x, target.y);
        gl2.glEnd();
    }

    /**
     * Нарисовать отрезок
     *
     * @param gl2 переменная OpenGL
     * @param A   первая точка отрезка
     * @param B   вторая точка отрезка
     */
    public static void renderLine(GL2 gl2, @NotNull Vector2d A, @NotNull Vector2d B) {
        gl2.glBegin(GL_LINES);
        gl2.glVertex2d(A.x, A.y);
        gl2.glVertex2d(B.x, B.y);
        gl2.glEnd();
    }

    /**
     * Задать режим рисования OpenGL линиями
     *
     * @param gl2 переменная рисования OpenGL
     */
    public static void setRenderLineMode(GL2 gl2) {
        gl2.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    /**
     * Задать режим рисования OpenGL линиями
     *
     * @param gl2 переменная рисования OpenGL
     */
    public static void setRenderFillMode(GL2 gl2) {
        gl2.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }


    /**
     * Конструктор для запрета наследования
     */
    private GLAlgorithms() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
