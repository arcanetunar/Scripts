import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

@Manifest(authors = { "Arcanetunar" }, description = "Vial Filler", name = "ArcaneTunar_VialFiller")
public class Arcanetunar_GEVialFiller extends ActiveScript {
  
	public static int vials = 229;
	public static int wvials = 227;
	public SceneObject fonta;
	public static int fontain = 47150;
	public static int banker = 3416;
	public Area ftile = new Area (new Tile[]{new Tile(3159, 3491, 0), new Tile (3159, 39492, 0)});

	public synchronized final void provide(final Node... jobs){ //Let people choose their jobs
		for (final Node job : jobs){
			if (!jobsCollection.contains(job)){
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	public synchronized final void revoke(final Node... jobs) { //Delete jobs
		for (final Node job : jobs) {
			if (jobsCollection.contains(job)){
				jobsCollection.remove(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	public final void submit(final Job... jobs){
		for (final Job job : jobs){
			getContainer().submit(job);
		}
	}
	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;
	
	public void onStart() {
		provide(new filler(),new banking());
	}
		
		@Override
		public int loop() {
			if (jobContainer !=null) {
				final Node job = jobContainer.state();
				if (job !=null){
					jobContainer.set(job);
					getContainer().submit(job);
					job.join();
				}
			}
			return Random.nextInt(10, 50);
		}

		public class filler extends Node {

			@Override
			public boolean activate() {
				
				return Inventory.getCount(vials) == 28;
			}

			@Override
			public void execute() {	
				fonta = SceneEntities.getNearest(fontain);			
				Item vial = Inventory.getItem(vials); 
				WidgetChild fill = Widgets.get(1371, 5);
				WidgetChild camera = Widgets.get(548, 3);
				boolean selected = false;
				if (!vial.getWidgetChild().contains(Mouse.getLocation()) && !selected){
					Mouse.click(camera.getCentralPoint(), true);
					Camera.turnTo(fonta);
					vial.getWidgetChild().interact("Use"); 
					selected = true;
			
				}else{
					fonta.click(true);
					final Timer timer = new Timer(5000);
					while (!fill.validate()) {
						Task.sleep(100);
						if (!timer.isRunning())
							break;
					}
					if(fill.validate()) {
						Mouse.click(fill.getCentralPoint(), true);
				}
			}
		}
		
	}
		
		public class banking extends Node {

			@Override
			public boolean activate() {
				
				return Inventory.getCount(vials) == 0;
			}

			@Override
			public void execute() {
				NPC banker1 = NPCs.getNearest(banker);
			if (Bank.isOpen()){
				Bank.deposit(wvials, Bank.Amount.ALL);
				Bank.withdraw(vials, Bank.Amount.ALL);
				Bank.close();
				
			}else {
				if(!Bank.isOpen()) {
					Camera.turnTo(banker1);
					banker1.interact("Bank");
					Task.sleep(3000);
					}
				}
			}
				
			}
			
		}

		


