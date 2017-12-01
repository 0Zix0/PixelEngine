# PixelEngine

### How to use
1. Extend the abstract class `Game`
2. Implement the 3 methods `onCreate()`, `onUpdate()` and `onRender()`  
3. Create a new instance of your classe and call the `start()` method.
```java
public class MyGame extends Game {

	public void onCreate() {
		// Called when the application is created.
		// You can use this for initialization.
	}

	public void onUpdate() {
		// Called 60 times a second.
		// Use this for input.
	}

	public void onRender(Renderer renderer) {
		// Called as many times as possible.
		// Used the supplied renderer instance to
		// draw to the screen.
	}
	
	public static void main(String[] args) {
		MyGame game = new MyGame();
		game.start("My Game!", 300, 150, 3);
	}
}
```
