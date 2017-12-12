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
	
	public static boolean keyDownReset(int keycode) {	
		boolean status = Keyboard.keys[keycode];
		Keyboard.keys[keycode] = false;
		return status;
	}

	public static boolean mouseButtonDown(int button) {
		return Mouse.buttons[button];
	}
	
	public static boolean mouseButtonDownReset(int button) {
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
	
	public static class Mouse implements MouseListener, MouseMotionListener {

		private static boolean[] buttons = new boolean[64];
		
		private static double mouseX = 0;
		private static double mouseY = 0;
		
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();		
		}

		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			buttons[e.getButton()] = true;
		}

		public void mouseReleased(MouseEvent e) {
			buttons[e.getButton()] = false;
		}
	}
	
	public static class Keyboard implements KeyListener {

		private static boolean[] keys = new boolean[1024];
		
		public void keyPressed(KeyEvent e) {
			keys[e.getKeyCode()] = true;
		}

		public void keyReleased(KeyEvent e) {
			keys[e.getKeyCode()] = false;
		}

		public void keyTyped(KeyEvent e) {}
	}
}
