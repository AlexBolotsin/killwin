package com.game.probably;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TailSheet {
	private BufferedImage sheet;
	private final int TILE_SIZE = 16, SHEET_SIZE = 256;

	public TailSheet(String icons) {
		try {
			sheet = ImageIO.read(getClass().getResourceAsStream((icons)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int[] GetPixels(int x, int y, int tiles) {
		return null;
	}

	private int[] GetPixels(int x, int y) throws Exception {
		if (TILE_SIZE + x >= SHEET_SIZE || TILE_SIZE + y >= SHEET_SIZE)
			throw new Exception("Out of bound.");
		int[] pixels = new int[TILE_SIZE*TILE_SIZE];
		int p = 0;
		for (int i = x; i < TILE_SIZE + x; i++)
			for (int j = y; j < TILE_SIZE + y; j++)
				pixels[p++] = sheet.getRGB(j, i);
		return pixels;
	}

	public Tile GetTile(int x, int y) {
		try {
			return new Tile(GetPixels(x * TILE_SIZE, y * TILE_SIZE), 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Tile GetTiles(int x, int y, int tiles) {
		return new Tile(GetPixels(x, y, tiles), tiles);
	}
}
