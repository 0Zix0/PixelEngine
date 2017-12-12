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

/**
 * This class represents an abstract game.
 * In order to create your own game you simply need to create a class
 * which inherits from this base class and implement the methods
 * 'onCreate()', 'onRender()' and 'onUpdate()'.
 * In order to run the game you must create a new instance of it and call
 * the 'start()' method with your desired parameters.
 */
public abstract class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	/**
	 * Called once the game object has been created.
	 */
	public abstract void onCreate();
	/**
	 * Called 60 times a second, put input code in here.
	 */
	public abstract void onUpdate();
	/**
	 * Called as many times as possible to render the game at the highest
	 * possible frame rate. Use the provided renderer instance to render
	 * to the screen.
	 */
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
	private boolean debug = false;
	private List<Integer> loggedFPS = new ArrayList<Integer>();
	private long startTime;
	private float currentFPS = 0;
	private float currentUPS = 0;
	// End Stats
	
	/**
	 * Creates the window and instantiates all required objects for the program
	 * to run.
	 * @param title The title of the window.
	 * @param width How many pixels the screen should be wide.
	 * @param height How many pixels the screen should be high.
	 * @param scale How many physical pixels on the screen should each pixel display as.
	 */
	protected void start(String title, int width, int height, int scale) {
		start(title, width, height, scale, false);
	}
	
	/**
	 * Creates the window and instantiates all required objects for the program
	 * to run.
	 * @param title The title of the window.
	 * @param width How many pixels the screen should be wide.
	 * @param height How many pixels the screen should be high.
	 * @param scale How many physical pixels on the screen should each pixel display as.
	 * @param debug Whether or now to display debug information in the corner of the screen.
	 */
	protected void start(String title, int width, int height, int scale, boolean debug) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.title = title;
		this.debug = debug;
		
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
				
				float runTime = (System.currentTimeMillis() - startTime) / 1000.0f;
				
				System.out.println("Ran for " + runTime + " seconds");
				System.out.println("Average FPS was " + averageFPS + "fps");
				
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
		if(debug) {
			g.drawString("FPS: " + currentFPS, 3, 15);
			g.drawString("USP: " + currentUPS, 3, 15 + 12 + 3);
		}
		
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
				currentFPS = frames;
				currentUPS = updates;
				//frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
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
