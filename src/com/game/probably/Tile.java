package com.game.probably;

public class Tile {
	int[] pixels;
	int size;
	public Tile(int[] data, int size) {
		pixels = data;
		this.size = size;
	}
	
	public int TileSize() {
		 return size;
	}
	
	public int[] getData() {
		return pixels;
	}
	
	public int HowManyTiles() {
		return pixels.length / size;
	}
}
