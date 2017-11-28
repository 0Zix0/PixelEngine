package com.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.Action;

public class Input {

	public static boolean keyDown(int keycode) {
		return Keyboard.keys[keycode];
	}

	public static boolean mouseButtonDown(int button) {
		return Mouse.buttons[button];
	}
	
	public static boolean mouseButtonDownR(int button) {
		boolean status = Mouse.buttons[button];
		Mouse.buttons[button] = false;
		return status;
	}
	
	public static double mouseX() {
		return Mouse.mouseX;
	}
	
	public static double mouseY() {
		return Mouse.mouseY;
	}
	
	//public static void addMousePressListener(Action action) {
	//	Mouse.mousePressListeners.add(action);
	//}
	
	//public static void addMouseReleaseListener(Action action) {
	//	Mouse.mouseReleaseListeners.add(action);
	//}
	
	//public static void addKeyTypedListener(Action action) {
	//	Keyboard.keyTypedListeners.add(action);
	//}
	
	//public static void addKeyPressListener(Action action) {
	//	Keyboard.keyPressListeners.add(action);
	//}
	
	public static class Mouse implements MouseListener, MouseMotionListener {

		private static boolean[] buttons = new boolean[64];
		
		private static double mouseX = 0;
		private static double mouseY = 0;

		//private static ArrayList<Action> mousePressListeners = new ArrayList<Action>();
		//private static ArrayList<Action> mouseReleaseListeners = new ArrayList<Action>();
		
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();		
		}

		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}
		
		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			//for(Action a : mousePressListeners) {
			//	a.invoke(e);
			//}
			buttons[e.getButton()] = true;
		}

		public void mouseReleased(MouseEvent e) {
			//for(Action a : mouseReleaseListeners) {
			//	a.invoke(e);
			//}
			buttons[e.getButton()] = false;
		}
	}
	
	public static class Keyboard implements KeyListener {

		//private static ArrayList<Action> keyTypedListeners = new ArrayList<Action>();
		//private static ArrayList<Action> keyPressListeners = new ArrayList<Action>();
		
		private static boolean[] keys = new boolean[1024];
		
		public void keyPressed(KeyEvent e) {
			keys[e.getKeyCode()] = true;
			//for(Action a : keyPressListeners) {
			//	a.invoke(e);
			//}
		}

		public void keyReleased(KeyEvent e) {
			keys[e.getKeyCode()] = false;
		}

		public void keyTyped(KeyEvent e) {
			//for(Action a : keyTypedListeners) {
			//	a.invoke(e);
			//}
		}
	}
}
