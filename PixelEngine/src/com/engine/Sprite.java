package com.engine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sprite {

	private short[] colors;
	private short[] glyphs;
	private int width, height;
	
	public Sprite(short[] colors, short[] glyphs, int width, int height) {
		this.colors = colors;
		this.glyphs = glyphs;
		this.width = width;
		this.height = height;
	}
	
	public short getGlyph(int x, int y) {
		if(x < 0 || x > width || y < 0 || y > height) {
			return ' ';
		} else {
			return glyphs[y * width + x];
		}
	}
	
	public short getColor(int x, int y) {
		if(x < 0 || x > width || y < 0 || y > height) {
			return 0x000000;
		} else {
			return colors[y * width + x];
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Loads a sprite file.
	 * Sprite files should have the extension '.spr'.
	 * @param path The path to the sprite file.
	 * @return A sprite object which can be used to draw.
	 */
	public static Sprite load(String path) {
		short[] colors = null;
		short[] glyphs = null;
		int width = 0;
		int height = 0;
		
		ByteBuffer in = null;
		try {
			in = ByteBuffer.wrap(Files.readAllBytes(Paths.get(path))).order(ByteOrder.LITTLE_ENDIAN);
		} catch (IOException e) {
			return null;
		}
		width = in.getInt();
		height = in.getInt();
		colors = new short[width * height];
		glyphs = new short[width * height];
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = in.getShort();
		}
		for(int i = 0; i < glyphs.length; i++) {
			glyphs[i] = in.getShort();
		}
		
		return new Sprite(colors, glyphs, width, height);
	}
}
