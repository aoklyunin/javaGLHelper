package com.github.aoklyunin.javaGLHelper;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2i;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector2i;
import jMath.aoklyunin.github.com.vector.Vector3d;
import jMath.aoklyunin.github.com.vector.Vector4d;

import java.awt.*;
import java.util.Objects;

/**
 * контроллер текста
 */
public class GLTextController {
    /**
     * параметры текста
     */
    @NotNull
    private final CaptionParams captionParams;
    /**
     * контроллер текста от jogamp
     */
    @NotNull
    private final TextRenderer textRenderer;
    /**
     * ширина окна
     */
    private final int clientWidth;
    /**
     * высота окна
     */
    private final int clientHeight;
    /**
     * система координат OpenGL
     */
    @NotNull
    private final CoordinateSystem2d glCS;
    /**
     * система координат экрана
     */
    @NotNull
    private final CoordinateSystem2i clientCS;

    /**
     * Конструктор контроллера текста
     *
     * @param clientWidth   ширина окна
     * @param clientHeight  высота окна
     * @param captionParams параметры текста
     */
    public GLTextController(int clientWidth, int clientHeight, @NotNull CaptionParams captionParams) {
        this.clientHeight = clientHeight;
        this.clientWidth = clientWidth;
        this.glCS = new CoordinateSystem2d(0, 1, 0, 1);
        this.clientCS = new CoordinateSystem2i(0, clientWidth, 0, clientHeight);
        textRenderer = new TextRenderer(new Font(
                captionParams.getFontName(), captionParams.isBold() ? Font.BOLD : Font.PLAIN, captionParams.getFontSize())
        );
        this.captionParams = captionParams;
    }

    /**
     * Вывести текст на экран
     *
     * @param text текст
     */
    public void drawText(@NotNull String text) {
        drawText(Objects.requireNonNull(text), captionParams.getPos(), captionParams.getColor());
    }

    /**
     * Вывести текст на экран
     *
     * @param text текст
     * @param pos  положение на экране в СК OpenGL
     */
    public void drawText(@NotNull String text, @NotNull Vector2d pos) {
        Vector2i posI = clientCS.getCoords(Objects.requireNonNull(pos), glCS);
        drawText(Objects.requireNonNull(text), posI, captionParams.getColor());
    }

    /**
     * Вывести текст на экран
     *
     * @param text  текст
     * @param pos   положение на экране в СК OpenGL
     * @param color цвет текста
     */
    public void drawText(@NotNull String text, @NotNull Vector2d pos, @Nullable Vector3d color) {
        Vector2i posI = clientCS.getCoords(Objects.requireNonNull(pos), glCS);
        drawText(Objects.requireNonNull(text), posI, color);
    }

    /**
     * Вывести текст на экран
     *
     * @param text  текст
     * @param pos   положение на экране в СК окна
     * @param color цвет текста
     */
    private void drawText(@NotNull String text, @NotNull Vector2i pos, @Nullable Vector3d color) {
        color = Objects.requireNonNullElse(color, Vector3d.ones());
        textRenderer.beginRendering(clientWidth, clientHeight);
        textRenderer.setColor((float) color.x, (float) color.y, (float) color.z, 1);
        textRenderer.draw(text, pos.x, pos.y);
        textRenderer.endRendering();
    }

    /**
     * Вывести текст на экран
     *
     * @param text  текст
     * @param pos   положение на экране в СК OpenGL
     * @param color цвет текста
     */
    void drawText(@NotNull String text, @NotNull Vector2d pos, @NotNull Vector4d color) {
        Vector2i posI = clientCS.getCoords(pos, glCS);
        drawText(text, posI, color);
    }

    /**
     * Вывести текст на экран
     *
     * @param text  текст
     * @param pos   положение на экране в СК окна
     * @param color цвет текста
     */
    private void drawText(@NotNull String text, @NotNull Vector2i pos, @Nullable Vector4d color) {
        color = Objects.requireNonNullElse(color, new Vector4d(1, 1, 1, 1));
        textRenderer.beginRendering(clientWidth, clientHeight);
        textRenderer.setColor((float) color.x, (float) color.y, (float) color.z, (float) color.w);
        textRenderer.draw(text, pos.x, pos.y);
        textRenderer.endRendering();
    }

    /**
     * Получить параметры заголовков
     *
     * @return параметры заголовков
     */
    @NotNull
    public CaptionParams getCaptionParams() {
        return captionParams;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GLTextController{getString()}"
     */

    @Override
    public String toString() {
        return "GLTextController{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "textRenderer, clientWidth, clientHeight, glCS, clientCS"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return textRenderer + ", " + clientWidth + ", " + clientHeight + ", " + glCS + ", " + clientCS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GLTextController that = (GLTextController) o;

        if (clientWidth != that.clientWidth) return false;
        if (clientHeight != that.clientHeight) return false;
        if (!Objects.equals(captionParams, that.captionParams))
            return false;
        if (!Objects.equals(textRenderer, that.textRenderer)) return false;
        if (!Objects.equals(glCS, that.glCS)) return false;
        return Objects.equals(clientCS, that.clientCS);
    }

    @Override
    public int hashCode() {
        int result = captionParams.hashCode();
        result = 31 * result + textRenderer.hashCode();
        result = 31 * result + clientWidth;
        result = 31 * result + clientHeight;
        result = 31 * result + glCS.hashCode();
        result = 31 * result + clientCS.hashCode();
        return result;
    }
}
