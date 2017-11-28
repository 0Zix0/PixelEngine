package com.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public abstract class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public abstract void onCreate();
	public abstract void onUpdate();
	public abstract void onRender(Renderer renderer);

	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private int width, height, scale;
	private String title;
	private BufferedImage image;
	private int[] pixels;	
	private Renderer renderer;
	// Start Stats
	private List<Integer> loggedFPS = new ArrayList<Integer>();
	private long startTime;
	// End Stats
	
	protected void start(String title, int width, int height, int scale) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.title = title;
		
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		renderer = new Renderer(width, height, pixels);
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int averageFPS = 0;
				for(Integer f : loggedFPS) {
					averageFPS += f;
				}
				if(loggedFPS.size() != 0) {
					averageFPS = averageFPS / loggedFPS.size();
				}
				
				//System.out.println("Ran for " + (System.currentTimeMillis() - startTime) / 1000.0f + " seconds");
				//System.out.println("Average FPS was " + averageFPS + "fps");
				
				super.windowClosing(e);
			}
		});

		setFocusTraversalKeysEnabled(false);
		addKeyListener(new Input.Keyboard());
		addMouseListener(new Input.Mouse());
		addMouseMotionListener(new Input.Mouse());
		
		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		requestFocus();
		
		onCreate();
		
		running = true;
		thread = new Thread(this, "SimpleEngine");
		thread.start();
		startTime = System.currentTimeMillis();
	}
	
	protected void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		renderer.clear();
		onRender(renderer);
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		
		g.dispose();
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				onUpdate();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				loggedFPS.add(frames);
				updates = 0;
				frames = 0;
			}
		}
	}
	
	public int screenWidth() {
		return width;
	}
	
	public int screenHeight() {
		return height;
	}
	
	public int scale() {
		return scale;
	}
}
