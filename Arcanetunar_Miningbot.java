import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

@Manifest(authors = { "arcanetunar" }, description = "A Lumbridge PowerMiner made by arcanetunar", name = "Arcanetunar_PowerMiner")
public class Arcanetunar_PowerMiner extends ActiveScript implements PaintListener {
  
	private int ORE_ID[] = {436, 438};
	
	
	
	private final Area MINE_AREA = new Area(new Tile[] { new Tile(3220, 3150, 0), new Tile(3229, 3153, 0), new Tile(3235, 3153, 0), 
			new Tile(3232, 3145, 0), new Tile(3226, 3143, 0), new Tile(3221, 3145, 0) });
	
	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;
	
	public synchronized final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if (!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	public synchronized final void revoke(final Node... jobs) {
		for (final Node job : jobs) {
			if (jobsCollection.contains(job)){
				jobsCollection.remove(job);
			}	
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	public final void submit(final Job... jobs) {
		for (final Job job : jobs){
			getContainer().submit(job);
		}
	}
	
	@Override
	public void onStart() {
		provide(new mine());
	}
	
	@Override
	public int loop() {
		if (jobContainer !=null) {
			final Node job = jobContainer.state();
			if (job !=null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		}
		if (Players.getLocal().isIdle()) {
				if (Inventory.getCount(ORE_ID) > 1) {
					Keyboard.sendKey('1');
					Keyboard.sendKey('2');
				}
			}
		return Random.nextInt(10, 50);
	}

public class mine extends Node {

	private final int ROCK_IDS[] = {3027, 3038, 3229, 3245};
	
	
	
	@Override
	public boolean activate() {
		return Inventory.getCount() < 28 && MINE_AREA.contains(Players.getLocal().getLocation())
				&& Players.getLocal().isIdle();
	}

	@Override
	public void execute() {
		SceneObject rock = SceneEntities.getNearest(ROCK_IDS);
		if(rock !=null) {
			rock.interact("Mine");
			Task.sleep(1100, 1400);
			
		}
	}
	
}
private final Color color1 = new Color(255, 255, 255);
private final Color color2 = new Color(0, 0, 0);
private final Color color3 = new Color(51, 51, 255);
private final Color color4 = new Color(255, 0, 0);

private final BasicStroke stroke1 = new BasicStroke(1);

private final Font font1 = new Font("BigNoodleTitling", 0, 32);
private final Font font2 = new Font("BigNoodleTitling", 0, 25);

public void onRepaint(Graphics g1) {
    Graphics2D g = (Graphics2D)g1;
    g.setColor(color1);
    g.setColor(color2);
    g.setStroke(stroke1);
    g.setFont(font1);
    g.setColor(color3);
    g.drawString("Arcanetunar_Lumbridge PowerMiner", 8, 308);
    g.setFont(font2);
    g.setColor(color4);


}
}

