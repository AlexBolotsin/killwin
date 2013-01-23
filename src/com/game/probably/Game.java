package com.game.probably;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public final static String GAME_TITLE = "Untitled";
	public final static int SCALE = 3;
	public final static int WIDTH = 160, HEIGHT = 120;
	public int[] colors = new int[256];
	BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_BGR);
	int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private TailSheet sheet;

	public static void main(String[] args) {
		Game game = new Game();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}

	private void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		boolean running = true;
		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void init() {
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					colors[pp++] = r1 << 16 | g1 << 8 | b1;
				}
			}
		}
		loadImage();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.white.brighter());
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.blue.brighter());

		modifyImage();

		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, Color.black, null);
		g.dispose();
		bs.show();
	}

	private void modifyImage() {
		DrawTile(0, 0, sheet.GetTile(0, 0));
		DrawTile(0, 16, sheet.GetTile(0, 1));
		DrawTile(16, 0, sheet.GetTile(1, 0));
		DrawTile(16, 16, sheet.GetTile(1, 1));
	}

	private void DrawTile(int xx, int yy, Tile tile) {
		int[] data = tile.getData();
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 16; j++)
				if (data[i + j * 16] != -1)
					pixels[yy + i + (xx + j) * WIDTH] = data[i + j * 16];
	}

	private void loadImage() {
		sheet = new TailSheet("/icons.png");
	}

	private void tick() {

	}

}
