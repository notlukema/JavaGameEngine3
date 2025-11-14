
package jge3.game.scenes.components;

import static jge3.game.PlatformerGame.*;

import jge3.engine.display.Window;
import jge3.engine.graphics.Graphics2d;
import jge3.game.PlatformerGame;

public class InfoDisplay extends Component {
	
	public static final InfoDisplay component = new InfoDisplay();
	
	private Graphics2d g2d;
	
	private InfoDisplay() {
	}
	
	@Override
	public void initialize(Window window) {
		g2d = createGraphics2d(window);
	}
	
	@Override
	public boolean update(float delta, Window window) {
		
		resetView(g2d);
		g2d.bind();
		g2d.clearData();
		g2d.setColor(255, 255, 255);
		g2d.setFont(FONT);
		g2d.drawString("FPS: "+PlatformerGame.getFPS(), -580, 330, 30, -1, -1);
		g2d.unbind();
		
		return false;
	}
	
}
